package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.Notification;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationRepo {
    private String insertIntoTable = "insert into notification(monitor_id,remark) values(?,?)";

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
}
