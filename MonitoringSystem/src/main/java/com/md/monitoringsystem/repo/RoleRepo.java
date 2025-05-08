package com.md.monitoringsystem.repo;


import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRepo {
    private static RoleRepo instance = null;
    private String getRoleId = "SELECT role_id FROM roles WHERE role_name = ?";
    public static RoleRepo get() {
        if (instance == null) {
            instance = new RoleRepo();
        }
        return instance;
    }

    public int getRoleId(String roleName) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(getRoleId)){
            statement.setString(1,roleName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }
}
