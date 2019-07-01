package net.primeux.primedropenchant.gui;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGui
{

	@Getter
	private Plugin plugin;

	@Getter
	@Setter
	protected Player player = null;

	@Getter
	protected List<Player> active = new ArrayList();

	@Getter
	@Setter
	protected Inventory inventory = null;

	public BaseGui(Plugin plugin)
	{
		this.plugin = plugin;
	}

	protected void create(Player owner)
	{
		this.inventory = Bukkit.createInventory(
			owner,
			9,
			"Unknown Inventory"
		);
		this.render();
	}

	protected void render()
	{
		this.fill();
	}

	public void open(Player player)
	{
		this.player = player;
		if (this.inventory == null) {
			this.create(player);
		}
		this.active.add(player);
		player.openInventory(this.inventory);
	}

	public void click(InventoryClickEvent event)
	{
		Player p = (Player) event.getWhoClicked();
		p.sendMessage("You clicked on " + event.getCurrentItem().getType());
		event.setCancelled(true);
	}

	public void close(Player player)
	{
		this.active.remove(player);
	}

	public void destroy()
	{
		this.player = null;
		this.inventory = null;
		this.active.clear();
	}

	protected void fill()
	{
		for (int i = 0; i < this.inventory.getSize(); ++i) {
			this.inventory.setItem(i, this.getFiller());
		}
	}

	protected ItemStack getFiller()
	{
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		is.setDurability((short) 15);

		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RESET + "");
		is.setItemMeta(im);

		return is;
	}

	protected ItemStack add(int slot, Material material, String name, String[] lore)
	{
		return this.add(slot, new ItemStack(material, 1), name, lore);
	}

	protected ItemStack add(int slot, ItemStack itemStack, String name, String[] lore)
	{
		ItemMeta im = itemStack.getItemMeta();
		if (name != null && name.length() != 0) {
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		}
		List<String> l = im.hasLore() ? im.getLore() : new ArrayList();
		for (String line : lore) {
			l.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		im.setLore(l);
		itemStack.setItemMeta(im);

		if (slot >= 0) {
			this.getInventory().setItem(slot, itemStack);
		}

		return itemStack;
	}

}
