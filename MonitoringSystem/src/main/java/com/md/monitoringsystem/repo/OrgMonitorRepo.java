package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrgMonitorRepo {
    private String insert = "INSERT INTO ORG_MONITOR VALUES (?,?)";
    private static OrgMonitorRepo instance = null;
    public static OrgMonitorRepo get() {
        if (instance == null) {
            instance = new OrgMonitorRepo();
        }
        return instance;
    }

    public void add(int monitorId, int orgId) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insert)){

            statement.setInt(1, orgId);
            statement.setInt(2, monitorId);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
    }
}
