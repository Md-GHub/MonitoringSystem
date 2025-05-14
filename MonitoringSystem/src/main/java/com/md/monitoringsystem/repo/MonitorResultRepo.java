package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.MonitorResult;
import com.md.monitoringsystem.model.PublicMonitorHistory;
import com.md.monitoringsystem.model.PublicMonitorHistoryBuilder;
import com.md.monitoringsystem.utils.PostgresConnections;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorResultRepo {
    private String monitorJoinMonitorResult = "select r.time,m.remark remark,m.monitor_id, m.monitor_name,m.url,m.status status_expected,m.interval,m.active,r.response_time,r.timestamp,r.status status_received,r.success_status from monitor m left join monitor_result r on m.monitor_id=r.monitor_id order by r.time desc limit 10 offset ? ";
    private String insertIntoResult = "INSERT INTO MONITOR_RESULT(monitor_id,response_time,timestamp,status,success_status,time) VALUES(?,?,?,?,?,?)";

    private static MonitorResultRepo instance = null;
    public static MonitorResultRepo get() {
        if (instance == null) {
            instance = new MonitorResultRepo();
        }
        return instance;
    }

    public void insert(MonitorResult monitorResult) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(insertIntoResult)){
            statement.setInt(1,monitorResult.getResultId());
            statement.setInt(2,monitorResult.getResponseTime());
            statement.setDate(3,monitorResult.getTimestamp());
            statement.setString(4,monitorResult.getStatus());
            statement.setBoolean(5,monitorResult.isSuccess());
            statement.setTimestamp(6,new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

//    public List<PublicMonitorHistory> getAllMonitorResult(int offset) {
//        Connection conn = PostgresConnections.getConnection();
//        List<PublicMonitorHistory> monitorResults = new ArrayList<>();
//        try(Statement statement = conn.createStatement()){
//            ResultSet resultSet = statement.executeQuery(monitorJoinMonitorResult+offset);
//            while (resultSet.next()) {
//                PublicMonitorHistory p = new PublicMonitorHistoryBuilder()
//                                                .setMonitorId(resultSet.getInt("monitor_id"))
//                                                .setMonitorName(resultSet.getString("monitor_name"))
//                                                .setUrl(resultSet.getString("url"))
//                                                .setTime(resultSet.getTimestamp("time"))
////                                                .setStatusReceived(resultSet.getString("status_received"))
//                                                .setSuccess_status(resultSet.getBoolean("success_status"))
//                                                .setRemark(resultSet.getString("remark"))
//                                                .build();
//                monitorResults.add(p);
//            }
//        }catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }finally {
//            PostgresConnections.returnConnection(conn);
//        }
//        return monitorResults;
//    }

    public List<PublicMonitorHistory> getAllMonitorResult(int offset) {
        Connection conn = PostgresConnections.getConnection();
        List<PublicMonitorHistory> monitorResults = new ArrayList<>();
        try(PreparedStatement statement = conn.prepareStatement(monitorJoinMonitorResult)){
            statement.setInt(1,offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PublicMonitorHistory p = new PublicMonitorHistoryBuilder()
                        .setMonitorId(resultSet.getInt("monitor_id"))
                        .setMonitorName(resultSet.getString("monitor_name"))
                        .setUrl(resultSet.getString("url"))
                        .setTime(resultSet.getTimestamp("time"))
//                                                .setStatusReceived(resultSet.getString("status_received"))
                        .setSuccess_status(resultSet.getBoolean("success_status"))
                        .setRemark(resultSet.getString("remark"))
                        .build();
                monitorResults.add(p);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return monitorResults;
    }
}
