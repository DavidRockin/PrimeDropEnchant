package net.primeux.primedropenchant.storage.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{

	/**
	 * Instance of the plugin
	 */
	@Getter
	public JavaPlugin plugin;

	/**
	 * List of loaded configuration files
	 */
	@Getter
	private List<Config> configs = new ArrayList<>();

	public ConfigHandler(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void loadConfig(String name, String file, ConfigType type)
	{
		this.configs.add(new Config(this, name, file, type));
	}

	public void loadConfig(String name, File file, ConfigType type)
	{
		this.configs.add(new Config(this, name, file, type));
	}

	/**
	 * Attempt to load our available configuration files
	 */
	public void loadConfigs()
	{
		for (Config config : this.configs) {
			config.load();
		}
	}

	/**
	 * Saves all active configuration file
	 *
	 * @todo Have an option to only save specific files
	 */
	public void saveConfigs()
	{
		for (Config config : this.configs) {
			config.save();
		}
	}

	/**
	 * Fetches the config file
	 *
	 * @param name configuration file name
	 * @return the loaded config file
	 */
	public YamlConfiguration getConfig(String name)
	{
		Config config = this.getConfigByName(name);
		return (null == config ? null : config.getConfig());
	}

	/**
	 * Saves the config file
	 *
	 * @param name configuration file name
	 * @return the loaded config file
	 */
	public void saveConfig(String name)
	{
		Config config = this.getConfigByName(name);
		if (null != config) config.save();
	}

	/**
	 * Finds a configuration file by its name
	 *
	 * @param name
	 * @return Config file instance or null
	 */
	public Config getConfigByName(String name)
	{
		for (Config config : this.configs) {
			if (config.name.equalsIgnoreCase(name)) {
				return config;
			}
		}
		System.out.println("[!!!] Config file " + name + " NOT found!!!");
		return null;
	}

}
