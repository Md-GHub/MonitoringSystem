package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.exception.NoUserFounded;
import com.md.monitoringsystem.exception.UserNotInvited;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.utils.BcryptUtil;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {
    private final BcryptUtil bcryptUtil = new BcryptUtil();
    private static UserRepo instance = null;
    private String getUserId = "SELECT user_id FROM users WHERE EMAIL=?";
    private String insertUser = "insert into users(USER_NAME,EMAIL,ORG_ID) values(?,?,?)";
    private String insertUserAdmin = "insert into users(USER_NAME,EMAIL,ORG_ID,PASSWORD) values(?,?,?,?)";
    private String deleteUser = "delete from users where user_id=?";
    private String deleteUserByEmail = "delete from users where email=?";
    private String isInvited = "select isactive from users where email=?";
    private String getUserDetails = "select * from users where email=?";
    private String getOrgIdByUserId = "select org_id from users where user_id=?";
    public static UserRepo get() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    public int addUser(User user, int createdOrgId){
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertUser)){
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setInt(3,createdOrgId);
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
            return rs.getInt("user_id");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }


    public void deleteUser(int id) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(deleteUser)){
            statement.setInt(1,id);
            int result  = statement.executeUpdate();
            if(result == 0){
                throw new NoUserFounded("No user founded");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
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
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return user;
    }

    public void addPassword(int id, String password) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement st = con.prepareStatement("UPDATE USERS SET PASSWORD = ? WHERE USER_ID = ?")){
            st.setString(1,bcryptUtil.hashPassword(password));
            st.setInt(2,id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserByEmail(String email) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(deleteUserByEmail)){
            statement.setString(1,email);
            int result  = statement.executeUpdate();
            if(result == 0){
                throw new NoUserFounded("No user founded");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
    }

    public int addAdmin(User user, int createdOrgId) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertUserAdmin)){
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setInt(3,createdOrgId);
            String hashPass = bcryptUtil.hashPassword(user.getPassword());
            statement.setString(4,hashPass);
            statement.executeUpdate();
            return getUserId(user.getEmail());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    public int getOrgIdByUserId(int userId) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(getOrgIdByUserId)){
            statement.setInt(1,userId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt("org_id");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }
}
