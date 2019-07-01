package net.primeux.primedropenchant;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin
{

	@Getter
	private Economy economy;

	@Override
	public void onEnable()
	{
	}

	@Override
	public void onDisable()
	{
	}

}
