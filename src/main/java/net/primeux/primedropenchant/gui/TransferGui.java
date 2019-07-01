package net.primeux.primedropenchant.gui;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.enchanting.Enchant;
import net.primeux.primedropenchant.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferGui extends BaseGui
{

	@Getter
	private ItemStack source;

	@Getter
	private List<Enchant> enchants;

	@Getter
	private HashMap<Integer, TransferState> enchantmentIndex = new HashMap();

	public TransferGui(Plugin plugin, ItemStack source)
	{
		super(plugin);
		this.source = source;
	}

	@Override
	protected void create(Player owner)
	{
		this.inventory = Bukkit.createInventory(
			owner,
			9 * 5,
			ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "     KEEP     " + ChatColor.DARK_GRAY + "|" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "   TRANSFER"
		);
		this.render();
	}

	@Override
	public void open(Player player)
	{
		if (this.source == null || this.source.getType().equals(Material.AIR)) {
			return;
		}
		this.enchants = this.getPlugin().getEnchantmentHandler().getItemEnchantments(this.source, true);
		if (this.enchants.size() == 0) {
			player.sendMessage("This item has no enchantments");
			return;
		}

		int i = 0;
		for (Enchant e : this.enchants) {
			this.enchantmentIndex.put(i, new TransferState(
				e,
				false,
				e.getItemStackLevel(this.getSource())
			));
			++i;
		}

		super.open(player);
	}

	@Override
	protected void render()
	{
		ItemStack is;
		this.getInventory().clear();

		for (int y = 4; y < this.getInventory().getSize(); y += 9) {
			this.getInventory().setItem(y, this.getFiller());
		}

		int col1 = 0, col2 = 5;

		for (TransferState ts : this.enchantmentIndex.values()) {
			Enchant e = ts.getEnchant();

			is = ItemBuilder.init().use(new ItemStack(Material.ENCHANTED_BOOK))
					.setName(e.getEnchantment().getName())
					.getItemStack();
			is.addUnsafeEnchantment(e.getEnchantment(), ts.getLevel());
			this.inventory.setItem(ts.isTransfer() ? col2 : col1, is);

			if (ts.isTransfer()) {
				if (col2 >= 9) col2 = 5;
				++col2;
			} else {
				if (col1 >= 4) col1 = 0;
				++col1;
			}
		}

		for (int x = 0; x < 9; ++x) {
			ItemBuilder filler = ItemBuilder.init().use(this.getFiller());

			filler.setPlaceholders(new HashMap<String, String>() {{
				put("cost", "\\$ 69.99");
			}});

			if (x > 4) {
				filler.setDurability((short) 5);
				filler.setName("&a&lTRANSFER ENCHANTMENTS");
				filler.setLore(new ArrayList<String>() {{
					add("&eClick this to confirm this enchantment transfer.");
					add("&eYou will be charged &f%cost%");
				}});
			} else if (x < 4) {
				filler.setDurability((short) 14);
				filler.setName("&c&lCANCEL TRANSFER");
				filler.setLore(new ArrayList<String>() {{
					add("&cClick this to cancel the enchantment transfer.");
					add("&cYou will not be charged, and all your enchantments will remain.");
				}});
			}

			this.getInventory().setItem(this.getInventory().getSize() - 9 + x, filler.getItemStack());
		}
	}

	@Override
	public void click(InventoryClickEvent event)
	{
		event.setResult(Event.Result.DENY);
		event.setCancelled(true);

		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) ||
				!event.getClickedInventory().equals(this.getInventory())) {
			return;
		}

		// test for enchantment index
		if (this.enchantmentIndex.containsKey(event.getSlot())) {
			final TransferState ts = this.enchantmentIndex.get(event.getSlot());
			if (ts == null) {
				return;
			}

			ts.setTransfer(! ts.isTransfer());
		} else if (event.getCurrentItem().getType().equals(Material.GREEN_RECORD) && this.transfer()) {
			this.getPlayer().closeInventory();
			return;
		}

		this.render();
	}

	protected boolean transfer()
	{
		List<Enchant> toTransfer = new ArrayList();

		for (TransferState ts : this.enchantmentIndex.values()) {
			if (!ts.isTransfer()) {
				continue;
			}
			toTransfer.add(ts.getEnchant());
		}

		if (toTransfer.size() == 0) {
			this.getPlayer().sendMessage("No enchantments to transfer");
			return false;
		}
		ItemStack book = getPlugin().getEnchantmentHandler().swap(getSource(), getPlayer(), toTransfer);
		if (null == book) {
			getPlayer().sendMessage("You cannot afford this");
			return false;
		}

		getPlayer().getInventory().addItem(book);
		getPlayer().sendMessage("hereee");
		return true;
	}

	protected class TransferState
	{
		@Getter
		private Enchant enchant;

		@Getter
		@Setter
		private boolean transfer = false;

		@Getter
		@Setter
		private int level = 0;

		public TransferState(Enchant enchant,  boolean transfer, int level)
		{
			this.enchant = enchant;
			this.transfer = transfer;
			this.level = level;
		}
	}

}
