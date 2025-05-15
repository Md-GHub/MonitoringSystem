package com.md.monitoringsystem.monitorexecution;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
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

//        Connection connections = PostgresConnections.getConnection();
//        String selectAllFromMonitorGroupByIntervals = "SELECT * FROM MONITOR GROUP BY interval";
//        try(Statement statement = connections.createStatement()){
//            ResultSet rs = statement.executeQuery(selectAllFromMonitorGroupByIntervals);
//            while(rs.next()){
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        int l1 = "$2a$10$/Fyq3/sSvTY4dYtseiHCL.sYVNGeDUWT9H1xNOgvOeKeyxLknUdPK".length();
        int l2 = "$2a$10$E1YaF4X9cCsvoHDp7ki/IehLYvos50WAFrTn7IFsuIGyfoHn2OEAa".length();
        int l3 = "$2a$10$y/y48G0qLxIJ8wXN5lu9XeB7KYhFK.qylDT65n5Ok1.7CWidUljTC".length();
        System.out.println((l1==l2 && l1==l3)+" "+l1);
    }
}
