package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.exception.NoMonitorFounded;
import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.model.PublicUpDownTime;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorRepo {
//     name, target_url, expected_status_code(s), check_interval (in seconds), enabled (bool)
    private String createMonitor = "INSERT INTO MONITOR(MONITOR_NAME,URL,STATUS,INTERVAL,ACTIVE) VALUES(?,?,?,?,?)";
    private String deleteMonitor = "DELETE FROM MONITOR WHERE MONITOR_ID = ?";
    private String selectMonitorByid = "SELECT * FROM MONITOR WHERE MONITOR_ID = ?";
    private String selectAllMonitors = "SELECT * FROM MONITOR";
    private String updateStatus = "UPDATE MONITOR SET STATUS = ? WHERE MONITOR_ID = ?";
    private String updateInterval = "UPDATE MONITOR SET INTERVAL = ? WHERE MONITOR_ID = ?";
    private String updateEnabled = "UPDATE MONITOR SET active = ? WHERE MONITOR_ID = ?";
    private String getAllMonitorsInterval= "SELECT distinct interval FROM MONITOR";
    private String getAllMonitorsByInterval = "SELECT * FROM MONITOR where interval = ?";
    private String updateFails = "UPDATE MONITOR SET no_of_fails = ? WHERE MONITOR_ID = ?";

    private String monitorJoinResult1 = "SELECT m.monitor_id AS monitor_id,m.monitor_name,SUM(CASE WHEN r.success_status = 'true' THEN 1 ELSE 0 END) AS upCount,SUM(CASE WHEN r.success_status = 'false' THEN 1 ELSE 0 END) AS downCount, s.status FROM monitor m LEFT JOIN monitor_result r ON m.monitor_id = r.monitor_id left join monitor_current_status s on m.monitor_id = s.monitor_id WHERE  r.timestamp >= NOW() - INTERVAL \'";
    private String monitorJoinResult2 =  "\' GROUP BY m.monitor_id, m.monitor_name,s.status LIMIT 10 OFFSET ";

    private String updateRemarks = "UPDATE MONITOR SET remark = ? WHERE MONITOR_ID = ?";
    private static MonitorRepo instance = null;
    public static MonitorRepo get() {
        if (instance == null) {
            instance = new MonitorRepo();
        }
        return instance;
    }


    public int addMonitor(Monitor monitor) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(createMonitor)){
            statement.setString(1, monitor.getMonitorName());
            statement.setString(2,monitor.getUrl());
            statement.setString(3,monitor.getStatus());
            statement.setString(4,monitor.getInterval());
            statement.setBoolean(5,monitor.isActive());
            statement.executeUpdate();
            return getMonitorIdByUrl(monitor.getUrl());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return 0;
    }

    private int getMonitorIdByUrl(String url) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement("SELECT MONITOR_ID FROM MONITOR WHERE URL = ?")){
            statement.setString(1, url);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("MONITOR_ID");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void deleteMonitor(int id) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(deleteMonitor)){
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result == 0) {
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public Monitor getMonitorById(int id) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(selectMonitorByid)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Monitor monitor = new Monitor();
                monitor.setId(rs.getInt("MONITOR_ID"));
                monitor.setMonitorName(rs.getString("MONITOR_NAME"));
                monitor.setUrl(rs.getString("URL"));
                monitor.setStatus(rs.getString("STATUS"));
                monitor.setInterval(rs.getString("INTERVAL"));
                monitor.setActive(rs.getBoolean("ACTIVE"));
                monitor.setNoOfFails(rs.getInt("no_of_fails"));
                return monitor;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Monitor> getAllMonitors() {
        Connection conn = PostgresConnections.getConnection();
        List<Monitor> monitors = new ArrayList<>();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(selectAllMonitors);
            while (rs.next()){
                Monitor monitor = new Monitor();
                monitor.setId(rs.getInt("MONITOR_ID"));
                monitor.setMonitorName(rs.getString("MONITOR_NAME"));
                monitor.setUrl(rs.getString("URL"));
                monitor.setStatus(rs.getString("STATUS"));
                monitor.setInterval(rs.getString("INTERVAL"));
                monitor.setActive(rs.getBoolean("ACTIVE"));
                monitors.add(monitor);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return monitors;
    }

    public void updateStatus(int id, String status) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateStatus)){
            statement.setInt(2, id);
            statement.setString(1, status);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public void setUpdateInterval(int id, String updateTime) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateInterval)){
            statement.setInt(2, id);
            statement.setString(1, updateTime);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public void setUpdateEnabled(int id, boolean isEnabled) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateEnabled)){
            statement.setBoolean(1, isEnabled);
            statement.setInt(2, id);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public List<Integer> loadIntervals() {
        Connection conn = PostgresConnections.getConnection();
        List<Integer> intervals = new ArrayList<>();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(getAllMonitorsInterval);
            while (rs.next()) {
                intervals.add(Integer.parseInt(rs.getString("INTERVAL")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return intervals;
    }

    public List<Monitor> getAllMonitorByIntervals(Integer interval) {
        Connection conn = PostgresConnections.getConnection();
        List<Monitor> monitors = new ArrayList<>();
        try(PreparedStatement statement = conn.prepareStatement(getAllMonitorsByInterval)){
            statement.setString(1, interval.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Monitor monitor = new Monitor();
                monitor.setId(rs.getInt("MONITOR_ID"));
                monitor.setMonitorName(rs.getString("MONITOR_NAME"));
                monitor.setUrl(rs.getString("URL"));
                monitor.setStatus(rs.getString("STATUS"));
                monitor.setInterval(rs.getString("INTERVAL"));
                monitor.setActive(rs.getBoolean("ACTIVE"));
                monitor.setNoOfFails(rs.getInt("no_of_fails"));
                monitors.add(monitor);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return monitors;
    }

    public void setUpdateFails(int id, int noOfFails) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateFails)){
            statement.setInt(1, noOfFails);
            statement.setInt(2, id);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public List<PublicUpDownTime> getUptimeAndDowntime(String timeLine,int offsetValue) {
        Connection conn = PostgresConnections.getConnection();
        List<PublicUpDownTime> uptimeAndDowntime = new ArrayList<>();
        String monitorJoinResult = monitorJoinResult1+timeLine+monitorJoinResult2+offsetValue;
        System.out.println(monitorJoinResult);
        try(PreparedStatement statement = conn.prepareStatement(monitorJoinResult)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PublicUpDownTime upTime = new PublicUpDownTime();
                upTime.setMonitorId(rs.getInt("MONITOR_ID"));
                upTime.setMonitorName(rs.getString("MONITOR_NAME"));
                upTime.setUpCount(rs.getInt("UPCOUNT"));
                upTime.setDownCount(rs.getInt("DOWNCOUNT"));
                upTime.setStatus(rs.getBoolean("status"));
                int totalRequest = upTime.getUpCount()+upTime.getDownCount();
                int uptime = upTime.getUpCount();
                float percentage = (float) uptime/totalRequest *100;

                upTime.setPercentage((int) percentage);
                uptimeAndDowntime.add(upTime);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return uptimeAndDowntime;
    }

    public void setUpdateRemark(String remark, int id) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(updateRemarks)){
            statement.setString(1, remark);
            statement.setInt(2, id);
            if(statement.executeUpdate() == 0){
                throw new NoMonitorFounded("No monitor found ");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }
}
