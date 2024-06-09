package be.thatsatim.gravestone.listeners;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class GuiClose implements Listener {

    public GuiClose(Gravestone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent event) {

        String containerTitle = event.getView().getTitle();

        if (!containerTitle.contains("'s gravestone")) {
            return;
        }

        ItemStack[] items = event.getInventory().getContents();
        PlayerInventory playerInventory = gravestoneToPlayerInventory(items);
        // TODO NEED TO GET THE LOCATION

        try {
            // Item 13 is where the cursorItem is!
            GravestoneDatabase.updateGravestone(playerInventory, items[13], location);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to update the database entry! " + exception.getMessage());
        }

    }


    private PlayerInventory gravestoneToPlayerInventory(ItemStack[] items) {
        PlayerInventory playerInventory = new PlayerInventory() {
            @Override
            public @Nullable ItemStack @NotNull [] getArmorContents() {
                return new ItemStack[0];
            }

            @Override
            public @Nullable ItemStack @NotNull [] getExtraContents() {
                return new ItemStack[0];
            }

            @Override
            public @Nullable ItemStack getHelmet() {
                return null;
            }

            @Override
            public @Nullable ItemStack getChestplate() {
                return null;
            }

            @Override
            public @Nullable ItemStack getLeggings() {
                return null;
            }

            @Override
            public @Nullable ItemStack getBoots() {
                return null;
            }

            @Override
            public void setItem(int i, @Nullable ItemStack itemStack) {

            }

            @Override
            public void setItem(@NotNull EquipmentSlot equipmentSlot, @Nullable ItemStack itemStack) {

            }

            @Override
            public @NotNull ItemStack getItem(@NotNull EquipmentSlot equipmentSlot) {
                return null;
            }

            @Override
            public void setArmorContents(@Nullable ItemStack[] itemStacks) {

            }

            @Override
            public void setExtraContents(@Nullable ItemStack[] itemStacks) {

            }

            @Override
            public void setHelmet(@Nullable ItemStack itemStack) {

            }

            @Override
            public void setChestplate(@Nullable ItemStack itemStack) {

            }

            @Override
            public void setLeggings(@Nullable ItemStack itemStack) {

            }

            @Override
            public void setBoots(@Nullable ItemStack itemStack) {

            }

            @Override
            public @NotNull ItemStack getItemInMainHand() {
                return null;
            }

            @Override
            public void setItemInMainHand(@Nullable ItemStack itemStack) {

            }

            @Override
            public @NotNull ItemStack getItemInOffHand() {
                return null;
            }

            @Override
            public void setItemInOffHand(@Nullable ItemStack itemStack) {

            }

            @Override
            public @NotNull ItemStack getItemInHand() {
                return null;
            }

            @Override
            public void setItemInHand(@Nullable ItemStack itemStack) {

            }

            @Override
            public int getHeldItemSlot() {
                return 0;
            }

            @Override
            public void setHeldItemSlot(int i) {

            }

            @Override
            public @Nullable HumanEntity getHolder() {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getMaxStackSize() {
                return 0;
            }

            @Override
            public void setMaxStackSize(int i) {

            }

            @Override
            public @Nullable ItemStack getItem(int i) {
                return null;
            }

            @Override
            public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
                return null;
            }

            @Override
            public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
                return null;
            }

            @Override
            public @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
                return null;
            }

            @Override
            public @Nullable ItemStack @NotNull [] getContents() {
                return new ItemStack[0];
            }

            @Override
            public void setContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {

            }

            @Override
            public @Nullable ItemStack @NotNull [] getStorageContents() {
                return new ItemStack[0];
            }

            @Override
            public void setStorageContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {

            }

            @Override
            public boolean contains(@NotNull Material material) throws IllegalArgumentException {
                return false;
            }

            @Override
            public boolean contains(@Nullable ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean contains(@NotNull Material material, int i) throws IllegalArgumentException {
                return false;
            }

            @Override
            public boolean contains(@Nullable ItemStack itemStack, int i) {
                return false;
            }

            @Override
            public boolean containsAtLeast(@Nullable ItemStack itemStack, int i) {
                return false;
            }

            @Override
            public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
                return null;
            }

            @Override
            public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack) {
                return null;
            }

            @Override
            public int first(@NotNull Material material) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public int first(@NotNull ItemStack itemStack) {
                return 0;
            }

            @Override
            public int firstEmpty() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void remove(@NotNull Material material) throws IllegalArgumentException {

            }

            @Override
            public void remove(@NotNull ItemStack itemStack) {

            }

            @Override
            public void clear(int i) {

            }

            @Override
            public void clear() {

            }

            @Override
            public int close() {
                return 0;
            }

            @Override
            public @NotNull List<HumanEntity> getViewers() {
                return List.of();
            }

            @Override
            public @NotNull InventoryType getType() {
                return null;
            }

            @Override
            public @Nullable InventoryHolder getHolder(boolean b) {
                return null;
            }

            @Override
            public @NotNull ListIterator<ItemStack> iterator() {
                return null;
            }

            @Override
            public @NotNull ListIterator<ItemStack> iterator(int i) {
                return null;
            }

            @Override
            public @Nullable Location getLocation() {
                return null;
            }
        };

        // Set the armor
        playerInventory.setHelmet(items[3]);
        playerInventory.setChestplate(items[5]);
        playerInventory.setLeggings(items[14]);
        playerInventory.setBoots(items[12]);

        // Set offhand
        playerInventory.setItemInOffHand(items[4]);

        // Top inventory row
        playerInventory.setItem(9, items[18]);
        playerInventory.setItem(10, items[19]);
        playerInventory.setItem(11, items[20]);
        playerInventory.setItem(12, items[21]);
        playerInventory.setItem(13, items[22]);
        playerInventory.setItem(14, items[23]);
        playerInventory.setItem(15, items[24]);
        playerInventory.setItem(16, items[25]);
        playerInventory.setItem(17, items[26]);

        // Second inventory row
        playerInventory.setItem(18, items[27]);
        playerInventory.setItem(19, items[28]);
        playerInventory.setItem(20, items[29]);
        playerInventory.setItem(21, items[30]);
        playerInventory.setItem(22, items[31]);
        playerInventory.setItem(23, items[32]);
        playerInventory.setItem(24, items[33]);
        playerInventory.setItem(25, items[34]);
        playerInventory.setItem(26, items[35]);

        // Third inventory row
        playerInventory.setItem(27, items[36]);
        playerInventory.setItem(28, items[37]);
        playerInventory.setItem(29, items[38]);
        playerInventory.setItem(30, items[39]);
        playerInventory.setItem(31, items[40]);
        playerInventory.setItem(32, items[41]);
        playerInventory.setItem(33, items[42]);
        playerInventory.setItem(34, items[43]);
        playerInventory.setItem(35, items[44]);

        // Hotbar
        playerInventory.setItem(0, items[45]);
        playerInventory.setItem(1, items[46]);
        playerInventory.setItem(2, items[47]);
        playerInventory.setItem(3, items[48]);
        playerInventory.setItem(4, items[49]);
        playerInventory.setItem(5, items[50]);
        playerInventory.setItem(6, items[51]);
        playerInventory.setItem(7, items[52]);
        playerInventory.setItem(8, items[53]);

        return playerInventory;

    }



}
