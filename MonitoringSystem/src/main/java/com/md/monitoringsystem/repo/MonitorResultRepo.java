package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.MonitorResult;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MonitorResultRepo {
    private String insertIntoResult = "INSERT INTO MONITOR_RESULT(monitor_id,response_time,timestamp,status,success_status) VALUES(?,?,?,?,?)";
    private Connection conn = PostgresConnections.getConnection(); /// ** remember : separate conn or same conn **
    private static MonitorResultRepo instance = null;
    public static MonitorResultRepo get() {
        if (instance == null) {
            instance = new MonitorResultRepo();
        }
        return instance;
    }

    public void insert(MonitorResult monitorResult) {
        try(PreparedStatement statement = conn.prepareStatement(insertIntoResult)){
            statement.setInt(1,monitorResult.getResultId());
            statement.setInt(2,monitorResult.getResponseTime());
            statement.setDate(3,monitorResult.getTimestamp());
            statement.setString(4,monitorResult.getStatus());
            statement.setBoolean(5,monitorResult.isSuccess());
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
