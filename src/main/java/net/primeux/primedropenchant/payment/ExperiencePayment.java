package net.primeux.primedropenchant.payment;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.util.Experience;
import org.bukkit.entity.Player;

/**
 * Experience Payment Processor
 */
public class ExperiencePayment implements iPayment
{

	@Getter
	private Plugin plugin;

	@Getter
	private final String id = "experience";

	public ExperiencePayment(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public String formatAmount(float amount)
	{
		return amount > 0 ? String.format("%,d XP", Math.round(amount)) : "FREE";
	}

	public boolean playerCanAfford(Player player, float amount)
	{
		return amount <= 0 || Experience.getExp(player) >= amount;
	}

	public void chargePlayer(Player player, float amount)
	{
		if (amount > 0) Experience.changeExp(player, - Math.round(amount));
	}

}
