package net.primeux.primedropenchant.storage.configuration;
import lombok.Getter;
import net.primeux.primedropenchant.util.StringUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Config
{

	/**
	 * Config file handler instance
	 */
	@Getter
	public ConfigHandler handler;

	/**
	 * Name of the config file
	 */
	@Getter
	public String name;

	/**
	 * Config file path
	 */
	@Getter
	public String file;

	/**
	 * Config file type
	 */
	@Getter
	public ConfigType type;

	/**
	 * File resource
	 */
	@Getter
	private File fileResource;

	/**
	 * YAML config
	 */
	@Getter
	private YamlConfiguration fileConfig;

	/**
	 * Constructor of new config file
	 *
	 * @param handler
	 * @param name
	 * @param file
	 * @param type
	 */
	public Config(ConfigHandler handler, String name, String file, ConfigType type)
	{
		this.handler = handler;
		this.name = name;
		this.file = file;
		this.type = type;
		this.load();
	}

	/**
	 * Constructor of new config file
	 *
	 * @param handler
	 * @param name
	 * @param file
	 * @param type
	 */
	public Config(ConfigHandler handler, String name, File file, ConfigType type)
	{
		this.handler = handler;
		this.name = name;
		this.fileResource = file;
		this.type = type;
		this.load();
	}

	/**
	 * Load this configuration file
	 */
	public void load()
	{
		// load resource files from JAR
		if (this.getType().equals(ConfigType.RESOURCE)) {
			this.fileConfig = YamlConfiguration.loadConfiguration(
				new InputStreamReader(
					this.getHandler().getPlugin().getResource(this.file)
				)
			);
			return;
		}

		if (this.fileResource == null) {
			this.fileResource = new File(this.handler.plugin.getDataFolder() + "/" + this.file);
		} else {
			this.file = this.fileResource.getAbsolutePath();
		}

		// migration legacy support?
		// basically I am lazy and dont feel like dealing with yaml serialization
		// so instead we'll replace the serialization object identification so we
		// can manually parse the shitty yaml file !!!!!! YAY
		if (this.getType().equals(ConfigType.DATA_TEMP)) {
			StringBuilder str = new StringBuilder();

			this.fileConfig = new YamlConfiguration();

			try {
				BufferedReader br = new BufferedReader(new FileReader(this.fileResource));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.contains("==:")) {
						line = line.replaceFirst("==:", "_lazyHack:");
					}
					str.append(line).append('\n');
				}
				this.fileConfig.loadFromString(str.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		// if the file doesn't exist, save the default
		if (!this.fileResource.exists()) {
			try {
				this.handler.plugin.saveResource(this.file, false);

			// if the config is for data, and this is not found packaged in the jar
			// we'll have to manually create this file prior to loading it
			} catch (IllegalArgumentException ex) {
				if (this.getType().equals(ConfigType.DATA)) {
					try {
						this.fileResource.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// load and parse YAML file
		this.fileConfig = YamlConfiguration.loadConfiguration(this.fileResource);
	}

	/**
	 * Saves this configuration file
	 */
	public void save()
	{
		try {
			// do not save this file if it's not data file
			if (this.type != ConfigType.DATA)
				return;
			this.fileConfig.save(this.fileResource);
		} catch (IOException e) {
			System.out.println("[!] Cannot save " + name);
			e.printStackTrace();
		}
	}

	/**
	 * Get YAML configuration
	 * @return
	 */
	public YamlConfiguration getConfig()
	{
		return this.fileConfig;
	}

	/**
	 * Merges this configuration file with another
	 *
	 * @param target to merge into
	 */
	public void merge(YamlConfiguration target)
	{
		try {
			for (Entry<String, Object> kek : this.fileConfig.getValues(true).entrySet()) {
				target.set(kek.getKey(), kek.getValue());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Formats a string with parameters
	 *
	 * @param str string to format
	 * @param params keys to format
	 * @param values parameter values
	 * @return formatted string
	 */
	public String formatString(String str, String[] params, String[] values)
	{
		return StringUtil.formatString(str, params, values);
	}

	/**
	 * Retrieves a formatted string from the YAML config
	 * @param key the key to fetch
	 * @return Formatted text
	 */
	public String getLocale(String key)
	{
		return getLocale(key, new String[0], new String[0]);
	}

	/**
	 * Retrieves a formatted locale string from the YAML config.
	 * This supports custom parameters to replace data from the string.
	 *
	 * @param key yaml string to fetch
	 * @param params array of keys to replace
	 * @param values array of values corresponding to the values
	 * @return formatted locale string
	 */
	public String getLocale(String key, String[] params, String[] values)
	{
		return this.formatString(
			getConfig().getString(key,"MISSING LOCALIZATION KEY " + key),
			params,
			values
		);
	}

	/**
	 * Retrieves a formatted locale string list from the YAML config.
	 * @param key yaml key
	 * @return formatted string list
	 */
	public List<String> getLocaleList(String key)
	{
		return getLocaleList(key, new String[0], new String[0]);
	}

	/**
	 * Retrieves a formatted locale string list from the YAML config.
	 * This supports custom parameters to replace data from the strings.
	 *
	 * @param key yaml key
	 * @return formatted string list
	 */
	public List<String> getLocaleList(String key, String[] params, String[] values)
	{
		List<String> res = new ArrayList<>();
		for (String line : getConfig().getStringList(key)) {
			res.add(
				formatString(line, params, values)
			);
		}
		return res;
	}

}
