package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.OperatorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class OperatorServlet extends HttpServlet {
    private OperatorService operatorService = new OperatorService();
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
        System.out.println(inputLine.toString());
        try{
            ObjectMapper mapper = new ObjectMapper();
            Monitor monitor = mapper.readValue(inputLine.toString(), Monitor.class);
            monitor.toString();
            operatorService.createMonitor(monitor);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\"}");
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        if(path.endsWith("/monitors$")){
            List<Monitor> monitors = operatorService.getAllMonitor();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print(new ObjectMapper().writeValueAsString(monitors));
        }else{
            int id = Integer.parseInt(req.getParameter("id"));
            Monitor monitor = operatorService.getMonitorById(id);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print(new ObjectMapper().writeValueAsString(monitor));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String interval = req.getParameter("interval");
        int isActive =-1;
        if(req.getParameter("isActive")!=null){
            isActive = Integer.parseInt(req.getParameter("isActive"));
        }


        int id = Integer.parseInt(req.getParameter("id"));
        if (status!=null && !status.equals("")) {
            operatorService.updateStatus(id,status);
        }else if(interval!=null && !interval.equals("")) {
            operatorService.updateInterval(id,interval);
        }else if(isActive>=0){
            boolean val = isActive == 1 ? true : false;
            operatorService.updateActive(id,val);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try{
            operatorService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\"}");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
