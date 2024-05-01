package entities;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Cliente extends Entity{
    /**
	 * Constructor of the truck entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this client's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Cliente(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

}
