package be.thatsatim.gravestone;

import be.thatsatim.gravestone.commands.RetrieveGraveStoneCommand;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.listeners.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.sql.SQLException;
import java.util.List;

public final class Gravestone extends JavaPlugin {

    private GravestoneDatabase gravestoneDatabase;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new Death(this);
        new RightClick(this);
        new BreakBlock(this);
        new GuiClose(this);
        new InventoryClick(this);
        new InventoryDrag(this);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            this.registerCommands(commands);
        });

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            gravestoneDatabase = new GravestoneDatabase(this);
            GravestoneDatabase.initGravestonesCache();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to connect to the database! " + exception.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        try {
            gravestoneDatabase.closeConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void registerCommands(Commands commands) {
        RetrieveGraveStoneCommand.register(commands);
    }
}
