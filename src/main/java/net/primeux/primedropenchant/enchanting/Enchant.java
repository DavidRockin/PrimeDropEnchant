package net.primeux.primedropenchant.enchanting;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.payment.iPayment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Available Enchantment
 */
public class Enchant
{

	/**
	 * Base enchantment
	 */
	@Getter
	@Setter
	private Enchantment enchantment = null;

	/**
	 * If this enchantment is allowed to be sold
	 */
	@Getter
	@Setter
	private boolean allowed = false;

	/**
	 * Base enchantment cost
	 */
	@Getter
	@Setter
	private float cost = 0;

	/**
	 * Is this cost constant per level or increments
	 */
	@Getter
	@Setter
	private boolean constantPrice = false;

	/**
	 * Payment processor method
	 */
	@Getter
	@Setter
	private iPayment payment;

	/**
	 * Determines if an itemstack has this enchantment
	 * @param itemStack
	 * @return
	 */
	public boolean itemIsEnchanted(ItemStack itemStack)
	{
		return this.getItemStackLevel(itemStack) > 0;
	}

	/**
	 * Retrieves this itemstack's enchantment level
	 * @param itemStack
	 * @return
	 */
	public int getItemStackLevel(ItemStack itemStack)
	{
		if (null == itemStack || null == enchantment || itemStack.getType().equals(Material.AIR)) {
			return 0;
		}
		return itemStack.getEnchantmentLevel(this.getEnchantment());
	}

	/**
	 * Removes this enchantment off this itemstack
	 * @param itemStack
	 */
	public void removeEnchantment(ItemStack itemStack)
	{
		itemStack.removeEnchantment(this.getEnchantment());
	}

	/**
	 * Determines if this can be sold
	 * @return
	 */
	public boolean canSell()
	{
		return this.enchantment != null && this.isAllowed() && this.cost > 0;
	}

	/**
	 * Calculate the cost of this enchantment to transfer
	 * @param itemStack
	 * @return
	 */
	public float getPrice(ItemStack itemStack)
	{
		return this.isConstantPrice() ? this.getCost() : this.getItemStackLevel(itemStack) * this.getCost();
	}

}