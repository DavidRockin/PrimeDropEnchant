package net.primeux.primedropenchant;

import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.primeux.primedropenchant.commands.PluginManagement;
import net.primeux.primedropenchant.enchanting.EnchantmentHandler;
import net.primeux.primedropenchant.events.PlayerListener;
import net.primeux.primedropenchant.gui.GuiHandler;
import net.primeux.primedropenchant.payment.*;
import net.primeux.primedropenchant.storage.configuration.Config;
import net.primeux.primedropenchant.storage.configuration.ConfigHandler;
import net.primeux.primedropenchant.storage.configuration.ConfigType;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Plugin extends JavaPlugin
{

	@Getter
	private Metrics metrics;

	@Getter
	private Economy economy = null;

	@Getter
	private EnchantmentHandler enchantmentHandler;

	@Getter
	private PaymentHandler paymentHandler = new PaymentHandler();

	private ConfigParser configParser = new ConfigParser(this);

	@Getter
	@Setter
	private Map<String, Object> enchantmentContainers;

	@Getter
	private ConfigHandler configHandler;

	@Override
	public void onLoad()
	{
		this.paymentHandler.add(new MoneyPayment(this));
		this.paymentHandler.add(new ExperiencePayment(this));
	}

	@Override
	public void onEnable()
	{
		this.setup();
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		this.getServer().getPluginManager().registerEvents(new GuiHandler(), this);
		this.getCommand("primedropenchant").setExecutor(new PluginManagement(this));
	}

	@Override
	public void onDisable()
	{
		GuiHandler.closeAll();
		this.enchantmentHandler = null;
		this.configHandler = null;
		this.economy = null;
		this.enchantmentContainers = null;
	}

	@Override
	public FileConfiguration getConfig()
	{
		return this.getConfigHandler().getConfig("config");
	}

	public Config getLocale()
	{
		return this.getConfigHandler().getConfigByName("locale");
	}

	public void setup()
	{
		this.hooks();

		GuiHandler.closeAll();

		this.configHandler = new ConfigHandler(this);
		this.configHandler.loadConfig("config", "config.yml", ConfigType.SETTINGS);
		this.configHandler.loadConfigs();

		this.configHandler.loadConfig("locale", "locale/" + this.getConfig().getString("locale", "en") + ".yml", ConfigType.RESOURCE);
		this.configHandler.getConfigByName("locale").load();

		this.enchantmentHandler = new EnchantmentHandler(this);
		this.configParser.load();

		// opt-in metrics, but default to enabled ;)
		if (this.getConfig().getBoolean("metrics", true)) {
			this.metrics = new Metrics(this);
		}
	}

	protected void hooks()
	{
		this.economy = null;

		try {
			RegisteredServiceProvider<Economy> provider = getServer().
					getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (provider != null) {
				this.economy = provider.getProvider();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
