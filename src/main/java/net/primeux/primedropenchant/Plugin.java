package net.primeux.primedropenchant;

import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.primeux.primedropenchant.enchanting.EnchantmentHandler;
import net.primeux.primedropenchant.events.PlayerListener;
import net.primeux.primedropenchant.gui.GuiHandler;
import net.primeux.primedropenchant.payment.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class Plugin extends JavaPlugin
{

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
	@Setter
	public boolean allowGuiTransfer = true;

	@Getter
	private FileConfiguration config;

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
	}

	@Override
	public void onDisable()
	{
	}

	public void setup()
	{
		this.hooks();

		File c = new File(this.getDataFolder(), "/config.yml");
		if (!c.exists()) {
			this.saveResource("config.yml", false);
		}

		this.config = YamlConfiguration.loadConfiguration(c);

		this.enchantmentHandler = new EnchantmentHandler(this);
		this.configParser.load();
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
