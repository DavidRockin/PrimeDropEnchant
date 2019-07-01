package net.primeux.primedropenchant.payment;

import org.bukkit.entity.Player;

/**
 * Payment Processor Interface
 */
public interface iPayment
{

	/**
	 * The payment processor ID
	 * @return
	 */
	String getId();

	/**
	 * Formats a given cost to a user friendly label
	 * @param amount
	 * @return
	 */
	String formatAmount(float amount);

	/**
	 * Determines if the player can afford to transfer this particular enchantment
	 * @param player
	 * @param amount
	 * @return
	 */
	boolean playerCanAfford(Player player, float amount);

	/**
	 * Charges the player to transfer
	 * @param player
	 * @param amount
	 */
	void chargePlayer(Player player, float amount);

}
