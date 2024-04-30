package modelling;

import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import entities.Cocinero;
import entities.Dependiente;

public class ComandaTomadaEvent extends Event<Dependiente>{

    private BurgerRestaurantModel model;

    public ComandaTomadaEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestaurantModel) owner;
    }

    @Override
    public void eventRoutine(Dependiente dependiente) throws SuspendExecution {
        
        model.dependientesQ.insert(dependiente);
        sendTraceNote("Comanda tomada, Dependiente " + dependiente +  "a la espera de cocinero");

        if(!model.idleCocinerosQ.isEmpty()){
            Cocinero cocinero = model.idleCocinerosQ.first();
            model.idleCocinerosQ.remove(cocinero);

            //model.dependientesQ.remove(dependiente);

            PedidoCocinadoEvent pedidoCocinado = new PedidoCocinadoEvent(model, "PedidoCocinadoEvent", true);
            pedidoCocinado.schedule(cocinero, dependiente, new TimeSpan(model.getPreparaComidaT(), TimeUnit.MINUTES));
        }
    }

}
