package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import be.thatsatim.gravestone.utils.LocationChecker;

import java.sql.SQLException;

public class Death implements Listener {

    public Death(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        // Get all Items of the player and prevent normal drops
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack cursorItem = player.getItemOnCursor();
        player.setItemOnCursor(null);
        event.getDrops().clear();
        Location playerLocation = player.getLocation();

        // Check if the location is safe!
        LocationChecker.getSafe(playerLocation);

        // Spawn the tombstone
        Block block = playerLocation.getBlock();
        block.setType(Material.STONE_STAIRS);

        Location location = block.getLocation();

        try {
            GravestoneDatabase.addGravestone(location, player, playerInventory, cursorItem);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to create the database entry! " + exception.getMessage());
        }

    }
}
