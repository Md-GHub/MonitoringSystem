package com.md.monitoringsystem.Listener;

import com.md.monitoringsystem.utils.PostgresConnections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        PostgresConnections connections = new PostgresConnections();
        sce.getServletContext().setAttribute("connections", connections);
        System.out.println("Context initialized");
    }
}
