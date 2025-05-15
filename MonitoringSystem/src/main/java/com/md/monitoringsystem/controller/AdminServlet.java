package com.md.monitoringsystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.exception.ErrorInCreatingUser;
import com.md.monitoringsystem.exception.NoUserFounded;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.UserService;
import com.md.monitoringsystem.utils.LoggerUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.*;

public class AdminServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Logger logger = LoggerUtils.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /// read the json (username password)
        BufferedReader reader = req.getReader();
        StringBuilder inputLine = new StringBuilder();
        while(reader.ready()){
            inputLine.append(reader.readLine());
        }
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(inputLine.toString(), User.class);
        user.toString();

        try{

            int userId = userService.createUser(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"invitationUrl\":\"" + "http://localhost:8080/MonitoringSystem_war_exploded/user/activate?id="+userId+ "\"}");
        }catch(ErrorInCreatingUser e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Timestamp(System.currentTimeMillis())+" "+e.getMessage());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Timestamp(System.currentTimeMillis())+" "+e.getMessage());
        }

    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
        String email = req.getParameter("email");
        try{
//            userService.deleteUser(id);
            userService.deleteUser(email);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"User Deleted Successfully\"}");
//            resp.setStatus(SC_NO_CONTENT);
            resp.setStatus(SC_OK);
        }catch (NoUserFounded e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Timestamp(System.currentTimeMillis())+" "+e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Timestamp(System.currentTimeMillis())+" "+e.getMessage());
        }
    }

}
