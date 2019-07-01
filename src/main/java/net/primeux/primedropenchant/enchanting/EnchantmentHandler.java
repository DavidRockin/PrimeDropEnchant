package net.primeux.primedropenchant.enchanting;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.payment.Transaction;
import net.primeux.primedropenchant.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
		List<Enchant> results = new ArrayList<>();
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
	public Transaction swap(ItemStack itemStack, Player player, List<Enchant> enchantments)
	{
		Map<Enchant, Integer> success = new HashMap<>();

		// build our available enchantments
		enchantments.forEach(e -> {
			if (e.canSell()) success.put(e, e.getItemStackLevel(itemStack));
		});

		if (success.isEmpty()) {
			return null;
		}

		Transaction t = new Transaction(success, player);
		if (!t.canAfford) {
			return null;
		}

		t.charge();

		// build the book
		final ItemStack book  = this.createBook(player, itemStack, success.keySet());
		t.setItemStack(book);

		// removes all enchantments from our itemstack
		for (Enchant e : success.keySet()) {
			e.removeEnchantment(itemStack);
		}

		return t;
	}

	/**
	 * Creates an enchantment book
	 * @param player
	 * @param original
	 * @param enchantments
	 * @return
	 */
	public ItemStack createBook(Player player, ItemStack original, Collection<Enchant> enchantments)
	{
		if (enchantments == null || enchantments.size() == 0) {
			return null;
		}

		ItemBuilder ib = ItemBuilder.init();
		ib.setPlaceholders(new HashMap<String, String>() {{
			put("player", player.getName());
		}});

		ItemStack book = ib.deserialize(this.getPlugin().getEnchantmentContainers()).getItemStack();
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
