package net.primeux.primedropenchant.payment;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment Handler System
 */
public class PaymentHandler
{

	@Getter(AccessLevel.PROTECTED)
	private List<iPayment> processors = new ArrayList();

	/**
	 * Registers a payment processor
	 * @param processor
	 */
	public void add(iPayment processor)
	{
		this.processors.add(processor);
	}

	/**
	 * Removes a processor by ID
	 * @param id
	 */
	public void remove(String id)
	{
		this.remove(this.get(id));
	}

	/**
	 * Removes processor instance
	 * @param processor
	 */
	public void remove(iPayment processor)
	{
		this.processors.remove(processor);
	}

	/**
	 * Retrieves processor by ID
	 * @param id
	 * @return
	 */
	public iPayment get(String id)
	{
		for (iPayment p : getProcessors()) {
			if (id.trim().equalsIgnoreCase(p.getId())) return p;
		}
		return null;
	}

}
