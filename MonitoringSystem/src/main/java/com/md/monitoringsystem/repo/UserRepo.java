package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.exception.UserNotInvited;
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
    private String isInvited = "select isactive from users where email=?";
    private String getUserDetails = "select * from users where email=?";
    public static UserRepo get() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    public int addUser(User user, int createdOrgId,boolean isActive){
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertUser)){
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getEmail());
            statement.setInt(4,createdOrgId);
            statement.setBoolean(5,isActive);
            statement.executeUpdate();
            return getUserId(user.getEmail());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    public int getUserId(String email) {
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

    public boolean isInvited(String email) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(isInvited)){
            statement.setString(1,email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getBoolean(1);
            }else{
                throw new UserNotInvited("User is not invited");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return false;
    }

    public User getUserDetails(String email) {
        Connection con = PostgresConnections.getConnection();
        User user = null;
        try(PreparedStatement statement = con.prepareStatement(getUserDetails)){
            statement.setString(1,email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getBoolean("isactive"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return user;
    }
}
