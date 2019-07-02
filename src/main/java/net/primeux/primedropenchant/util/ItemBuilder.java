package net.primeux.primedropenchant.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemBuilder
{

	@Getter
	protected ItemStack itemStack;

	@Getter
	@Setter
	protected HashMap<String, String> placeholders = new HashMap<>();

	public static ItemBuilder init()
	{
		return new ItemBuilder();
	}

	public ItemBuilder use(ItemStack itemStack)
	{
		this.itemStack = itemStack;
		return this;
	}

	public ItemBuilder use(Material material)
	{
		return this.use(material, (short) 1);
	}

	public ItemBuilder use(Material material, short amount)
	{
		this.itemStack = new ItemStack(material, amount);
		return this;
	}

	public ItemBuilder setType(Material type)
	{
		this.itemStack.setType(type);
		return this;
	}

	public ItemBuilder setName(String name)
	{
		ItemMeta im = this.itemStack.getItemMeta();
		im.setDisplayName(this.format(name));
		this.itemStack.setItemMeta(im);
		return this;
	}

	public ItemBuilder setAmount(int amount)
	{
		this.itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder setDurability(short durability)
	{
		this.itemStack.setDurability(durability);
		return this;
	}

	public ItemBuilder setLore(List<String> lore)
	{
		List<String> newLore = new ArrayList<>();
		for (String line : lore) {
			newLore.add(this.format(line));
		}
		ItemMeta im = this.itemStack.getItemMeta();
		im.setLore(newLore);
		this.itemStack.setItemMeta(im);

		return this;
	}

	public ItemBuilder appendLore(List<String> lore)
	{
		List<String> newLore = new ArrayList<>(this.getLore());
		newLore.addAll(lore);
		return this.setLore(newLore);
	}

	public List<String> getLore()
	{
		return this.itemStack.hasItemMeta() && this.itemStack.getItemMeta().hasLore()
				? this.itemStack.getItemMeta().getLore()
				: new ArrayList<>()
		;
	}

	public String[] loreRegexp(String regexp)
	{
		if (this.getLore().size() == 0) {
			return null;
		}

		Pattern p = Pattern.compile(regexp);
		Matcher m;

		for (String line : this.getLore()) {
			try {
				m = p.matcher(ChatColor.stripColor(line).trim());
				if (!m.find()) {
					continue;
				}
				String[] groups = new String[m.groupCount() + 1];
				for (int i = 0; i <= m.groupCount(); ++i) {
					groups[i] = m.group(i);
				}
				return groups;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

	public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level)
	{
		this.itemStack.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilder addFlags(ItemFlag... flags)
	{
		ItemMeta im = this.getItemStack().getItemMeta();
		im.addItemFlags(flags);
		this.itemStack.setItemMeta(im);
		return this;
	}

	public ItemBuilder deserialize(Map<String, Object> map)
	{
		ItemStack is = this.itemStack;

		if (map.containsKey("type")) {
			is = this.itemStack = this.parseType(map.get("type"));
		}

		if (is == null) {
			return this;
		}

		if (map.containsKey("name")) {
			this.setName(map.get("name") + "");
		}

		if (map.containsKey("amount")) {
			this.setAmount(Integer.parseInt(map.get("amount") + ""));
		}

		if (map.containsKey("lore")) {
			this.setLore((List<String>) map.get("lore"));
		}

		this.itemStack = is;

		return this;
	}

	public final ItemStack copy()
	{
		return new ItemStack(this.getItemStack());
	}

	protected final String format(String str)
	{
		for (Map.Entry<String, String> params : this.getPlaceholders().entrySet()) {
			str = str.replaceAll("(%" + params.getKey() + "%)", params.getValue());
		}
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	protected ItemStack parseType(Object type)
	{
		ItemStack is = null;
		String[] parts = type.toString().split(":");

		if (parts[0].length() != 0) {
			if (NumberUtil.isInteger(parts[0])) {
				is = new ItemStack(Material.getMaterial(Integer.parseInt(parts[0])));
			} else {
				is = new ItemStack(
					Material.valueOf(
						parts[0].trim().toUpperCase().replace(' ', '_')
					)
				);
			}

			// TODO database of items
		}

		if (is != null && parts.length > 1) {
			is.setDurability(Short.parseShort(parts[1]));
		}

		return is;
	}

}
