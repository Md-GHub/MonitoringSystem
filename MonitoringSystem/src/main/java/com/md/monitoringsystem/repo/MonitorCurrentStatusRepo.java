package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.MonitorCurrentStatus;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MonitorCurrentStatusRepo {
    private String updateMonitor = "update monitor_current_status set status = ? where monitor_id = ?";
    private String insertMonitor = "insert into monitor_current_status values(?,?)";
//    private String isMonitorAvailable = "select monitor_id from monitor_current_status where monitor_id = ?";
    private static MonitorCurrentStatusRepo instance = null;
    public static MonitorCurrentStatusRepo get() {
        if (instance == null) {
            instance = new MonitorCurrentStatusRepo();
        }
        return instance;
    }
    public void updateMonitorCurrentStatus(MonitorCurrentStatus monitorCurrentStatus) {
        Connection connection = PostgresConnections.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(updateMonitor)){
            statement.setBoolean(1,monitorCurrentStatus.isStatus());
            statement.setInt(2,monitorCurrentStatus.getMonitor().getId());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(connection);
        }
    }

    public void insert(MonitorCurrentStatus monitorCurrentStatus) {
        Connection connection = PostgresConnections.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(insertMonitor)){
            statement.setInt(1,monitorCurrentStatus.getMonitor().getId());
            statement.setBoolean(2,monitorCurrentStatus.isStatus());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(connection);
        }
    }
}
