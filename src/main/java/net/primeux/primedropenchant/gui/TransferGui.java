package net.primeux.primedropenchant.gui;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.ConfigParser;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.enchanting.Enchant;
import net.primeux.primedropenchant.payment.Transaction;
import net.primeux.primedropenchant.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
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
	private ItemStack container;

	@Getter
	private List<Enchant> enchants;

	@Getter
	private HashMap<Integer, TransferState> enchantmentIndex = new HashMap();

	private boolean successfulTransfer = false;

	public TransferGui(Plugin plugin, ItemStack source, ItemStack container)
	{
		super(plugin);
		this.source = source;
		this.container = container;
	}

	@Override
	protected void create(Player owner)
	{
		this.inventory = Bukkit.createInventory(
			owner,
			9 * 5,
			getPlugin().getLocale().getLocale("gui.title")
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
			player.sendMessage(getPlugin().getLocale().getLocale("enchanting.source.no-enchants"));
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
	public void close(Player player)
	{
		super.close(player);
		if (!this.successfulTransfer) {
			this.getPlayer().sendMessage(getPlugin().getLocale().getLocale("enchanting.transfer.cancel"));

			if (this.container != null && !this.container.getType().equals(Material.AIR)) {
				this.getPlayer().getInventory().addItem(this.container);
			}
		}
	}

	@Override
	protected void render()
	{
		this.getInventory().clear();

		for (int y = 4; y < this.getInventory().getSize(); y += 9) {
			this.getInventory().setItem(y, this.getFiller());
		}

		int col1 = 0, col2 = 5, row1 = 0, row2 = 0;

		for (TransferState ts : this.enchantmentIndex.values()) {
			ts.setStot(ts.isTransfer() ? col2 + row2 : col1 + row1);
			final Enchant e = ts.getEnchant();
			final int level = e.getItemStackLevel(getSource());

			ItemBuilder ib = new ItemBuilder();
			ib.setPlaceholders(new HashMap<String, String>() {{
				put("name", e.getName());
				put("level", level + "");
				put("cost", e.getPayment().formatAmount(e.getPrice(level)));
			}});
			ib.deserialize(ConfigParser.getConfigSectionValue(getPlugin().getLocale().getConfig().getConfigurationSection("gui.item"), true));
			ib.addUnsafeEnchantment(e.getEnchantment(), ts.getLevel());
			ib.addFlags(ItemFlag.HIDE_ENCHANTS);

			ib.appendLore(getPlugin().getLocale().getLocaleList("gui.item." + (ts.isTransfer() ? "transfer" : "keep")));

			this.inventory.setItem(ts.getStot(), ib.getItemStack());

			if (ts.isTransfer()) {
				if (col2 >= 8) {
					col2 = 5;
					row2 += 9;
				} else ++col2;
			} else {
				if (col1 >= 3) {
					col1 = 0;
					row1 += 9;
				} else ++col1;
			}
		}

		Transaction t = this.createTransaction();

		for (int x = 0; x < 9; ++x) {
			ItemBuilder filler = ItemBuilder.init().use(this.getFiller());

			filler.setPlaceholders(new HashMap<String, String>() {{
				put("cost", t == null ? "No Enchantments" : t.formatCost());
			}});

			if (x > 4) {
				filler.setDurability((short) 5);
				filler.setName(getPlugin().getLocale().getLocale("gui.transfer.name"));
				filler.setLore(getPlugin().getLocale().getLocaleList("gui.transfer.lore"));

				if (t != null && !t.canAfford) {
					filler.appendLore(getPlugin().getLocale().getLocaleList("gui.cannotAfford"));
				}
			} else if (x < 4) {
				filler.setDurability((short) 14);
				filler.setName(getPlugin().getLocale().getLocale("gui.cancel.name"));
				filler.setLore(getPlugin().getLocale().getLocaleList("gui.cancel.lore"));
			}

			this.getInventory().setItem(this.getInventory().getSize() - 9 + x, filler.getItemStack());
		}
	}

	private Transaction createTransaction()
	{
		Map<Enchant, Integer> success = new HashMap<>();

		// build our available enchantments
		this.getEnchantmentIndex().forEach((integer, transferState) -> {
			if (transferState.isTransfer()) {
				success.put(transferState.getEnchant(), transferState.getLevel());
			}
		});

		if (success.isEmpty()) {
			return null;
		}

		return Transaction.begin(success, this.getPlayer());
	}

	@Override
	public void click(InventoryClickEvent event)
	{
		final ItemStack clicked = event.getCurrentItem();
		event.setResult(Event.Result.DENY);
		event.setCancelled(true);

		if (clicked == null || clicked.getType().equals(Material.AIR) ||
				!event.getClickedInventory().equals(this.getInventory())) {
			return;
		}

		// test for enchantment index
		final TransferState ts = this.getTransferState(event.getSlot());
		if (ts != null) {
			ts.setTransfer(! ts.isTransfer());
		} else if (clicked.getType().equals(this.getFiller().getType())) {
			if (clicked.getDurability() == (short) 5 && this.transfer()) {
				this.successfulTransfer = true;
			} else if (clicked.getDurability() == (short) 14) {
				this.successfulTransfer = false;
			} else {
				return;
			}

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
			this.getPlayer().sendMessage(getPlugin().getLocale().getLocale("enchanting.source.no-enchants"));
			return false;
		}
		Transaction book = getPlugin().getEnchantmentHandler().swap(getSource(), getPlayer(), toTransfer);
		if (null == book || book.getItemStack() == null) {
			getPlayer().sendMessage(getPlugin().getLocale().getLocale(
				"enchanting.source.unaffordable",
				new String[] {
					"cost"
				},
				new String[] {
					book != null ? book.formatCost() : "N/A"
				}
			));
			return false;
		}

		getPlayer().getInventory().addItem(book.getItemStack());
		getPlayer().sendMessage(getPlugin().getLocale().getLocale(
			"enchanting.transfer.success", new String[] {
				"cost"
			},
			new String[] {
				book.formatCost()
			}
		));

		return true;
	}

	private TransferState getTransferState(int slot)
	{
		for (TransferState ts : getEnchantmentIndex().values()) {
			if (ts.getStot() == slot) return ts;
		}
		return null;
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

		@Getter
		@Setter
		private int stot = 0;

		public TransferState(Enchant enchant,  boolean transfer, int level)
		{
			this.enchant = enchant;
			this.transfer = transfer;
			this.level = level;
		}
	}

}
