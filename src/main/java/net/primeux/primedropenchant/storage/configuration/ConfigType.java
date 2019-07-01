package net.primeux.primedropenchant.storage.configuration;

public enum ConfigType
{

	/**
	 * Plugin configuration
	 *
	 * General settings to modify plugin behaviour
	 */
	SETTINGS,

	/**
	 * Plugin data configuration file
	 *
	 * Files that store plugin generated information
	 */
	DATA,

	/**
	 * Resources file
	 */
	RESOURCE,

	/**
	 * Ugh this is for fucking migrations. Fuck my life.
	 */
	DATA_TEMP;

}
