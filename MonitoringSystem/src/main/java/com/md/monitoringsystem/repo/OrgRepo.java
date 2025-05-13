package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.*;

public class OrgRepo {
    private static OrgRepo instance = null;
    private String checkOrgName = "SELECT ORG_ID FROM ORGANIZATION WHERE ORG_DOMAIN in (?)";
    private String insertOrg = "INSERT INTO ORGANIZATION(ORG_NAME,ORG_DOMAIN) VALUES(?,?)";
    private String getOrgId = "SELECT ORG_ID FROM ORGANIZATION WHERE ORG_DOMAIN in (?)";
    public static OrgRepo get() {
        if (instance == null) {
            instance = new OrgRepo();
        }
        return instance;
    }

    public int isExist(String orgName) {
        Connection con = PostgresConnections.getConnection();

        try(PreparedStatement statement = con.prepareStatement(checkOrgName)){
            statement.setString(1,orgName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if(rs!=null){
                return rs.getInt("ORG_ID");
            }
        } catch (SQLException e) {

        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    public int addOrg(String orgName) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(insertOrg)){
            statement.setString(1,orgName.split(" ")[0]);
            statement.setString(2,orgName);
            statement.executeUpdate();
            return isExist(orgName);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    public int getOrgId(String orgName) {
        Connection con = PostgresConnections.getConnection();
        try(PreparedStatement statement = con.prepareStatement(getOrgId)){
            statement.setString(1,orgName);
            ResultSet st = statement.executeQuery();
            if(st.next()){
                return st.getInt("ORG_ID");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(con);
        }
        return -1;
    }

    public int countOrg() {
        Connection con = PostgresConnections.getConnection();
        try(Statement statement = con.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(ORG_ID) AS NUM FROM ORGANIZATION");
            rs.next();
            return rs.getInt("NUM");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            PostgresConnections.returnConnection(con);
        }
    }
}
