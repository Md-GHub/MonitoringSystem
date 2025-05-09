package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.exception.ErrorInCreatingUser;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRepo {
    private static UserRoleRepo instance = null;
    private String insertUserRole = "insert into USER_ROLE(USER_ID,ROLE_ID) values(?,?)";
    private String getRoleById = "select ROLE_ID from USER_ROLE where USER_ID=?";
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
            if(ps.executeUpdate()==0){
                throw new ErrorInCreatingUser("Error in inserting user role");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }


    public int getRoleIdByUserId(int userId) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement ps = conn.prepareStatement(getRoleById)){
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return -1;
    }
}
