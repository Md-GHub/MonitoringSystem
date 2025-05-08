package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorRepo {
//     name, target_url, expected_status_code(s), check_interval (in seconds), enabled (bool)
    private String createMonitor = "INSERT INTO MONITOR(MONITOR_NAME,URL,STATUS,INTERVAL,ACTIVE,LAST_UPDATE) VALUES(?,?,?,?,?,?)";
    private String deleteMonitor = "DELETE FROM MONITOR WHERE MONITOR_ID = ?";
    private String selectMonitorByid = "SELECT * FROM MONITOR WHERE MONITOR_ID = ?";
    private String selectAllMonitors = "SELECT * FROM MONITOR";
    private String updateStatus = "UPDATE MONITOR SET STATUS = ? WHERE MONITOR_ID = ?";
    private String updateInterval = "UPDATE MONITOR SET INTERVAL = ? WHERE MONITOR_ID = ?";
    private String updateEnabled = "UPDATE MONITOR SET enabled = ? WHERE MONITOR_ID = ?";

    private static MonitorRepo instance = null;
    public static MonitorRepo get() {
        if (instance == null) {
            instance = new MonitorRepo();
        }
        return instance;
    }


    public void addMonitor(Monitor monitor) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(createMonitor)){
            statement.setString(1, monitor.getMonitorName());
            statement.setString(2,monitor.getUrl());
            statement.setString(3,monitor.getStatus());
            statement.setString(4,monitor.getInterval());
            statement.setBoolean(5,monitor.isActive());
            statement.setString(6,monitor.getLastUpdate().toString());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteMonitor(int id) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(deleteMonitor)){
            statement.setInt(1, id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public Monitor getMonitorById(int id) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(selectMonitorByid)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Monitor monitor = new Monitor();
                monitor.setId(rs.getInt("MONITOR_ID"));
                monitor.setMonitorName(rs.getString("MONITOR_NAME"));
                monitor.setUrl(rs.getString("URL"));
                monitor.setStatus(rs.getString("STATUS"));
                monitor.setInterval(rs.getString("INTERVAL"));
                monitor.setActive(rs.getBoolean("ACTIVE"));
                return monitor;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Monitor> getAllMonitors() {
        Connection conn = PostgresConnections.getConnection();
        List<Monitor> monitors = new ArrayList<>();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(selectAllMonitors);
            while (rs.next()){
                Monitor monitor = new Monitor();
                monitor.setId(rs.getInt("MONITOR_ID"));
                monitor.setMonitorName(rs.getString("MONITOR_NAME"));
                monitor.setUrl(rs.getString("URL"));
                monitor.setStatus(rs.getString("STATUS"));
                monitor.setInterval(rs.getString("INTERVAL"));
                monitor.setActive(rs.getBoolean("ACTIVE"));
                monitors.add(monitor);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return monitors;
    }

    public void updateStatus(int id, String status) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateStatus)){
            statement.setInt(2, id);
            statement.setString(1, status);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void setUpdateInterval(int id, String updateTime) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateInterval)){
            statement.setInt(2, id);
            statement.setString(1, updateTime);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void setUpdateEnabled(int id, boolean isEnabled) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateEnabled)){
            statement.setInt(2, id);
            statement.setBoolean(1, isEnabled);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
