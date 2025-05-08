package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRoleRepo {
    private static UserRoleRepo instance = null;
    private String insertUserRole = "insert into USER_ROLE(USER_ID,ROLE_ID) values(?,?)";
    public static UserRoleRepo get() {
        if (instance == null) {
            instance = new UserRoleRepo();
        }
        return instance;
    }

    public void insertUserRole(int roleId, int userId) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement ps = conn.prepareStatement(insertUserRole)){
            ps.setInt(1,userId);
            ps.setInt(2,roleId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }
}
