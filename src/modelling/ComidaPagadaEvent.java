package modelling;

import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import entities.Cliente;
import entities.Dependiente;

public class ComidaPagadaEvent extends Event<Dependiente>{

    private BurgerRestaurantModel model;

    public ComidaPagadaEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestaurantModel) owner;
    }

    @Override
    public void eventRoutine(Dependiente dependiente) throws SuspendExecution {
        if(dependiente.getClienteAsignado() == null) {
            sendTraceNote("NULL CLIENTE????");
        }
        else {
            sendTraceNote(
                "Ha pagado el cliente: " + dependiente.getClienteAsignado().getName() + 
                " al dependiente: " + dependiente.getName()
            );
        }

        if(!model.clientesQ.isEmpty()) {
            sendTraceNote("COMIDA_PAGADA_EV: la cola clientes no esta vacia");
            Cliente cliente = model.clientesQ.first();
            model.clientesQ.remove(cliente);

            dependiente.setClienteAsignado(cliente);

            ComandaTomadaEvent comandaTomada = new ComandaTomadaEvent(model, "ComandaTomadaEvent", true);

            comandaTomada.schedule(dependiente, new TimeSpan(model.getTomaComandaT(), TimeUnit.MINUTES));
        }
        else {
            sendTraceNote("!COMIDA_PAGADA_EV: la cola clientes esta vacia");
            model.idleDependientesQ.insert(dependiente);
        }

        model.clientsServiced.update(++model.servicedClients);
        //model.waitTimeHistogram.update(null);
    }

}
