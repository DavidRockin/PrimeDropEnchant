package net.primeux.primedropenchant.enchanting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Enchantment Handler literally fucking handles all available
 * enchantments and their generic information
 */
public class EnchantmentHandler
{

	/**
	 * Loaded enchants
	 */
	private List<Enchant> enchantments = new ArrayList();

	/**
	 * Registers an enchant
	 * @param enchant
	 */
	public void addEnchantment(Enchant enchant)
	{
		this.enchantments.add(enchant);
	}

	/**
	 * Removes it from our registrar
	 * @param enchant
	 */
	public void removeEnchantment(Enchant enchant)
	{
		this.enchantments.remove(enchant);
	}

	/**
	 * Retrieves all available enchantments on an itemstack
	 * @param itemStack
	 * @return
	 */
	public List<Enchant> getItemEnchantments(ItemStack itemStack, boolean canSell)
	{
		if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
			return new ArrayList<Enchant>();
		}
		List<Enchant> results = new ArrayList<Enchant>();
		for (Enchant e : this.enchantments) {
			if (canSell && !e.canSell()) continue;
			if (e.itemIsEnchanted(itemStack)) {
				results.add(e);
			}
		}
		return results;
	}

}
