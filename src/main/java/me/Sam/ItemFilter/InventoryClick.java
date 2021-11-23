package me.Sam.ItemFilter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    ItemFilter main;

    public InventoryClick(ItemFilter main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (main.filters.containsKey(player.getUniqueId())) {
            FilterStorage filter = main.filters.get(player.getUniqueId());
            if (event.getView().getTopInventory().equals(filter.getFilterInv())) {
                if (event.getCurrentItem() == null) {
                    return;
                }
                ItemStack clickedItem = event.getCurrentItem();
                if (!event.getClickedInventory().equals(filter.getFilterInv())) {
                    if (event.getView().getTopInventory().containsAtLeast(clickedItem, 1)) {
                        player.sendMessage(Utils.chat("&cYou can not add more than one of the same item to the filter!"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
