package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.model.MonitorResult;
import com.md.monitoringsystem.model.PublicMonitorHistory;
import com.md.monitoringsystem.model.PublicUpDownTime;
import com.md.monitoringsystem.service.PublicService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PublicServlet extends HttpServlet {
    private PublicService publicService = new PublicService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int offset = (req.getParameter("offset")==null)?0:Integer.parseInt(req.getParameter("offset").toString());
        String timeLine = (req.getParameter("timeline")==null) ? null : req.getParameter("timeline").toString();
        String url = req.getRequestURI();
        boolean isGetHistory = url.contains("history");
        try{
            if(!isGetHistory){
                List<PublicUpDownTime> result = publicService.getAllPublicUpDownTimes(timeLine, offset);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(result);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
                resp.setStatus(200);
            }else{
                List<PublicMonitorHistory> results = publicService.getAllMonitorResult(offset);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(results);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
                resp.setStatus(200);
                System.out.println(results.get(0).getTime());
            }
        }catch (Exception e){

            System.out.println(e.getMessage());
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }
}
