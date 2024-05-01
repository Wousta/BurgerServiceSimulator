package modelling;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import entities.Cocinero;
import entities.Dependiente;

public class PCocinadoEv extends EventOf2Entities<Cocinero,Dependiente>{

    private BurgerRestModel model;

    public PCocinadoEv(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestModel) owner;
    }

    @Override
    public void eventRoutine(Cocinero cocinero, Dependiente dependiente) {
        if(dependiente.getClienteAsignado() == null) {
            sendTraceNote("ALERT!!! NULL client is going to pay");
        }
        else {
            sendTraceNote(
            "Se ha cocinado el pedido asignado al dependiente: " 
            + dependiente.getName() 
            + " por el cocinero: " 
            + cocinero.getName());
        }

        model.dependientesQ.remove(dependiente);
        CPagadaEv comidaPagada = new CPagadaEv(
            model, 
            "ComidaPagadaEvent", 
            true);

        if(!model.dependientesQ.isEmpty()){
            Dependiente nextDependiente = model.dependientesQ.first();
            PCocinadoEv pedidoCocinado = new PCocinadoEv(
                model, 
                "PedidoCocinadoEvent", 
                true);

            comidaPagada.schedule(
                dependiente, 
                new TimeSpan(model.getPagaComidaT(), TimeUnit.MINUTES));
            pedidoCocinado.schedule(
                cocinero, 
                nextDependiente, 
                new TimeSpan(model.getPreparaComidaT(), TimeUnit.MINUTES));
        }
        else {
            comidaPagada.schedule(
                dependiente, 
                new TimeSpan(model.getPagaComidaT(), TimeUnit.MINUTES));
            model.idleCocinerosQ.insert(cocinero);
        }      
    }

}
