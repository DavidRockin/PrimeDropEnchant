package net.primeux.primedropenchant.enchanting;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.payment.iPayment;
import org.apache.commons.lang.StringUtils;
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

	public String getName()
	{
		return StringUtils.capitalize(this.getEnchantment().getName().toLowerCase().replace('_', ' '));
	}

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
		return this.enchantment != null && this.isAllowed();
	}

	/**
	 * Calculate the cost of this enchantment to transfer
	 * @param level
	 * @return
	 */
	public float getPrice(int level)
	{
		return this.isConstantPrice() ? this.getCost() : level * this.getCost();
	}

	/**
	 * Enchants an itemstack to this enchantment
	 * @param itemStack
	 * @param level
	 */
	public void enchantItemstack(ItemStack itemStack, int level)
	{
		itemStack.addUnsafeEnchantment(this.getEnchantment(), level);
	}

}
