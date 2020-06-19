package me.Sam.ItemFilter;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FilterStorage {

	
	UUID player;
	Inventory filterinv;
	
	
	public FilterStorage(UUID player) {
		this.player = player;
		Inventory inv = Bukkit.createInventory(null, 54, Bukkit.getOfflinePlayer(player).getName() + "'s ItemFilter");
		this.filterinv = inv;
	}
	
	public UUID getPlayer() {
		return this.player;
	}
	
	public Inventory getFilterInv() {
		return this.filterinv;
	}
	
	public void openFilterInv() {
		Player p = Bukkit.getPlayer(this.player);
		p.openInventory(this.filterinv);
	}
	
	public void addItem(ItemStack item) {
		this.filterinv.addItem(item);
	}
}
