package be.thatsatim.gravestone.utils;

import org.bukkit.Location;

public class StringLocationConvertor {

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + Math.round(location.getX()) + ", " + Math.round(location.getY()) + ", " + Math.round(location.getZ());
    }
    
}