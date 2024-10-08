package modelling;

import java.util.concurrent.TimeUnit;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeOperations;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Histogram;
import desmoj.core.statistic.TimeSeries;
import entities.Cliente;
import entities.Cocinero;
import entities.Dependiente;

public class BurgerRestModel extends Model {

    protected static final int NUM_DEPENDIENTES = 2;
    protected static final int NUM_COCINEROS = 3;

    protected TimeSeries clientsArrived;
    protected TimeSeries clientsServiced;
    protected Histogram waitTimeHistogram;
    protected int arrivedClients = 0;
    protected int servicedClients = 0;

    private TimeInstant startWait;
    private TimeInstant endWait;

    private ContDistExponential llegadaClienteT;
    private ContDistExponential tomaComandaT;
    private ContDistExponential preparaComidaT;
    private ContDistExponential pagaComidaT;

    protected Queue<Cliente> clientesQ;
    // idleDependientesQ are free dependientes, dependientesQ are dependientes with
    // client assigned
    protected Queue<Dependiente> idleDependientesQ;
    protected Queue<Dependiente> dependientesQ;
    protected Queue<Cocinero> idleCocinerosQ;

    /**
     * Creates a new BurgerRestaurantModel model via calling
     * the constructor of the superclass.
     *
     * @param owner        the model this model is part of (set to <tt>null</tt>
     *                     when there is no such model)
     * @param modelName    this model's name
     * @param showInReport flag to indicate if this model shall produce output to
     *                     the report file
     * @param showInTrace  flag to indicate if this model shall produce output to
     *                     the trace file
     */
    public BurgerRestModel(
            Model owner, 
            String name, 
            boolean showInReport, 
            boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    /**
     * Returns a description of the model to be used in the report.
     * 
     * @return model description as a string
     */
    public String description() {
        return "This model describes a restaurant where clients arrive to a single queue "
                + "and have the order taken by the dependientes in order of arrival. "
                + "When an order is taken by a dependiente, it waits in the busy dependientes "
                + "Queue until the cocinero is free and can start cooking the order. "
                + "Once the order has been cooked the client pays for it and leaves.\n"
                + "The clients leave the waiting queue when they have their order taken by the dependiente.\n"
                + "The dependientes do not serve any other customer until payment has been made.\n"
                + "The cooks exclusively prepare a customer's order and until they have finished " 
                + "with the entire order they do not start the next one.";
    }

    // Creates the first event to kickoff the simulation
    public void doInitialSchedules() {
        GenCliEv genCliente = new GenCliEv(
            this, 
            "Client generator", 
            true);

        genCliente.schedule(new TimeSpan(0.0));
    }

    // Initialises static model components like distributions and queues.
    public void init() {
        clientsArrived = new TimeSeries(this, "arrived", new TimeInstant(0), new TimeInstant(1500), true, false);
        clientsServiced = new TimeSeries(this, "finished", new TimeInstant(0), new TimeInstant(1500), true, false);
        waitTimeHistogram = new Histogram(this, "Client Wait Times", 0, 16, 10, true, false);

        /*
            * Initialize the random number generators for the inter-arrival times and the Queues
            * Parameters:
            * Model = the model this distribution is associated with
            * String = this distribution's name
            * double = the mean inter-arrival time
            * boolean = show in report?
            * boolean = show in trace?
            */
        llegadaClienteT = new ContDistExponential(this, "Llegada Cliente Time", 5.0, true, false);
        tomaComandaT = new ContDistExponential(this, "Toma Comanda Time", 4.0, true, false);
        preparaComidaT = new ContDistExponential(this, "Prepara Comida Time", 7.0, true, false);
        pagaComidaT = new ContDistExponential(this, "Paga Comida Time", 1.5, true, false);

        // necessary because an inter-arrival time can not be negative, but
        // a sample of an exponential distribution can...
        llegadaClienteT.setNonNegative(true);
        tomaComandaT.setNonNegative(true);
        preparaComidaT.setNonNegative(true);

        clientesQ = new Queue<>(this, "Clientes Queue", true, true);
        idleDependientesQ = new Queue<>(this, "Idle Dependientes Queue", true, true);
        dependientesQ = new Queue<>(this, "Dependientes con comanda Queue", true, true);
        idleCocinerosQ = new Queue<>(this, "Idle Cocineros Queue", true, true);

        // create and insert the Dependientes and Cocineros into the idle queues
        for (int i = 0; i < NUM_DEPENDIENTES; i++) {
            Dependiente dependiente = new Dependiente(
                this, 
                "Dependiente", 
                true);
            idleDependientesQ.insert(dependiente);
        }

        for (int i = 0; i < NUM_COCINEROS; i++) {
            Cocinero cocinero = new Cocinero(
                this, 
                "Cocinero", 
                true);
            idleCocinerosQ.insert(cocinero);
        }
    }

    /**
     * This get methods return a sample of the random stream used to determine the
     * time needed to complete a task
     *
     * @return double a llegadaClienteTime sample
     */
    public double getLlegaClienteT() {
        return llegadaClienteT.sample();
    }

    public double getTomaComandaT() {
        return tomaComandaT.sample();
    }

    public double getPreparaComidaT() {
        return preparaComidaT.sample();
    }

    public double getPagaComidaT() {
        return pagaComidaT.sample();
    }

    public static void main(String[] args) {
        Experiment.setEpsilon(TimeUnit.SECONDS);
        Experiment.setReferenceUnit(TimeUnit.MINUTES);

        Experiment experiment = new Experiment("Burger Restaurant Experiment");

        BurgerRestModel burgerRestModel = new BurgerRestModel(
            null,
            "Burger Restaurant Model",
            true,
            false
        );

        burgerRestModel.connectToExperiment(experiment);
        experiment.tracePeriod(new TimeInstant(0), new TimeInstant(300));
        experiment.stop(new TimeInstant(1500));
        experiment.setShowProgressBar(false);
        experiment.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES)); // and debug output

        experiment.start();
        // --> now the simulation is running until it reaches its end criterion
        // <-- afterwards, the main thread returns here
        experiment.report();
        // stop all threads still alive and close all output files
        experiment.finish();
    }

    public void endWait() {
        endWait = presentTime();
    }

    public double getWaitTime() {
        if (startWait != null && endWait != null) 
            return TimeOperations.diff(startWait, endWait).getTimeAsDouble();
        else
            return Double.NaN;
    }
}
