package com.md.monitoringsystem.service;

import com.md.monitoringsystem.exception.NoMonitorFounded;
import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.model.MonitorAudit;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.repo.*;

import java.util.List;

public class OperatorService {
    private MonitorRepo monitorRepo = MonitorRepo.get();
    private MonitorAuditRepo auditRepo = MonitorAuditRepo.get();
    private OrgMonitorRepo orgMonitorRepo = OrgMonitorRepo.get();
    private UserRepo userRepo = UserRepo.get();
    public void createMonitor(Monitor monitor, User user) throws NoMonitorFounded {
        if(monitor == null){
            throw new IllegalArgumentException("Monitor object cannot be null");
        }
        int monitorId = monitorRepo.addMonitor(monitor);
        monitor.setId(monitorId);
        MonitorAudit monitorAudit = new MonitorAudit();
        monitorAudit.setUser(user);
        monitorAudit.setMonitor(monitor);
        monitorAudit.setRemark("CREATED");
        auditRepo.insert(monitorAudit);
        int orgId = userRepo.getOrgIdByUserId(user.getUserId());
        orgMonitorRepo.add(monitorId,orgId);
    }

    public Monitor getMonitorById(int id) {
        Monitor monitor = monitorRepo.getMonitorById(id);
        if(monitor == null){
            throw new NoMonitorFounded("No Monitor Found");
        }
        return monitor;
    }

    public List<Monitor> getAllMonitor() {
        return monitorRepo.getAllMonitors();
    }

    public void deleteById(int id,User user) throws NoMonitorFounded {

        Monitor monitor = new Monitor();
        monitor.setId(id);
        monitorRepo.addMonitor(monitor);
        MonitorAudit monitorAudit = new MonitorAudit();
        monitorAudit.setUser(user);
        monitorAudit.setMonitor(monitor);
        monitorAudit.setRemark("DELETED");
        auditRepo.insert(monitorAudit);
        monitorRepo.deleteMonitor(id);
    }


    public void updateStatus(int id,String status,User user) throws NoMonitorFounded {
        Monitor monitorFromDb = monitorRepo.getMonitorById(id);
        monitorRepo.updateStatus(id,status);

        Monitor monitor = new Monitor();
        monitor.setId(id);
        monitorRepo.addMonitor(monitor);
        MonitorAudit monitorAudit = new MonitorAudit();
        monitorAudit.setUser(user);
        monitorAudit.setMonitor(monitor);
        monitorAudit.setRemark("UPDATED - status from "+monitorFromDb.getStatus()+" to "+status);
        auditRepo.insert(monitorAudit);
    }

    public void updateInterval(int id, String interval,User user) throws NoMonitorFounded {
        Monitor monitorFromDb = monitorRepo.getMonitorById(id);
        monitorRepo.setUpdateInterval(id, interval);

        Monitor monitor = new Monitor();
        monitor.setId(id);
        monitorRepo.addMonitor(monitor);
        MonitorAudit monitorAudit = new MonitorAudit();
        monitorAudit.setUser(user);
        monitorAudit.setMonitor(monitor);
        monitorAudit.setRemark("UPDATED - interval from "+monitorFromDb.getInterval()+" to "+interval);
        auditRepo.insert(monitorAudit);
    }

    public void updateActive(int id, boolean isActive,User user) throws NoMonitorFounded {
        Monitor monitorFromDb = monitorRepo.getMonitorById(id);
        monitorRepo.setUpdateEnabled(id,isActive);

        Monitor monitor = new Monitor();
        monitor.setId(id);
        monitorRepo.addMonitor(monitor);
        MonitorAudit monitorAudit = new MonitorAudit();
        monitorAudit.setUser(user);
        monitorAudit.setMonitor(monitor);
        monitorAudit.setRemark("UPDATED - active from "+monitorFromDb.isActive()+" to "+isActive);
        auditRepo.insert(monitorAudit);
    }
}
