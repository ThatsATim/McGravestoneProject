package be.thatsatim.gravestone.utils;

import be.thatsatim.gravestone.Gravestone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Base64;

public class Inventory {

    private final Gravestone plugin;

    public Inventory(Gravestone plugin) {
        this.plugin = plugin;
    }

    private void saveInventory(PlayerInventory playerInventory, Player player) {
        FileConfiguration config = this.plugin.getConfig();

        // Serialize inventory to Base64
        String serializedInventory = serializeInventory(playerInventory);

        // Save to config
        config.set(getStaffInventoryKey(player), serializedInventory);
        this.plugin.saveConfig();

        playerInventory.clear();
    }

    // Get the inventory for use in the gravestone
    private void getInventory(Player player) {
        FileConfiguration config = this.plugin.getConfig();

        if (config.contains(getStaffInventoryKey(player))) {
            // Read from config
            String serializedInventory = config.getString(getStaffInventoryKey(player));

            // Deserialize and restore inventory
            deserialize(serializedInventory, 0);
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "&4GRAVESTONE: NO SAVED INVENTORY FOUND!");
    }

    // Serialize an inventory to a String.
    private String serializeInventory(PlayerInventory playerInventory) {
        StringBuilder serialized = new StringBuilder();
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null) {
                // Serialize each item to Base64
                String serializedItem = Base64.getEncoder().encodeToString(item.serializeAsBytes());
                serialized.append(serializedItem).append(";");
                continue;
            }
            serialized.append("null;");
        }
        return serialized.toString();
    }

    // Deserialize a String back into an inventory
    private ItemStack[] deserialize(String serializedInventory, int tries) {
        try {
            String[] serializedItems = serializedInventory.split(";");
            ItemStack[] inventoryContents = new ItemStack[serializedItems.length];

            for (int i = 0; i < serializedItems.length; i++) {
                String serializedItem = serializedItems[i];
                if (!serializedItem.equals("null")) {
                    // Deserialize each item from Base64
                    byte[] serializedData = Base64.getDecoder().decode(serializedItem);
                    inventoryContents[i] = ItemStack.deserializeBytes(serializedData);
                }
            }

            // Send the Inventory back
            return inventoryContents;

        } catch (Exception e) {
            if (tries > 10) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "&4GRAVESTONE: ERROR WHILE DE-SERIALISING INVENTORY!");
                return null;
            }
            deserialize(serializedInventory, tries + 1);
        }
        return null;
    }
}
