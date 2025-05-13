package com.md.monitoringsystem.monitorexecution;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
//    private int id;
//    private String monitorName;
//    private String url;
//    private String status;
//    private String interval;
//    private java.util.Date lastUpdate = new java.util.Date();
//    private boolean isActive = true;

    public static void main(String[] args) {
//        Monitor monitor1 = new Monitor();
//        monitor1.setId(30);
//        monitor1.setMonitorName("Google Monitor");
//        monitor1.setUrl("https://www.test.com");
//        monitor1.setStatus("UP");
//        monitor1.setInterval("4");
//        monitor1.setActive(true);
//
//        Monitor monitor2 = new Monitor();
//        monitor2.setId(31);
//        monitor2.setMonitorName("GitHub Monitor");
//        monitor2.setUrl("https://www.github.com");
//        monitor2.setStatus("UP");
//        monitor2.setInterval("3");
//        monitor2.setActive(true);
//
//        Scheduler scheduler = new Scheduler();
//        scheduler.addMonitor(monitor1);
//        scheduler.addMonitor(monitor2);
//        scheduler.serve();

        Connection connections = PostgresConnections.getConnection();
        String selectAllFromMonitorGroupByIntervals = "SELECT * FROM MONITOR GROUP BY interval";
        try(Statement statement = connections.createStatement()){
            ResultSet rs = statement.executeQuery(selectAllFromMonitorGroupByIntervals);
            while(rs.next()){

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
