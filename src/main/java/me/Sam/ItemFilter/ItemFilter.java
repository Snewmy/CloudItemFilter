package me.Sam.ItemFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemFilter extends JavaPlugin{

	public static ItemFilter instance;
	File datafile = new File(getDataFolder(), "data.yml");
	FileConfiguration data = YamlConfiguration.loadConfiguration(datafile);
	ArrayList<FilterStorage> filters = new ArrayList<FilterStorage>();
	public File messagesfile = new File(getDataFolder(), "messages.yml");
	public FileConfiguration messages = YamlConfiguration.loadConfiguration(messagesfile);
	
	public void onEnable() {
		instance = this;
		getServer().getLogger().info("Sams itemfilter plugin enabled");
		getServer().getPluginManager().registerEvents(new DropsListener(this), this);
		if(!data.isConfigurationSection("Data")) {
			data.createSection("Data");
			try {
				this.data.save(datafile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		loadData();
		autosaver();
		if(!messagesfile.exists()){
			saveResource("messages.yml", false);
		}
	}
	
	public void onDisable() {
		saveData();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("itemfilter")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(hasFilter(p)) {
					FilterStorage filter = getPlayerFilter(p.getUniqueId());
					filter.openFilterInv();
					p.sendMessage(Utils.chat(Msg.OPENINGFILTER.getMsg()));
				} else {
					FilterStorage filter = new FilterStorage(p.getUniqueId());
					this.filters.add(filter);
					filter.openFilterInv();
					p.sendMessage(Utils.chat(Msg.OPENINGFILTER.getMsg()));
				}
			}
		}
		return false;
	}

	public boolean hasFilter(Player p) {
		for(FilterStorage filter : this.filters) {
			if(filter.getPlayer().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public FilterStorage getPlayerFilter(UUID player) {
		for(FilterStorage filter : this.filters) {
			if(filter.getPlayer().equals(player)) {
				return filter;
			}
		}
		return null;
	}
	
	public void saveData() {
		ConfigurationSection data = this.data.getConfigurationSection("Data");
		for(FilterStorage filter : this.filters) {
			data.set(filter.getPlayer().toString() + ".inventory", filter.getFilterInv().getContents());
		}
		try {
			this.data.save(this.datafile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadData() {
		ConfigurationSection data = this.data.getConfigurationSection("Data");
		for(String uuid : data.getKeys(false)) {
			FilterStorage filter = new FilterStorage(UUID.fromString(uuid));
			ItemStack[] items = ((List<ItemStack>) data.get(uuid + ".inventory")).toArray(new ItemStack[0]);
			for(ItemStack item : items) {
				if(item != null) {
				filter.getFilterInv().addItem(item);
				}
			}
			this.filters.add(filter);
		}
	}

	public void autosaver() {
		final int time = (32 * 60) * 20;
		new BukkitRunnable() {
			@Override
			public void run() {
				saveData();
			}
		}.runTaskTimerAsynchronously(this, time, time);
	}
}
