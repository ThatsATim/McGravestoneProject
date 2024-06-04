package be.thatsatim.gravestone.utils;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Location {

    public static void getSafe(org.bukkit.Location location) {
        if (isSafe(location)) {return;}
        Material block = location.getBlock().getType();
        if (block != Material.AIR) {inGround(location);}
        if (block == Material.AIR) {inAir(location);}
    }

    private static boolean isSafe(org.bukkit.Location location) {
        Block block = location.getBlock();
        Block ground = location.subtract(0, 1,0).getBlock();
        location.add(0, 1, 0);
        return block.getType() == Material.AIR && ground.getType() != Material.AIR;
    }

    private static void inGround(org.bukkit.Location location) {
        org.bukkit.Location newLocation = location.add(0, 1, 0);
        getSafe(newLocation);
    }

    private static void inAir(org.bukkit.Location location) {
        org.bukkit.Location newLocation = location.subtract(0, 1, 0);
        getSafe(newLocation);
    }

}
