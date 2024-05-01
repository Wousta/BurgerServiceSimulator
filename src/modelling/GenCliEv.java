package modelling;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import entities.Cliente;

public class GenCliEv extends ExternalEvent {
    private BurgerRestModel model;

    public GenCliEv(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (BurgerRestModel)owner;
    }

    @Override
    public void eventRoutine() {
        Cliente cliente = new Cliente(
            model, 
            "Cliente", 
            true);
        LlegaCliEv llegaCliente = new LlegaCliEv(
            model, 
            "Cliente llega al restaurante", 
            true);

        llegaCliente.schedule(cliente, new TimeSpan(0.0));
        // Schedule this event again
        schedule(new TimeSpan(model.getLlegaClienteT(), TimeUnit.MINUTES));
        model.clientsArrived.update(++model.arrivedClients);
    }

}
