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
import org.bukkit.inventory.*;

import java.sql.SQLException;

public class RightClick implements Listener {

    public RightClick(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) { return; }

        Block block = event.getClickedBlock();
        Location location = block.getLocation();
        Player player = event.getPlayer();
        ItemStack[] inventory;

        if (!block.getType().equals(Material.STONE_STAIRS)) { return; }

        try {
            String[] values = GravestoneDatabase.getGravestone(location);

            String DatabaseInventory = values[1];
            inventory = ItemSerializer.deserializeInventory(DatabaseInventory, 0);

            String cursorItemString = values[2];
            ItemStack cursorItem = ItemSerializer.deserializeItem(cursorItemString, 0);

            openGui(player, inventory, cursorItem);

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to get the database entry! " + exception.getMessage());
        }

    }

    private void openGui(Player player, ItemStack[] items, ItemStack cursorItem) {
        String name = player.getDisplayName() + "'s gravestone";
        Inventory testInventory = Bukkit.createInventory(player, 54, name);

        // Set armor
        testInventory.setItem(3, items[39]);
        testInventory.setItem(5, items[38]);
        testInventory.setItem(12, items[36]);
        testInventory.setItem(14, items[37]);

        // Set offhand and cursor item
        testInventory.setItem(4, items[40]);
        testInventory.setItem(13, cursorItem);

        // Top Inventory bar
        testInventory.setItem(18, items[9]);
        testInventory.setItem(19, items[10]);
        testInventory.setItem(20, items[11]);
        testInventory.setItem(21, items[12]);
        testInventory.setItem(22, items[13]);
        testInventory.setItem(23, items[14]);
        testInventory.setItem(24, items[15]);
        testInventory.setItem(25, items[16]);
        testInventory.setItem(26, items[17]);

        // 2nd Inventory bar
        testInventory.setItem(27, items[18]);
        testInventory.setItem(28, items[19]);
        testInventory.setItem(29, items[20]);
        testInventory.setItem(30, items[21]);
        testInventory.setItem(31, items[22]);
        testInventory.setItem(32, items[23]);
        testInventory.setItem(33, items[24]);
        testInventory.setItem(34, items[25]);
        testInventory.setItem(35, items[26]);

        // 3rd Inventory bar
        testInventory.setItem(36, items[27]);
        testInventory.setItem(37, items[28]);
        testInventory.setItem(38, items[29]);
        testInventory.setItem(39, items[30]);
        testInventory.setItem(40, items[31]);
        testInventory.setItem(41, items[32]);
        testInventory.setItem(42, items[33]);
        testInventory.setItem(43, items[34]);
        testInventory.setItem(44, items[35]);

        // Set hotbar
        testInventory.setItem(45, items[0]);
        testInventory.setItem(46, items[1]);
        testInventory.setItem(47, items[2]);
        testInventory.setItem(48, items[3]);
        testInventory.setItem(49, items[4]);
        testInventory.setItem(50, items[5]);
        testInventory.setItem(51, items[6]);
        testInventory.setItem(52, items[7]);
        testInventory.setItem(53, items[8]);

        player.openInventory(testInventory);
    }

}
