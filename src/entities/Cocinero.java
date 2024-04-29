package entities;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Cocinero extends Entity{

    /**
	 * Constructor of the truck entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this cook's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Cocinero(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

}
