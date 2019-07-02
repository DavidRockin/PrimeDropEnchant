package net.primeux.primedropenchant.commands;

import lombok.Getter;
import net.primeux.primedropenchant.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginManagement implements CommandExecutor
{

	/**
	 * Plugin instance
	 */
	@Getter
	private Plugin plugin;

	/**
	 * Base permission prefix
	 */
	@Getter
	private String permission = "primedropenchant.admin";

	public PluginManagement(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(
			CommandSender commandSender,
			Command command,
			String s,
			String[] args
	) {
		if (args.length == 0) {
			this.cmdInfo(commandSender, args);
			return true;
		}

		if (commandSender.hasPermission(this.getPermission() + "." + args[0].toLowerCase().trim())) {
			switch (args[0].trim().toLowerCase()) {
				case "reload":
					this.cmdReload(commandSender, args);
					return true;

			}
		}

		this.cmdInfo(commandSender, args);
		return true;
	}

	/**
	 * General plugin information
	 *
	 * @param cs command executor
	 * @param args command arguments
	 */
	private void cmdInfo(CommandSender cs, String[] args)
	{
		cs.sendMessage(ChatColor.GRAY + "==========[ " + ChatColor.LIGHT_PURPLE + getPlugin().getDescription().getName() + " " + ChatColor.GRAY + "]==========");
		cs.sendMessage(ChatColor.LIGHT_PURPLE + "Version: " + ChatColor.GRAY + getPlugin().getDescription().getVersion());
		cs.sendMessage(ChatColor.LIGHT_PURPLE + "Website: " + ChatColor.GRAY + getPlugin().getDescription().getWebsite());
		cs.sendMessage(ChatColor.LIGHT_PURPLE + "Built By: " + ChatColor.GRAY + getPlugin().getDescription().getAuthors().toString());
	}

	/**
	 * Reloads plugin configuration files and resources
	 *
	 * @param cs command executor
	 * @param args command arguments
	 */
	private void cmdReload(CommandSender cs, String[] args)
	{
		getPlugin().setup();
		cs.sendMessage(ChatColor.YELLOW + "Reloading configuration files");
	}

}
