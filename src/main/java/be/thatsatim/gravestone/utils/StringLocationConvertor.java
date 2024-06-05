package be.thatsatim.gravestone.utils;

import org.bukkit.Location;

public class StringLocationConvertor {

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ();
    }
    
}