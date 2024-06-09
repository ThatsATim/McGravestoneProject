package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.utils.LocationChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
        if (playerInventory.isEmpty()) { return; }
        ItemStack cursorItem = player.getItemOnCursor();
        ItemStack[] gravestoneInventory = setLayout(playerInventory, cursorItem);
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
            GravestoneDatabase.addGravestone(location, player, gravestoneInventory);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to create the database entry! " + exception.getMessage());
        }

    }

    private ItemStack[] setLayout(PlayerInventory playerInventory, ItemStack cursorItem) {
        Inventory chestInventory = Bukkit.createInventory(null, 54);
        ItemStack[] items = playerInventory.getContents();

        // Set armor
        chestInventory.setItem(3, items[39]);
        chestInventory.setItem(5, items[38]);
        chestInventory.setItem(12, items[36]);
        chestInventory.setItem(14, items[37]);

        // Set offhand and cursor item
        chestInventory.setItem(4, items[40]);
        chestInventory.setItem(13, cursorItem);

        // Top Inventory bar
        chestInventory.setItem(18, items[9]);
        chestInventory.setItem(19, items[10]);
        chestInventory.setItem(20, items[11]);
        chestInventory.setItem(21, items[12]);
        chestInventory.setItem(22, items[13]);
        chestInventory.setItem(23, items[14]);
        chestInventory.setItem(24, items[15]);
        chestInventory.setItem(25, items[16]);
        chestInventory.setItem(26, items[17]);

        // 2nd Inventory bar
        chestInventory.setItem(27, items[18]);
        chestInventory.setItem(28, items[19]);
        chestInventory.setItem(29, items[20]);
        chestInventory.setItem(30, items[21]);
        chestInventory.setItem(31, items[22]);
        chestInventory.setItem(32, items[23]);
        chestInventory.setItem(33, items[24]);
        chestInventory.setItem(34, items[25]);
        chestInventory.setItem(35, items[26]);

        // 3rd Inventory bar
        chestInventory.setItem(36, items[27]);
        chestInventory.setItem(37, items[28]);
        chestInventory.setItem(38, items[29]);
        chestInventory.setItem(39, items[30]);
        chestInventory.setItem(40, items[31]);
        chestInventory.setItem(41, items[32]);
        chestInventory.setItem(42, items[33]);
        chestInventory.setItem(43, items[34]);
        chestInventory.setItem(44, items[35]);

        // Set hotbar
        chestInventory.setItem(45, items[0]);
        chestInventory.setItem(46, items[1]);
        chestInventory.setItem(47, items[2]);
        chestInventory.setItem(48, items[3]);
        chestInventory.setItem(49, items[4]);
        chestInventory.setItem(50, items[5]);
        chestInventory.setItem(51, items[6]);
        chestInventory.setItem(52, items[7]);
        chestInventory.setItem(53, items[8]);

        return chestInventory.getContents();
    }

}
