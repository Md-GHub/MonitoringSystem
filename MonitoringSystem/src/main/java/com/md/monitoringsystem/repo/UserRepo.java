package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {
    private static UserRepo instance = null;
    private String getUserId = "SELECT user_id FROM users WHERE EMAIL=?";
    private String insertUser = "insert into users(USER_NAME,PASSWORD,EMAIL,ORG_ID,isactive) values(?,?,?,?,?)";
    private String activateUser = "update users set isactive=true where user_id=?";
    private String deleteUser = "delete from users where user_id=?";
    public static UserRepo get() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    public int addUser(User user, int createdOrgId) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertUser)){
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getEmail());
            statement.setInt(4,createdOrgId);
            statement.setBoolean(5,false);
            statement.executeUpdate();
            return getUserId(user.getEmail());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    private int getUserId(String email) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(getUserId)){
            statement.setString(1,email);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void activateUser(int id) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(activateUser)){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(int id) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(deleteUser)){
            statement.setInt(1,id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
