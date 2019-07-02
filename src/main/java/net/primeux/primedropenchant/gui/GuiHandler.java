package net.primeux.primedropenchant.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuiHandler
		implements Listener
{

	@Getter
	private static ConcurrentHashMap<Player, BaseGui> playerGuis = new ConcurrentHashMap<Player, BaseGui>();

	public static void open(Player player, BaseGui gui)
	{
		// I spent like 2 fucking hours on this fucking piece of shit plugin trying to get this to work
		// every time you'd open the GUI, the inventory would FUCKING CLOSE right after being opened.
		// I swore I fucked up GC, or destruction, or fucking basic shit like assigning and tracking our
		// shitty guis. But no, instead, SPIGOT HAS TO BE FUCKING AUTISTIC and want to throw the interact
		// event TWICE because of the retarded off hand thing. Now, since the plugin is built against 1.8
		// i don't have the stupid fucking option to test.. SOOOO we're gonna be lazy, cause fuck this game,
		// and we'll just prevent opening more inventories until close() runs :D
		// Fuck my god damn life ugh.
		if (isOpen(player)) {
			if (getGui(player).equals(gui)) return;
			playerGuis.remove(player);
		}

		playerGuis.put(player, gui);
		gui.open(player);
	}

	public static void close(Player player)
	{
		BaseGui gui = playerGuis.get(player);
		gui.close(player);
		if (gui.active.isEmpty()) {
			gui.destroy();
		}
		playerGuis.remove(player);
	}

	public static boolean isOpen(Player player)
	{
		return playerGuis.containsKey(player);
	}

	public static BaseGui getGui(Player player)
	{
		return playerGuis.get(player);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		final Player p = (Player) event.getWhoClicked();
		if (isOpen(p)) {
			getGui(p).click(event);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();

		if (!isOpen((Player) event.getPlayer()) || !getGui((Player) event.getPlayer()).getInventory().equals(event.getInventory())) {
			return;
		}
		close((Player) event.getPlayer());
	}

	public static void closeAll(BaseGui target)
	{
		for (Map.Entry<Player, BaseGui> gui : playerGuis.entrySet()) {
			if (gui.getValue().getInventory().equals(target.getInventory()) || gui.getValue().equals(target)) {
				gui.getKey().closeInventory();
			}
		}
	}

	public static void closeAll()
	{
		for (Player p : getPlayerGuis().keySet()) {
			p.closeInventory();
		}
		playerGuis.clear();
	}

}
