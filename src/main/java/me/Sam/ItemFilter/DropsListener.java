package me.Sam.ItemFilter;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class DropsListener implements Listener{
	
	ItemFilter main;
	
	public DropsListener(ItemFilter main) {
		this.main = main;
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("SamItemFilter.Filter")) {
			if (!main.hasFilter(p)) {
				Random random = new Random();
				int chance = random.nextInt(1000) + 1;
				if (chance <= 2) {
					p.sendMessage(Utils.chat(Msg.DIDYOUKNOW.getMsg()));
				}
				return;
			}
			ItemStack item = e.getItem().getItemStack();
			if (main.filters.get(p.getUniqueId()).getFilterInv().contains(item.getType(), 1)) {
				e.setCancelled(true);
			}
		}
	}

}
