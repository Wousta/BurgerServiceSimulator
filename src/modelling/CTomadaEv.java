package modelling;

import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import entities.Cocinero;
import entities.Dependiente;

public class CTomadaEv extends Event<Dependiente>{
    private BurgerRestModel model;

    public CTomadaEv(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestModel) owner;
    }

    @Override
    public void eventRoutine(Dependiente dependiente) throws SuspendExecution {
        model.dependientesQ.insert(dependiente);
        sendTraceNote(
            "Comanda tomada, Dependiente " 
            + dependiente 
            + " a la espera de cocinero");

        if(!model.idleCocinerosQ.isEmpty()){
            Cocinero cocinero = model.idleCocinerosQ.first();
            PCocinadoEv pedidoCocinado = new PCocinadoEv(
                model, 
                "PedidoCocinadoEvent", 
                true);

            model.idleCocinerosQ.remove(cocinero);
            pedidoCocinado.schedule(
                cocinero, 
                dependiente, 
                new TimeSpan(model.getPreparaComidaT(), TimeUnit.MINUTES));
        }
    }
}
