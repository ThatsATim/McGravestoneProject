package be.thatsatim.gravestone.database;

import be.thatsatim.gravestone.Gravestone;
import be.thatsatim.gravestone.utils.ItemSerializer;
import be.thatsatim.gravestone.utils.StringLocationConvertor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.sql.*;

public class GravestoneDatabase {

    private static Connection connection;

    public GravestoneDatabase(Gravestone plugin) throws SQLException {
        String path = plugin.getDataFolder().getAbsolutePath() + "/gravestone.db";
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
        String locationString = StringLocationConvertor.locationToString(location);
        String playerName = player.getName();
        String UUID = String.valueOf(player.getUniqueId());
        String inventory = ItemSerializer.serializeInventory(playerInventory);

        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO graves (location, username, uuid, inventoryContents) VALUES (?, ?, ?, ?)"
        )) {
            preparedStatement.setString(1, locationString);
            preparedStatement.setString(2, playerName);
            preparedStatement.setString(3, UUID);
            preparedStatement.setString(4, inventory);
            preparedStatement.executeUpdate();
        }

    }

    public static String[] getGravestone(Location location) throws SQLException {
        String locationString = StringLocationConvertor.locationToString(location);

        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM graves WHERE location = ?")) {
            preparedStatement.setString(1, locationString);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new String[] {
                        resultSet.getString("uuid"),
                        resultSet.getString("inventoryContents")
                };
            }
            return null;
        }
    }

    public static void deleteGravestone(Location location) throws SQLException {
        String locationString = StringLocationConvertor.locationToString(location);

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM graves WHERE location = ?")) {
            preparedStatement.setString(1, locationString);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
