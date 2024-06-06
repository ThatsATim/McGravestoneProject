package be.thatsatim.gravestone;

import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.listeners.BreakBlock;
import be.thatsatim.gravestone.listeners.Death;
import be.thatsatim.gravestone.listeners.RightClick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Gravestone extends JavaPlugin {

    private GravestoneDatabase gravestoneDatabase;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new Death(this);
        new RightClick(this);
        new BreakBlock(this);

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            gravestoneDatabase = new GravestoneDatabase(this);
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
}
