package net.primeux.primedropenchant;

import lombok.Getter;
import net.primeux.primedropenchant.enchanting.Enchant;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

/**
 * Handles Parsing of Configuration File
 */
public class ConfigParser
{

	@Getter
	private Plugin plugin;

	public ConfigParser(Plugin plugin)
	{
		this.plugin = plugin;
	}

	protected FileConfiguration getConfig()
	{
		return this.getPlugin().getConfig();
	}

	/**
	 * Parses the configuration file
	 */
	public void load()
	{
		this.parseEnchantments();
	}

	/**
	 * Parses our enchantments
	 */
	private void parseEnchantments()
	{
		Enchant enchant;
		for (Map<?, ?> e : getConfig().getMapList("enchantments")) {
			try {
				enchant = new Enchant();

				enchant.setEnchantment(Enchantment.getByName(e.get("id").toString().trim().toUpperCase()));
				enchant.setAllowed((Boolean) e.get("allow"));

				if (e.containsKey("cost")) {
					Map<?, ?> c = (Map<?, ?>) e.get("cost");
					enchant.setPayment(getPlugin().getPaymentHandler().get(c.get("currency") + ""));
					enchant.setCost(Float.parseFloat(c.get("amount") + ""));
					enchant.setConstantPrice(!(Boolean) c.get("perLevel"));
				}

				this.getPlugin().getEnchantmentHandler().addEnchantment(enchant);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
