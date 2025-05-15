package com.md.monitoringsystem.repo;

import com.md.monitoringsystem.model.MonitorAudit;
import com.md.monitoringsystem.model.MonitorAuditDisplay;
import com.md.monitoringsystem.utils.PostgresConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorAuditRepo {
    private String insertData = "INSERT INTO AUDIT(monitor_id,user_id,remark) VALUES(?,?,?)";
    private String getAllMonitors = "select a.audit_id,a.date,a.remark,m.monitor_name,m.url,m.status,m.active,u.user_name from audit a inner join monitor m on m.monitor_id=a.monitor_id inner join users u on a.user_id = u.user_id;";
    private static MonitorAuditRepo instance = null;
    public static MonitorAuditRepo get() {
        if (instance == null) {
            instance = new MonitorAuditRepo();
        }
        return instance;
    }

    public void insert(MonitorAudit m) {
        Connection conn = PostgresConnections.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(insertData)){
            statement.setInt(1,m.getMonitor().getId());
            statement.setInt(2,m.getUser().getUserId());
            statement.setString(3,m.getRemark());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
    }

    public List<MonitorAuditDisplay> getAll() {
        Connection conn = PostgresConnections.getConnection();
        List<MonitorAuditDisplay> l = new ArrayList();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(getAllMonitors);
            while(rs.next()){
                MonitorAuditDisplay m = new MonitorAuditDisplay();
                m.setAuditId(rs.getInt("audit_id"));
                m.setAuditDate(rs.getDate("date"));
                m.setRemark(rs.getString("remark"));
                m.setMonitor_name(rs.getString("monitor_name"));
                m.setUrl(rs.getString("url"));
                m.setStatus(rs.getString("status"));
                m.setActive(rs.getBoolean("active"));
                m.setChangedBy(rs.getString("user_name"));
                l.add(m);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            PostgresConnections.returnConnection(conn);
        }
        return l;
    }

}
