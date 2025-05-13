package com.md.monitoringsystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PostgresConnections {
    private static Queue<Connection> connectionPool = new LinkedList<>();
    private static int MAX_CONNECTIONS = 20;
    private static String URL = "jdbc:postgresql://localhost:5432/MonitoringSystem";
    private static String USER = "postgres";
    private static String PASSWORD = "080703";
    public static void createConnection() {
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connectionPool.add(connection);
                System.out.println("Connection added to pool.");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Error initializing connection pool: " + e.getMessage());
            }
        }
    }

    public static synchronized Connection getConnection() {
        if (connectionPool.isEmpty()) {
            System.err.println("No available connections in the pool.");
            return null;
        }
        return connectionPool.poll();
    }

    public static synchronized void returnConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connectionPool.offer(connection);
                    System.out.println("Connection returned to pool.");
                }
            } catch (SQLException e) {
                System.err.println("Error returning connection to pool: " + e.getMessage());
            }
        }
    }

    public static void closeConnections() {
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
