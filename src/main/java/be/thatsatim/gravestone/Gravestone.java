package be.thatsatim.gravestone;

import be.thatsatim.gravestone.database.GravestoneDatabase;
import be.thatsatim.gravestone.listeners.Death;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Gravestone extends JavaPlugin {

    private GravestoneDatabase gravestoneDatabase;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new Death(this);

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            gravestoneDatabase = new GravestoneDatabase(getDataFolder().getAbsolutePath() + "/gravestone.db");
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
