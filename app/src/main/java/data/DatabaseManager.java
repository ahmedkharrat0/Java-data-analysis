package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class DatabaseManager {
    private Connection connection;
        public DatabaseManager(Connection connection) {
        this.connection = connection;
    }

    public DatabaseManager(String dbUrl, String user, String password) {
        try {
            // Establish the connection with PostgreSQL
            connection = DriverManager.getConnection(dbUrl, user, password);
            
            // Create tables if they don't exist
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create the tables (co2, ch4) if they do not already exist
    private void createTables() {
        String createCo2Table = "CREATE TABLE IF NOT EXISTS co2 (" +
            "id SERIAL PRIMARY KEY, " +
            "domain VARCHAR(255), " +
            "area VARCHAR(255), " +
            "source VARCHAR(255), " +
            "year INTEGER, " +
            "unit VARCHAR(255), " +
            "value DOUBLE PRECISION" +
        ")";

        String createCh4Table = "CREATE TABLE IF NOT EXISTS ch4 (" +
            "id SERIAL PRIMARY KEY, " +
            "domain VARCHAR(255), " +
            "area VARCHAR(255), " +
            "source VARCHAR(255), " +
            "year INTEGER, " +
            "unit VARCHAR(255), " +
            "value DOUBLE PRECISION" +
        ")";


        // Execute table creation queries
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createCo2Table);
            stmt.executeUpdate(createCh4Table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a record into the respective table based on emission type
    public void insertEmissionRecord(EmissionRecord record) {
        String query = "INSERT INTO " + record.getEmissions().toLowerCase() + " (domain, area, source, year, unit, value) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, record.getDomain());
            stmt.setString(2, record.getArea());
                stmt.setString(3, record.getSource());
            stmt.setInt(4, record.getYear());
            stmt.setString(5, record.getUnit());
            stmt.setDouble(6, record.getValue());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve data from the database
    public ResultSet getData(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
