package net.primeux.primedropenchant.events;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.enchanting.Enchant;
import net.primeux.primedropenchant.gui.GuiHandler;
import net.primeux.primedropenchant.gui.TransferGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerListener implements Listener
{

	@Getter
	private Plugin plugin;

	public PlayerListener(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void playerInventoryClick(InventoryClickEvent event)
	{
		final ItemStack is = event.getCurrentItem();
		final ItemStack hand = event.getCursor();
		final Player p = (Player) event.getWhoClicked();

		if (event.isCancelled() || !event.getClick().equals(ClickType.RIGHT) || is == null || is.getType().equals(Material.AIR) ||
				hand == null || !hand.getType().equals(Material.BOOK)) {
			return;
		}

		if (!p.hasPermission("primedropenchant.transfer") && !p.isOp()) {
			p.sendMessage(getPlugin().getLocale().getLocale("noPermission"));
			return;
		}

		List<Enchant> enchants = getPlugin().getEnchantmentHandler().getItemEnchantments(is, true);
		if (enchants.size() == 0) {
			p.sendMessage(getPlugin().getLocale().getLocale("enchanting.source.no-enchants"));
			event.setCancelled(true);
			event.setResult(Event.Result.DENY);
			return;
		}

		hand.setAmount(hand.getAmount() - 1);

		final ItemStack container = new ItemStack(hand);
		container.setAmount(1);

		if (hand.getAmount() < 1) {
			hand.setType(Material.AIR);
		}

		if (hand != null && !hand.getType().equals(Material.AIR)) {
			p.getInventory().addItem(hand);
		}

		event.setCursor(new ItemStack(Material.AIR));
		event.setCancelled(true);
		event.setResult(Event.Result.DENY);

		GuiHandler.open(p, new TransferGui(plugin, is, container));
	}

}
