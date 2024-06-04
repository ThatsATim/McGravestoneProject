package be.thatsatim.gravestone.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GravestoneDatabase {

    private final Connection connection;

    public GravestoneDatabase(String path) throws SQLException {
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

    public void addGravestone() {

    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
