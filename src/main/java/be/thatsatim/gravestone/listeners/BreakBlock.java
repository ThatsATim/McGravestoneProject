package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;

public class BreakBlock implements Listener {

    public BreakBlock(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (block.getType().equals(Material.STONE_STAIRS)) {
            try {
                GravestoneDatabase.deleteGravestone(location);
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Failed to delete the database entry! " + exception.getMessage());
            }
        }
    }
}
