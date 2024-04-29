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
        sendTraceNote(
            "Ha pagado el cliente: " + dependiente.getClienteAsignado().getName() + 
            " al dependiente: " + dependiente.getName()
        );

        if(!model.clientesQ.isEmpty()) {
            Cliente cliente = model.clientesQ.first();
            model.clientesQ.remove(cliente);

            ComandaTomadaEvent comandaTomada = new ComandaTomadaEvent(model, "ComandaTomadaEvent", true);

            comandaTomada.schedule(dependiente, new TimeSpan(model.getTomaComandaT(), TimeUnit.MINUTES));
        }
        else {
            model.idleDependientesQ.insert(dependiente);
        }
    }

}
