package me.Sam.ItemFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Filter;

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
	Map<UUID, FilterStorage> filters = new HashMap<UUID, FilterStorage>();
	public File messagesfile = new File(getDataFolder(), "messages.yml");
	public FileConfiguration messages = YamlConfiguration.loadConfiguration(messagesfile);
	
	public void onEnable() {
		instance = this;
		getServer().getLogger().info("Sams itemfilter plugin enabled");
		getServer().getPluginManager().registerEvents(new DropsListener(this), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
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
				if (p.hasPermission("SamItemFilter.Filter")) {
					if (hasFilter(p)) {
						FilterStorage filter = filters.get(p.getUniqueId());
						if (p.hasPermission("SamItemFilter.Size6")) {
							filter.setSize(54);
						} else if (p.hasPermission("SamItemFilter.Size5")) {
							filter.setSize(45);
						} else if (p.hasPermission("SamItemFilter.Size4")) {
							filter.setSize(36);
						} else if (p.hasPermission("SamItemFilter.Size3")) {
							filter.setSize(27);
						} else if (p.hasPermission("SamItemFilter.Size2")) {
							filter.setSize(18);
						} else if (p.hasPermission("SamItemFilter.Size1")) {
							filter.setSize(9);
						}
						filter.openFilterInv();
						p.sendMessage(Utils.chat(Msg.OPENINGFILTER.getMsg()));
					} else {
						FilterStorage filter = new FilterStorage(p.getUniqueId(), 9);
						if (p.hasPermission("SamItemFilter.Size6")) {
							filter.setSize(54);
						} else if (p.hasPermission("SamItemFilter.Size5")) {
							filter.setSize(45);
						} else if (p.hasPermission("SamItemFilter.Size4")) {
							filter.setSize(36);
						} else if (p.hasPermission("SamItemFilter.Size3")) {
							filter.setSize(27);
						} else if (p.hasPermission("SamItemFilter.Size2")) {
							filter.setSize(18);
						} else if (p.hasPermission("SamItemFilter.Size1")) {
							filter.setSize(9);
						}
						this.filters.put(p.getUniqueId(), filter);
						filter.openFilterInv();
						p.sendMessage(Utils.chat(Msg.OPENINGFILTER.getMsg()));
					}
				} else {
					p.sendMessage(Utils.chat("&cYou do not have permission!"));
				}
			}
		}
		return false;
	}

	public boolean hasFilter(Player p) {
		return filters.containsKey(p.getUniqueId());
	}
	
	public void saveData() {
		ConfigurationSection data = this.data.getConfigurationSection("Data");
		for(Map.Entry<UUID, FilterStorage> entry : this.filters.entrySet()) {
			FilterStorage filter = entry.getValue();
			data.set(filter.getPlayer().toString() + ".inventory", filter.getFilterInv().getContents());
			data.set(filter.getPlayer().toString() + ".size", filter.getSize());
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
			int size = data.getInt(uuid + ".size");
			FilterStorage filter = new FilterStorage(UUID.fromString(uuid), size);
			ItemStack[] items = ((List<ItemStack>) data.get(uuid + ".inventory")).toArray(new ItemStack[0]);
			for(ItemStack item : items) {
				if(item != null) {
				filter.getFilterInv().addItem(item);
				}
			}
			this.filters.put(UUID.fromString(uuid), filter);
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
