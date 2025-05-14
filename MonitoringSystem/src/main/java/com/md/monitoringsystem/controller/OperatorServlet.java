package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.exception.NoMonitorFounded;
import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.monitorexecution.Scheduler;
import com.md.monitoringsystem.service.OperatorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class OperatorServlet extends HttpServlet {
    private OperatorService operatorService = new OperatorService();
    private Logger logger = Logger.getLogger(OperatorServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder inputLine = new StringBuilder();
        while(reader.ready()){
            inputLine.append(reader.readLine());
        }
        if(inputLine.toString().equals("")){
            throw new RuntimeException("Bad Request");
        }
        User user = (User) req.getAttribute("user");
        Scheduler scheduler = (Scheduler) req.getServletContext().getAttribute("scheduler");
        try{
            ObjectMapper mapper = new ObjectMapper();
            Monitor monitor = mapper.readValue(inputLine.toString(), Monitor.class);
            monitor.toString();
            operatorService.createMonitor(monitor,user);
            scheduler.addMonitor(monitor);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\"}");
            logger.info(new Date() +" Monitor Added - "+monitor.toString());
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\"}");
            logger.info(new Date() +" Monitor Not Added - "+e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        try{
            if(path.endsWith("/monitors") && req.getParameter("mid")!=null){
                int id = Integer.parseInt(req.getParameter("mid"));
                Monitor monitor = operatorService.getMonitorById(id);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().print(new ObjectMapper().writeValueAsString(monitor));
            }else{
                List<Monitor> monitors = operatorService.getAllMonitor();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().print(new ObjectMapper().writeValueAsString(monitors));

            }
        }catch (NoMonitorFounded e){
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\""+e.getMessage()+"\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String interval = req.getParameter("interval");
        String noOfFails = req.getParameter("nooffails");
        int isActive =-1;
        if(req.getParameter("isActive")!=null){
            isActive = Integer.parseInt(req.getParameter("isActive"));
        }
        User user = (User) req.getAttribute("user");
        Scheduler scheduler = (Scheduler) req.getServletContext().getAttribute("scheduler");

        try{
            int id = Integer.parseInt(req.getParameter("id"));
            if (status!=null && !status.equals("")) {
                operatorService.updateStatus(id,status,user);
                scheduler.updateMonitorStatus(id,status);
            }else if(interval!=null && !interval.equals("")) {
                operatorService.updateInterval(id,interval,user);
                scheduler.updateMonitorInterval(id,interval);
            }else if(isActive>=0){
                boolean val = (isActive == 1)? true : false;
                operatorService.updateActive(id,val,user);
                scheduler.updateMonitorActive(id,val);
            }else if(noOfFails != null){
                int n = Integer.parseInt(noOfFails);
                operatorService.updateNoOfFails(id,n,user);
                scheduler.updateNoOfFails(id,n);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print(new ObjectMapper().writeValueAsString(operatorService.getMonitorById(id)));
            logger.info(new Date() +" Monitor Updated - "+operatorService.getMonitorById(id));
        }catch (NoMonitorFounded e) {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"" + e.getMessage() + "\"}");
            logger.info(new Date() + " Monitor Not Updated - " + e.getMessage());
        }catch (Exception e){
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"" + e.getMessage() + "\"}");
            logger.info(new Date() + " Monitor Not Updated - " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User user = (User) req.getAttribute("user");
        Scheduler scheduler = (Scheduler) req.getServletContext().getAttribute("scheduler");
        try{
            operatorService.deleteById(id,user);
            scheduler.deleteMonitor(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\"}");
            logger.info(new Date() +" Monitor Deleted");
        }catch (NoMonitorFounded e){
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Date() +" Monitor Not Deleted - "+e.getMessage());
        }
    }

}
