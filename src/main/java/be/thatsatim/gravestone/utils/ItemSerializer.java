package be.thatsatim.gravestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Base64;

public class ItemSerializer {

    public static String serializeItem(ItemStack item) {
        StringBuilder serialized = new StringBuilder();

        String serializedItem = Base64.getEncoder().encodeToString(item.serializeAsBytes());
        serialized.append(serializedItem).append(";");
        return serialized.toString();
    }

    public static ItemStack deserializeItem(String serializedItem, int tries) {
        try {
            ItemStack item;
            if (!serializedItem.contains("null")) {
                // Deserialize the item from Base64
                byte[] serializedData = Base64.getDecoder().decode(serializedItem);
                item = ItemStack.deserializeBytes(serializedData);
                return item;
            }
        } catch (Exception exception) {
            if (tries > 10) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "GRAVESTONE: ERROR WHILE DE-SERIALISING ITEM!");
                return null;
            }
            deserializeInventory(serializedItem, tries + 1);
        }
        return null;
    }


    // Serialize an ItemStack to a String.
    public static String serializeInventory(PlayerInventory items) {
        StringBuilder serialized = new StringBuilder();

        for (ItemStack item : items.getContents()) {
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

    // Deserialize a String back into an ItemStack
    public static ItemStack[] deserializeInventory(String serializedInventory, int tries) {
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
            return inventoryContents;

        } catch (Exception exception) {
            if (tries > 10) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "GRAVESTONE: ERROR WHILE DE-SERIALISING INVENTORY!");
                return null;
            }
            deserializeInventory(serializedInventory, tries + 1);
        }
        return null;
    }

}
