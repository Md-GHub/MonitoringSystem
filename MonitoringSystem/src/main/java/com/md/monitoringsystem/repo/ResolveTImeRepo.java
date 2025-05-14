package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.ResolveTime;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ResolveTImeRepo {
    private String insert = "INSERT INTO RESOLVE_TIME(MONITOR_ID , downAt,reason_for_down) VALUES(?,?,?)";
    private String update = "UPDATE RESOLVE_TIME SET resolvedAt = ? WHERE MONITOR_ID = ?";
    public void insert(ResolveTime resolveTime) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(insert)){
            statement.setInt(1,resolveTime.getMonitorId());
            statement.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            statement.setString(3,resolveTime.getRemarks());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public void update(ResolveTime resolveTime) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(update)){
            statement.setTimestamp(1,resolveTime.getUpAt());
            statement.setInt(2,resolveTime.getMonitorId());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }
}
