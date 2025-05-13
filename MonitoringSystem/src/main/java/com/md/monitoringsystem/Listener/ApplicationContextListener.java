package com.md.monitoringsystem.Listener;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.monitorexecution.Scheduler;
import com.md.monitoringsystem.repo.MonitorRepo;
import com.md.monitoringsystem.utils.PostgresConnections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private MonitorRepo monitorRepo = MonitorRepo.get();
    public void contextInitialized(ServletContextEvent sce) {
        Scheduler scheduler = new Scheduler();
        sce.getServletContext().setAttribute("scheduler", scheduler);
        scheduler.serve();
        System.out.println("Application started");
        PostgresConnections.createConnection();

        /// load monitors :
        List<Integer> intervals = monitorRepo.loadIntervals();
        for (Integer interval : intervals) {
            List<Monitor> batch = monitorRepo.getAllMonitorByIntervals(interval);
            scheduler.getMonitors().put(interval, batch);
        }
    }
}
