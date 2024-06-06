package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.utils.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class RightClick implements Listener {

    public RightClick(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = event.getClickedBlock();
        Location location = block.getLocation();
        Player player = event.getPlayer();
        ItemStack[] inventory;

        if (!block.getType().equals(Material.STONE_STAIRS)) {
            return;
        }

        try {
            String[] values = GravestoneDatabase.getGravestone(location);
            String DatabaseInventory = values[1];
            inventory = ItemSerializer.deserializeInventory(DatabaseInventory, 0);
            player.getInventory().setContents(inventory);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to get the database entry! " + exception.getMessage());
        }


    }

}
