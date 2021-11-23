package me.Sam.ItemFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FilterStorage {

	
	UUID player;
	Inventory filterinv;
	int size;
	
	
	public FilterStorage(UUID player, int size) {
		this.player = player;
		Inventory inv = Bukkit.createInventory(null, size, Bukkit.getOfflinePlayer(player).getName() + "'s ItemFilter");
		this.filterinv = inv;
		this.size = size;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		ItemStack[] tempArray = this.filterinv.getContents();
		//LinkedList<ItemStack> tempList = new LinkedList<ItemStack>(Arrays.asList(this.filterinv.getContents()));
		this.filterinv = Bukkit.createInventory(null, size, Bukkit.getOfflinePlayer(player).getName() + "'s ItemFilter");
		this.filterinv.setContents(tempArray);
	}
}
