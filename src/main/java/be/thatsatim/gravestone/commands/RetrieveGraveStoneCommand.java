package be.thatsatim.gravestone.commands;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.utils.ItemSerializer;
import be.thatsatim.gravestone.utils.Memory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class RetrieveGraveStoneCommand implements Command<CommandSourceStack> {
    // Import Economy from Vault
    private static Economy economy = null;
    private static Gravestone plugin = null;

    public static void register(Gravestone pl, Commands commands) {
        // Get the economy from Vault
        economy = pl.getEconomy();
        plugin = pl;

        LiteralArgumentBuilder<CommandSourceStack> commandBuilder =
            Commands.literal("retrieve-gravestone")
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("world", StringArgumentType.string())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("x", StringArgumentType.string())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("y", StringArgumentType.string())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("z", StringArgumentType.string())
                .executes(new RetrieveGraveStoneCommand())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("confirm", StringArgumentType.string())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("price", StringArgumentType.string())
                .executes(new RetrieveGraveStoneCommand())))))));

        commands.register(
            commandBuilder.build(),
            "Retrieve a gravestone",
            List.of("rg")
        );
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {
            CommandSourceStack source = context.getSource();

            Entity entity = source.getExecutor();

            if (!(entity instanceof Player)) {
                return 0;
            }

            Player player = (Player) entity;
            Location location = entity.getLocation();
            startAnimation(location);

            if (true) {
                return 0;
            }

            String world = context.getArgument("world", String.class);
            String x = context.getArgument("x", String.class);
            String y = context.getArgument("y", String.class);
            String z = context.getArgument("z", String.class);

            boolean confirm = false;
            try {
                String confirmArg = context.getArgument("confirm", String.class);
                confirm = confirmArg.equals("confirm");
            } catch (IllegalArgumentException e) { }

            // Retrieve gravestone at x, y, z
            Location gravestoneLocation = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));

            String[] values = null;
            try {
                values = GravestoneDatabase.getGravestone(gravestoneLocation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (values == null) {
                player.sendMessage("No gravestone found at " + x + " " + y + " " + z);
                return 0;
            }

            // Calculate the distance between the player and the gravestone
            double distance = location.distance(gravestoneLocation);

            // Get cost of retrieving the gravestone every 100 blocks
            int cost100xBlocks = 3;

            // Calculate the cost of retrieving the gravestone
            int cost = (int) Math.ceil(distance / 100) * cost100xBlocks;

            // Check if the player has enough money
            if (economy != null && economy.getBalance(player) < cost || economy == null) {
                player.sendMessage("You do not have enough money to retrieve this gravestone. Cost: " + cost);
                return 0;
            }

            // Check if the player has confirmed the retrieval
            if (!confirm) {
                Component message = Component.text()
                    .append(Component.text("Retrieving the gravestone will cost you ", NamedTextColor.GRAY))
                    .append(Component.text(cost, NamedTextColor.DARK_GRAY, TextDecoration.BOLD))
                    .append(Component.text(" tokens. ", NamedTextColor.GRAY))
                    .append(Component.newline())
                    .append(Component.text("Click ", NamedTextColor.GRAY))
                    .append(Component.text("here", NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.runCommand("/retrieve-gravestone " + world + " " + x + " " + y + " " + z + " confirm " + cost)))
                    .append(Component.text(" to CONFIRM the retrieve of your items.", NamedTextColor.GRAY))
                    .build();

                player.sendMessage(message);
                return 0;
            }

            int price = 0;
            try {
                String priceArg = context.getArgument("price", String.class);
                price = Integer.parseInt(priceArg);
            } catch (IllegalArgumentException e) { }

            if (price != cost) {
                player.sendMessage("The price you entered is incorrect. Please try again.");
                return 0;
            }

            // UUID and player related logic
            String graveOwnerString = values[0];
            Player graveOwnerPlayer = Bukkit.getPlayer(UUID.fromString(graveOwnerString));

            // Permission logic
            if (player != graveOwnerPlayer && !player.hasPermission("MapleGrave.Staff")) {
                player.sendMessage("You do not have permission to retrieve this gravestone.");
                return 0;
            }

            // Inventory logic
            String DatabaseInventory = values[1];
            ItemStack[] inventory = ItemSerializer.deserializeInventory(DatabaseInventory, 0);

            // Drop the inventory
            if (inventory != null) {
                for (ItemStack item : inventory) {
                    if (item != null) {
                        location.getWorld().dropItemNaturally(location, item);
                    }
                }
            }

            try {
                GravestoneDatabase.deleteGravestone(gravestoneLocation);
                Memory.deleteGravestone(gravestoneLocation);
                // Remove the gravestone block
                gravestoneLocation.getBlock().setType(Material.AIR);
                economy.withdrawPlayer(player, cost);
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Failed to delete the database entry! " + exception.getMessage());
            }

            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void startAnimation(Location playerLocation) {
        Bukkit.broadcastMessage("Starting animation");

        // Spawn a creeper 100 blocks in front of the player and 10 blocks above the player location in the direction the player is facing
        Location spawnLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(100));
        spawnLocation.setY(playerLocation.getY() + 10);

        Creeper creeper = (Creeper) playerLocation.getWorld().spawnEntity(spawnLocation, EntityType.CREEPER);
        creeper.setCustomName("Flying Creeper");
        creeper.setCustomNameVisible(true);
        creeper.setAI(false); // Disable AI to prevent it from moving on its own

        // Variables for animation task
        int totalTicks = 20; // Animation duration in ticks (1 second = 20 ticks)
        final int[] ticks = {0};

        // Schedule the main animation task
        BukkitTask animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks[0] >= totalTicks) { // After animation duration, stop animation and explode creeper
                    cancel();
                    explodeCreeper(creeper);
                    return;
                }

                double t = (double) ticks[0] / totalTicks; // Normalize ticks to [0, 1]
                double ease = t * t * (3 - 2 * t); // Smoothstep easing function

                Location currentLocation = creeper.getLocation();
                Location targetLocation = playerLocation.clone().add(0, 0, 0); // Hover 1 block above the player

                // Interpolate the position with easing
                double x = currentLocation.getX() + (targetLocation.getX() - currentLocation.getX()) * ease;
                double y = currentLocation.getY() + (targetLocation.getY() - currentLocation.getY()) * ease;
                double z = currentLocation.getZ() + (targetLocation.getZ() - currentLocation.getZ()) * ease;

                // Set new location without teleportation
                creeper.teleport(new Location(playerLocation.getWorld(), x, y, z));

                ticks[0]++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run every tick (50ms)

        Bukkit.broadcastMessage("Starting animation done");
    }

    private void explodeCreeper(Creeper creeper) {
        if (!creeper.isDead()) {
            creeper.getWorld().createExplosion(creeper.getLocation(), 0F, false, false); // 0F power, no fire, no block damage
            creeper.remove();
            // Sound effect for the explosion
            creeper.getWorld().playSound(creeper.getLocation(), "entity.creeper.primed", 1.0F, 1.0F);
            // Particle effect for the explosion
            creeper.getWorld().spawnParticle(Particle.EXPLOSION, creeper.getLocation(), 1);
        }
    }

}