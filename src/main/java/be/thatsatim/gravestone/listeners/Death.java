package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import be.thatsatim.gravestone.utils.Location;

public class Death implements Listener {

    FileConfiguration config;

    public Death(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        config = plugin.getConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        // Get all Items of the player and prevent normal drops
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack cursor = player.getItemOnCursor();
        player.setItemOnCursor(null);
        event.getDrops().clear();
        org.bukkit.Location playerLocation = player.getLocation();

        // Check if the location is safe!
        Location.getSafe(playerLocation);

        // Spawn the chest
        Block block = playerLocation.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Inventory inventory = chest.getBlockInventory();

        // Put the items in the chest
        inventory.addItem(cursor);
        for (ItemStack item : playerInventory) {
            if (item == null) {continue;}
            inventory.addItem(item);
        }
    }
}
