package net.primeux.primedropenchant.enchanting;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.util.ItemBuilder;
import org.bukkit.Material;
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
	 * Creates an enchantment book
	 * @param player
	 * @param original
	 * @param enchantments
	 * @return
	 */
	public ItemStack createBook(Player player, ItemStack original, List<Enchant> enchantments)
	{
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
