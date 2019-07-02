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
		return String.format("%,d XP", Math.round(amount));
	}

	public boolean playerCanAfford(Player player, float amount)
	{
		return Experience.getExp(player) >= amount;
	}

	public void chargePlayer(Player player, float amount)
	{
		Experience.changeExp(player, - Math.round(amount));
	}

}
