package be.thatsatim.gravestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemSerializer {
    // Serialize an ItemStack to a String.
    public static String serializeInventory(ItemStack[] items) {
        StringBuilder serialized = new StringBuilder();

        for (ItemStack item : items) {
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
                return new ItemStack[0];
            }
            deserializeInventory(serializedInventory, tries + 1);
        }
        return null;
    }

}
