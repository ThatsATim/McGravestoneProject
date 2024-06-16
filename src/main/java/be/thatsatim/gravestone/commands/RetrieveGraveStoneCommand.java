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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class RetrieveGraveStoneCommand implements Command<CommandSourceStack> {

    public static void register(Commands commands) {
        LiteralArgumentBuilder<CommandSourceStack> commandBuilder =
            Commands.literal("retrieve-gravestone")
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("world", StringArgumentType.string())
                    .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("x", StringArgumentType.string())
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("y", StringArgumentType.string())
                            .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("z", StringArgumentType.string())
                                .executes(new RetrieveGraveStoneCommand())))));

        commands.register(
            commandBuilder.build(),
            "Retrieve a gravestone",
            List.of("rg")
        );
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        Entity entity = source.getExecutor();

        if (!(entity instanceof Player)) {
            return 0;
        }

        Player player = (Player) entity;
        Location location = entity.getLocation();

        String world = context.getArgument("world", String.class);
        String x = context.getArgument("x", String.class);
        String y = context.getArgument("y", String.class);
        String z = context.getArgument("z", String.class);

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
        player.sendMessage("Distance: " + distance);


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
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to delete the database entry! " + exception.getMessage());
        }

        entity.sendMessage("You are a player. " + x + " " + y + " " + z);

        return Command.SINGLE_SUCCESS;
    }
}