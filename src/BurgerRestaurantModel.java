
import java.util.concurrent.TimeUnit;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import entities.Cliente;
import entities.Cocinero;
import entities.Dependiente;

public class BurgerRestaurantModel extends Model{

    protected static final int NUM_DEPENDIENTES = 3;
    protected static final int NUM_COCINEROS = 1;

    private ContDistExponential llegadaClienteT;
    private ContDistExponential tomaComandaT;
    private ContDistExponential preparaComidaT;

    protected Queue<Cliente> clientesQ;
    protected Queue<Dependiente> idleDependientesQ;
    protected Queue<Dependiente> dependientesQ;
    protected Queue<Cocinero> idleCocinerosQ;


    /**
	 * Creates a new BurgerRestaurantModel model via calling
	 * the constructor of the superclass.
	 *
	 * @param owner the model this model is part of (set to <tt>null</tt> when there is no such model)
	 * @param modelName this model's name
	 * @param showInReport flag to indicate if this model shall produce output to the report file
	 * @param showInTrace flag to indicate if this model shall produce output to the trace file
	 */
    public BurgerRestaurantModel(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }
    

    /**
    * Returns a description of the model to be used in the report.
    * @return model description as a string
    */
    public String description() {
        return "This model describes a queueing system located at a "+
                    "container terminal. Trucks will arrive and "+
                    "require the loading of a container. A van carrier (VC) is "+
                    "on duty and will head off to find the required container "+
                    "in the storage. It will then load the container onto the "+
                    "truck. Afterwards, the truck leaves the terminal. "+
                    "In case the VC is busy, the truck waits "+
                    "for its turn on the parking-lot. "+
                    "If the VC is idle, it waits on its own parking spot for the "+
                    "truck to come.";
    }
  
     /**
      * Activates dynamic model components (events).
      *
      * This method is used to place all events or processes on the
      * internal event list of the simulator which are necessary to start
      * the simulation.
      *
      * In this case, the truck generator event will have to be
      * created and scheduled for the start time of the simulation.
      */
    public void doInitialSchedules() { 
    
    }
  
     /**
      * Initialises static model components like distributions and queues.
      */
    public void init() {
        /*
         * Initialize the random number generators for the inter-arrival times and the Queues
         * Parameters:
         * Model                = the model this distribution is associated with
         * String               = this distribution's name
         * double               = the mean inter-arrival time
         * boolean              = show in report?
         * boolean              = show in trace?
         */
        llegadaClienteT = new ContDistExponential(this, "Llegada Cliente Time", 5.0, true, false);
        tomaComandaT = new ContDistExponential(this, "Toma Comanda Time", 4.0, true, false);
        preparaComidaT = new ContDistExponential(this, "Prepara Comida Time", 7.0, true, false);

        // necessary because an inter-arrival time can not be negative, but
		// a sample of an exponential distribution can...
        llegadaClienteT.setNonNegative(true);
        tomaComandaT.setNonNegative(true);
        preparaComidaT.setNonNegative(true);

        clientesQ = new Queue<>(this, "Clientes Queue", true, true);
        idleDependientesQ = new Queue<>(this, "Idle Dependientes Queue", true, true);
        dependientesQ = new Queue<>(this, "Dependientes Queue", true, true);
        idleCocinerosQ = new Queue<>(this, "Idle Cocineros Queue", true, true);

        // create and insert the Dependientes and Cocineros into the idle queues
        for(int i = 0; i < NUM_DEPENDIENTES; i++){
            Dependiente dependiente = new Dependiente(this, "Dependiente", true);
            idleDependientesQ.insert(dependiente);
        }

        for(int i = 0; i < NUM_COCINEROS; i++){
            Cocinero cocinero = new Cocinero(this, "Cocinero", true);
            idleCocinerosQ.insert(cocinero);
        }
    }

    /**
	 * This get methods return a sample of the random stream used to determine the
	 * time needed to complete a task
	 *
	 * @return double a llegadaClienteTime sample
	 */
    public double getLlegadaClienteT(){
        return llegadaClienteT.sample();
    }
    public double getTomaComandaT(){
        return tomaComandaT.sample();
    }
    public double getPreparaComidaT(){
        return preparaComidaT.sample();
    }

    public static void main(String[] args){
        BurgerRestaurantModel model = new BurgerRestaurantModel(null, "Burger Restaurant Model", true, true);
        Experiment exp = new Experiment("Burger Restaurant Experiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
        model.connectToExperiment(exp);
        
        // set experiment parameters
		exp.setShowProgressBar(true);  // display a progress bar (or not)
		exp.stop(new TimeInstant(1500, TimeUnit.MINUTES));   // set end of simulation at 1500 minutes
		exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES));  // set the period of the trace
		exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));   // and debug output
			// ATTENTION!
			// Don't use too long periods. Otherwise a huge HTML page will
			// be created which crashes Netscape :-)

		// start the experiment at simulation time 0.0
		exp.start();

		// --> now the simulation is running until it reaches its end criterion
		// ...
		// ...
		// <-- afterwards, the main thread returns here

		// generate the report (and other output files)
		exp.report();

		// stop all threads still alive and close all output files
		exp.finish();
    }
}
