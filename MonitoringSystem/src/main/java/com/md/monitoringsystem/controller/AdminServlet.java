package com.md.monitoringsystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class AdminServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /// read the json (username password)
        BufferedReader reader = req.getReader();
        StringBuilder inputLine = new StringBuilder();
        while(reader.ready()){
            inputLine.append(reader.readLine());
        }

        try{
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(inputLine.toString(), User.class);
            user.toString();
            String invitationUrl = userService.createUser(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"invitationUrl\":\"" + invitationUrl + "\"}");
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\"}");
        }

    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        try{
            userService.deleteUser(id);
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"User Deleted Successfully\"}");
        }catch (Exception e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"cant delete user\"}");
        }
    }
}
