package modelling;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

import entities.Cliente;

public class GenClienteEvent extends ExternalEvent {

    public GenClienteEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() {
        BurgerRestaurantModel model = (BurgerRestaurantModel) getModel();
        Cliente cliente = new Cliente(model, "Cliente", true);

        LlegaClienteEvent llegaCliente = new LlegaClienteEvent(model, "Cliente llega al restaurante", true);
        llegaCliente.schedule(cliente, new TimeSpan(0.0));

        // Schedule this event again
        schedule(new TimeSpan(model.getLlegaClienteT(), TimeUnit.MINUTES));

        model.clientsArrived.update(++model.arrivedClients);
    }

}