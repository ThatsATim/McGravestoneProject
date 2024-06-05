package be.thatsatim.gravestone.database;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.utils.ItemSerializer;
import be.thatsatim.gravestone.utils.StringLocationConvertor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GravestoneDatabase {

    private static Connection connection;
    private static String path;

    public GravestoneDatabase(Gravestone plugin) throws SQLException {
        path = plugin.getDataFolder().getAbsolutePath() + "/gravestone.db";
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try(Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS graves (
                location TEXT PRIMARY KEY,
                username TEXT,
                uuid TEXT,
                inventoryContents TEXT);
            """);
        }

    }

    public static void addGravestone(Location location, Player player, PlayerInventory playerInventory) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        String inventory = ItemSerializer.serializeInventory(playerInventory);
        String locationString = StringLocationConvertor.locationToString(location);
        String playerName = player.getName();
        String UUID = String.valueOf(player.getUniqueId());

        try(Statement statement = connection.createStatement()) {
            statement.execute("""
                INSERT INTO graves (location, username, uuid, inventoryContents)
                VALUES
                """ + "('" + locationString + "', '" + playerName + "', '" + UUID + "', '" + inventory + "');"
            );
        }

    }

    public static void getGravestone(Location location) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        String locationString = StringLocationConvertor.locationToString(location);

        try(Statement statement = connection.createStatement()) {
            statement.execute("""
                SELECT * FROM graves WHERE location =
                """ + " '" + locationString + "';"
            );
        }
    }

    public static void deleteGravestone(Location location) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        String locationString = StringLocationConvertor.locationToString(location);

        try(Statement statement = connection.createStatement()) {
            statement.execute("""
                DELETE FROM graves WHERE location =
                """ + " '" + locationString + "';"
            );
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
