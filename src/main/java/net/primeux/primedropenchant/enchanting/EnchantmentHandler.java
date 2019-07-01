package net.primeux.primedropenchant.enchanting;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Enchantment Handler literally fucking handles all available
 * enchantments and their generic information
 */
public class EnchantmentHandler
{

	@Getter
	private Plugin plugin;

	/**
	 * Loaded enchants
	 */
	private List<Enchant> enchantments = new ArrayList();

	public EnchantmentHandler(Plugin plugin)
	{
		this.plugin = plugin;
	}

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

	/**
	 * Swaps all applicable enchantments on a player's itemstack
	 * Returns null if unable to afford any enchantment swap, otherwise
	 * itemstack of the transferred enchantments
	 *
	 * @param itemStack
	 * @param player
	 * @param enchantments
	 * @return
	 */
	public ItemStack swap(ItemStack itemStack, Player player, List<Enchant> enchantments)
	{
		List<Enchant> success = new ArrayList();

		for (Enchant e : enchantments) {
			if (e.canSell() && e.getPayment().playerCanAfford(player, e, itemStack)) {
				e.getPayment().chargePlayer(player, e, itemStack);
				e.removeEnchantment(itemStack);
				success.add(e);
			}
		}

		return this.createBook(player, itemStack, success);
	}

	/**
	 * Creates an enchantment book
	 * @param player
	 * @param original
	 * @param enchantments
	 * @return
	 */
	public ItemStack createBook(Player player, ItemStack original, List<Enchant> enchantments)
	{
		if (enchantments == null || enchantments.size() == 0) {
			return null;
		}

		ItemBuilder ib = ItemBuilder.init().deserialize(this.getPlugin().getEnchantmentContainers());
		ib.setPlaceholders(new HashMap<String, String>() {{
			put("player", player.getName());
		}});

		ItemStack book = ib.getItemStack();
		enchantments.forEach(e -> e.enchantItemstack(book, e.getItemStackLevel(original)));
		return book;
	}

	/**
	 * Clears all enchantments
	 */
	public void clear()
	{
		this.enchantments.clear();
	}

}
