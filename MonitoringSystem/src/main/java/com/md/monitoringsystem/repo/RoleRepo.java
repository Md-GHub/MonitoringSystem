package com.md.monitoringsystem.repo;


import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRepo {
    private static RoleRepo instance = null;
    private String getRoleId = "SELECT role_id FROM roles WHERE role_name = ?";
    private String getRoleName = "SELECT role_name FROM roles WHERE role_id = ?";
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

    public Role getRoleById(int roleId) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(getRoleName)){
            statement.setInt(1,roleId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return Role.valueOf(rs.getString("role_name"));
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return null;
    }
}
