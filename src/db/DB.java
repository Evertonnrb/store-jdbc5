package db;

import db.exception.DBException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fileInputStream);
            return props;
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }

    public static void closeStatements(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }


}
