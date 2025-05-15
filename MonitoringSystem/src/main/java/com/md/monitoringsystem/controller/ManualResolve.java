package com.md.monitoringsystem.controller;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.model.Notification;
import com.md.monitoringsystem.model.ResolveTime;
import com.md.monitoringsystem.repo.NotificationRepo;
import com.md.monitoringsystem.repo.ResolveTImeRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/manualentry")
public class ManualResolve extends HttpServlet {
    private ResolveTImeRepo repo = new ResolveTImeRepo();
    private NotificationRepo notificationRepo = NotificationRepo.get();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String remarks = req.getParameter("remarks");
        int id = Integer.parseInt(req.getParameter("mid"));

        try{
            ResolveTime time = new ResolveTime();
            time.setDownAt(new Timestamp(System.currentTimeMillis()));
            time.setMonitorId(id);
            time.setRemarks(remarks);
            repo.insert(time);
            Notification notification = new Notification();
            Monitor monitor = new Monitor();
            monitor.setId(id);
            notification.setMonitor(monitor);
            notification.setRemark(remarks);
            notificationRepo.insertIntoTable(notification);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Added The manual entry\"}");
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\",\"message\":\""+e.getMessage()+"\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("mid"));

        try{
            ResolveTime time = new ResolveTime();
            time.setUpAt(new Timestamp(System.currentTimeMillis()));
            time.setMonitorId(id);
            repo.update(time);
            Notification notification = new Notification();
            Monitor monitor = new Monitor();
            monitor.setId(id);
            notification.setMonitor(monitor);
            notification.setRemark("Back to online");
            notificationRepo.insertIntoTable(notification);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Added the Resolved time\"}");
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\",\"message\":\""+e.getMessage()+"\"}");
        }
    }
}
