package be.thatsatim.gravestone.utils;

import org.bukkit.Location;

public class StringLocationConvertor {

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + Math.round(location.getX()) + ", " + Math.round(location.getY()) + ", " + Math.round(location.getZ());
    }

    public static Location stringToLocation(String string) {
        String[] parts = string.split(", ");
        return new Location(org.bukkit.Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }
}