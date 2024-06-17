package be.thatsatim.gravestone;

import be.thatsatim.gravestone.commands.RetrieveGraveStoneCommand;
import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.listeners.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.sql.SQLException;

public final class Gravestone extends JavaPlugin {
    private GravestoneDatabase gravestoneDatabase;
    private Economy economy;

    @Override
    public void onEnable() {
        // Create the default config if it doesn't exist
        this.saveDefaultConfig();

        // Setup Vault
        if (!setupEconomy()) {
            getLogger().severe("Vault not found or Economy not supported! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the listeners
        new Death(this);
        new RightClick(this);
        new BreakBlock(this);
        new GuiClose(this);
        new InventoryClick(this);
        new InventoryDrag(this);

        // Register the commands
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            this.registerCommands(commands);
        });

        // Create the database connection
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
            if (gravestoneDatabase != null) {
                gravestoneDatabase.closeConnection();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void registerCommands(Commands commands) {
        RetrieveGraveStoneCommand.register(this, commands);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
}
