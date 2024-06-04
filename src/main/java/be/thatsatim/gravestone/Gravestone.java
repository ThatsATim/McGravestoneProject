package be.thatsatim.gravestone;

import be.thatsatim.gravestone.listeners.Death;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gravestone extends JavaPlugin {

    @Override
    public void onEnable() {
        new Death(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
