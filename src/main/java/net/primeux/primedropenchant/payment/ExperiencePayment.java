package net.primeux.primedropenchant.payment;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import net.primeux.primedropenchant.enchanting.Enchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

	public boolean playerCanAfford(Player player, Enchant enchant, ItemStack itemStack)
	{
		return player.getTotalExperience() >= enchant.getPrice(itemStack);
	}

	public void chargePlayer(Player player, Enchant enchant, ItemStack itemStack)
	{
		player.setTotalExperience(player.getTotalExperience() - Math.round(enchant.getPrice(itemStack)));
	}

}
