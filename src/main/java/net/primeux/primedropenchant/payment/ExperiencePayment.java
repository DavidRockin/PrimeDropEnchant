package net.primeux.primedropenchant.payment;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
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

	public boolean playerCanAfford(Player player, float amount)
	{
		return player.getTotalExperience() >= amount;
	}

	public void chargePlayer(Player player, float amount)
	{
		player.giveExp(- Math.round(amount));
	}

}
