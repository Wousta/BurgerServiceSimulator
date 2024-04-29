package modelling;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import entities.Cliente;
import entities.Dependiente;

public class LlegaClienteEvent extends Event<Cliente>{

    private BurgerRestaurantModel model;

    public LlegaClienteEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestaurantModel)owner;
    }

    @Override
    public void eventRoutine(Cliente cliente) {
        model.clientesQ.insert(cliente);
        sendTraceNote("Cliente llega al restaurante: " + model.clientesQ.length() + " clientes en cola");
        
        if(!model.idleDependientesQ.isEmpty()) {
            Dependiente dependiente = model.idleDependientesQ.first();
            model.idleDependientesQ.remove(dependiente);

            dependiente.setClienteAsignado(cliente);

            model.clientesQ.remove(cliente);

            ComandaTomadaEvent comandaTomada = new ComandaTomadaEvent(model, "ComandaTomadaEvent", true);

            comandaTomada.schedule(dependiente, new TimeSpan(model.getTomaComandaT(), TimeUnit.MINUTES));
        }
    }
}
