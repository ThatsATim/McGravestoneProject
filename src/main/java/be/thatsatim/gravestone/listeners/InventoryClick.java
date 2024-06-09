package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    public InventoryClick(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().contains("'s gravestone")) { return; }

        Inventory clickedInventory = event.getClickedInventory();
        Inventory playerInventory = event.getWhoClicked().getInventory();

        if (event.getClick().isShiftClick()) {
            if (playerInventory == clickedInventory) {
                event.setCancelled(true);
            }
        }


        if (playerInventory != clickedInventory) {
            ItemStack cursorItem = event.getCursor();
            if (cursorItem.getType() != Material.AIR) {
                event.setCancelled(true);
            }
        }

    }
}
