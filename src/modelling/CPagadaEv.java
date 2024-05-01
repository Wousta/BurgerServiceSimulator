package modelling;

import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import entities.Cliente;
import entities.Dependiente;

public class CPagadaEv extends Event<Dependiente>{
    private BurgerRestaurantModel model;

    public CPagadaEv(Model owner, String name, boolean showInTrace) {
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
                "Ha pagado el cliente: " 
                + dependiente.getClienteAsignado().getName() 
                + " al dependiente: " + dependiente.getName());
            model.clientsServiced.update(++model.servicedClients);
        }

        if(!model.clientesQ.isEmpty()) {
            sendTraceNote("COMIDA_PAGADA_EV: la cola clientes no esta vacia");
            Cliente cliente = model.clientesQ.first();
            CTomadaEv comandaTomada = new CTomadaEv(
                model, 
                "ComandaTomadaEvent", 
                true);

            model.clientesQ.remove(cliente);
            dependiente.setClienteAsignado(cliente);
            comandaTomada.schedule(
                dependiente, 
                new TimeSpan(model.getTomaComandaT(), TimeUnit.MINUTES));
        }
        else {
            sendTraceNote("!COMIDA_PAGADA_EV: la cola clientes esta vacia");
            model.idleDependientesQ.insert(dependiente);
        }

    }

}
