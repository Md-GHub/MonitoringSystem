package com.md.monitoringsystem.monitorexecution;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.utils.PostgresConnections;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbThread {
    Connection conn = PostgresConnections.getConnection();
    ThreadPool threadPool = new ThreadPool();
    public void getConnection() throws SQLException {
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet crs = factory.createCachedRowSet();
        crs.setUrl("jdbc:postgresql://localhost:5432/monitoringsystem");
        crs.setCommand("select * from monitoringsystem");
        crs.setPassword("080703");
        crs.setUsername("postgres");
        crs.execute();
        while(!crs.next()) {
            if(crs.getDate("last_update").equals(Calendar.getInstance().getTime())) {
                Monitor monitor = new Monitor();
                monitor.setId(crs.getInt("monitor_id"));
                monitor.setMonitorName(crs.getString("monitor_name"));
                monitor.setUrl(crs.getString("monitor_url"));
                monitor.setInterval(crs.getString("interval"));
                monitor.setStatus(crs.getString("status"));
                monitor.setActive(crs.getBoolean("active"));
                monitor.setLastUpdate(new java.util.Date(crs.getString("last_update")));
                Task task = new Task(monitor);
                threadPool.submit(task);
                crs.updateString("last_update",new Date(System.currentTimeMillis()+(Integer.parseInt(monitor.getInterval())*1000*60)).toString());
                crs.updateRow();
                crs.acceptChanges();
            }
        }
    }
}
