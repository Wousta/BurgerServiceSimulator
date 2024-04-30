package modelling;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import entities.Cocinero;
import entities.Dependiente;

public class PedidoCocinadoEvent extends EventOf2Entities<Cocinero,Dependiente>{

    private BurgerRestaurantModel model;

    public PedidoCocinadoEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestaurantModel) owner;
    }

    @Override
    public void eventRoutine(Cocinero cocinero, Dependiente dependiente) {
        sendTraceNote(
            "Se ha cocinado el pedido asignado al dependiente: " + dependiente.getName() + 
            " por el cocinero: " + cocinero.getName()
        );

        model.dependientesQ.remove(dependiente);
        ComidaPagadaEvent comidaPagada = new ComidaPagadaEvent(model, "ComidaPagadaEvent", true);

        if(!model.dependientesQ.isEmpty()){
            Dependiente nextDependiente = model.dependientesQ.first();
            //model.dependientesQ.remove(nextDependiente);

            PedidoCocinadoEvent pedidoCocinado = new PedidoCocinadoEvent(model, "PedidoCocinadoEvent", true);

            comidaPagada.schedule(dependiente, new TimeSpan(model.getPagaComidaT(), TimeUnit.MINUTES));
            pedidoCocinado.schedule(cocinero, nextDependiente, new TimeSpan(model.getPreparaComidaT(), TimeUnit.MINUTES));
        }
        else {
            comidaPagada.schedule(dependiente, new TimeSpan(model.getPagaComidaT(), TimeUnit.MINUTES));
            model.idleCocinerosQ.insert(cocinero);
        }      
    }

}
