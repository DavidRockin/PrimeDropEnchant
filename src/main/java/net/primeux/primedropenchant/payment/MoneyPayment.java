package net.primeux.primedropenchant.payment;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import org.bukkit.entity.Player;

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

	@Override
	public String formatAmount(float amount)
	{
		return amount > 0 ? String.format("\\$ %,.2f", amount) : "FREE";
	}

	public boolean playerCanAfford(Player player, float amount)
	{
		return amount <= 0 || this.getPlugin().getEconomy().has(player, amount);
	}

	public void chargePlayer(Player player, float amount)
	{
		if (amount > 0) this.getPlugin().getEconomy().withdrawPlayer(player, amount);
	}

}
