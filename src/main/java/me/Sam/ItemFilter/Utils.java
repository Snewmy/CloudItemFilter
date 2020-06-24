package me.Sam.ItemFilter;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static String prefix = ItemFilter.instance.messages.getString("Prefix");
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
