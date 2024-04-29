package entities;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Dependiente extends Entity{

	private Cliente clienteAsignado;

	/**
	 * Constructor of the truck entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this cashier's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Dependiente(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	public Cliente getClienteAsignado() {
		return clienteAsignado;
	}

	public void setClienteAsignado(Cliente clientAsignado) {
		this.clienteAsignado = clientAsignado;
	}

	public void releaseCliente() {
		this.clienteAsignado = null;
	}
}
