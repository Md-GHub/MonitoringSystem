package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.Notification;
import com.md.monitoringsystem.model.NotificationDisplay;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepo {
    private String insertIntoTable = "insert into notification(monitor_id,remark) values(?,?)";
    private String getAllNotifications = "select m.monitor_name,n.remark from notification n inner join monitor m on n.monitor_id = m.monitor_id";
    private static NotificationRepo instance = null;
    public static NotificationRepo get() {
        if (instance == null) {
            instance = new NotificationRepo();
        }
        return instance;
    }

    public void insertIntoTable(Notification notification) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertIntoTable)){
            statement.setInt(1,notification.getMonitor().getId());
            statement.setString(2,notification.getRemark());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
    }

    public List<NotificationDisplay> getAllNotifications() {
        Connection con = PostgresConnections.getConnection();
        List<NotificationDisplay> notifications = new ArrayList<>();
        try(Statement statement = con.createStatement()){
            ResultSet rs = statement.executeQuery(getAllNotifications);
            while (rs.next()) {
                NotificationDisplay notificationDisplay = new NotificationDisplay();
                notificationDisplay.setMonitorName(rs.getString("monitor_name"));
                notificationDisplay.setNotification(rs.getString("remark"));
                notifications.add(notificationDisplay);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return notifications;
    }
}
