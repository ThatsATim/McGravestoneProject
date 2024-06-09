package be.thatsatim.gravestone.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Memory {

    private static final HashMap<Player, Location> openGravestones = new HashMap<>();

    public static void openGravestone(Player player, Location location) { openGravestones.put(player, location); }

    public static Location closeGravestone(Player player) { return openGravestones.get(player); }

}
