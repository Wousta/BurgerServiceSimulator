package modelling;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import entities.Dependiente;

public class ComandaTomadaEvent extends Event<Dependiente>{

    public ComandaTomadaEvent(Model arg0, String arg1, boolean arg2) {
        super(arg0, arg1, arg2);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void eventRoutine(Dependiente arg0) throws SuspendExecution {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eventRoutine'");
    }

}
