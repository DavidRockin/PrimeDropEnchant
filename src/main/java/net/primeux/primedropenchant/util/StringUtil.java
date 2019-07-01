package net.primeux.primedropenchant.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StringUtil
{

	/**
	 * Formats a message to use chat colors
	 * @param str message
	 * @return formatted message
	 */
	public static String textFormat(String str)
	{
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	/**
	 * Formats a list of strings to use chat colours
	 * @param list
	 * @return formatted list
	 */
	public static List<String> textFormat(List<String> list)
	{
		int i = 0;
		for (String line : list) {
			list.set(i, textFormat(line));
			++i;
		}
		return list;
	}

	/**
	 * Formats a string with parameters
	 *
	 * @param str string to format
	 * @param params keys to format
	 * @param values parameter values
	 * @return formatted string
	 */
	public static String formatString(String str, String[] params, String[] values)
	{
		if (params != null && values != null && params.length == values.length) {
			for (int i = 0; i < params.length; ++i) {
				str = str.replaceAll("(%" + params[i] + "%)", values[i]);
			}
		}
		return textFormat(str);
	}

	/**
	 * Formats a string with parameters
	 *
	 * @param text strings to format
	 * @param params keys to format
	 * @param values parameter values
	 * @return formatted strings
	 */
	public static List<String> formatString(List<String> text, String[] params, String[] values)
	{
		List<String> result = new ArrayList<>();
		for (String line : text) {
			result.add(formatString(line, params, values));
		}
		return result;
	}

	public static void sendMessage(CommandSender cs, String... str)
	{
		for (String s : str) {
			cs.sendMessage(textFormat(s));
		}
	}

}
