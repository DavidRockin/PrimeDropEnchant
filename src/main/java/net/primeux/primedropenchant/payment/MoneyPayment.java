package net.primeux.primedropenchant.payment;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.enchanting.Enchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Vault Economy Payment Processors
 */
public class MoneyPayment implements iPayment
{

	@Getter
	private Plugin plugin;

	@Getter
	private final String id = "money";

	public MoneyPayment(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public boolean playerCanAfford(Player player, Enchant enchant, ItemStack itemStack)
	{
		return this.getPlugin().getEconomy().has(player, enchant.getPrice(itemStack));
	}

	public void chargePlayer(Player player, Enchant enchant, ItemStack itemStack)
	{
		this.getPlugin().getEconomy().withdrawPlayer(player, enchant.getPrice(itemStack));
	}

}
