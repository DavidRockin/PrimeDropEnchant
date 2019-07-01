package net.primeux.primedropenchant.payment;

import net.primeux.primedropenchant.enchanting.Enchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	 * Determines if the player can afford to transfer this particular enchantment
	 * @param player
	 * @param enchant
	 * @param itemStack
	 * @return
	 */
	boolean playerCanAfford(Player player, Enchant enchant, ItemStack itemStack);

	/**
	 * Charges the player to transfer
	 * @param player
	 * @param enchant
	 * @param itemStack
	 */
	void chargePlayer(Player player, Enchant enchant, ItemStack itemStack);

}
