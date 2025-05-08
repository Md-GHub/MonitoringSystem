package com.md.monitoringsystem.service;

import com.md.monitoringsystem.model.Monitor;
import com.md.monitoringsystem.repo.MonitorRepo;

import java.util.List;

public class OperatorService {
    private MonitorRepo monitorRepo = MonitorRepo.get();

    public void createMonitor(Monitor monitor) {
        if(monitor == null){
            throw new IllegalArgumentException("Monitor object cannot be null");
        }
        monitorRepo.addMonitor(monitor);
    }

    public Monitor getMonitorById(int id) {
        return monitorRepo.getMonitorById(id);
    }

    public List<Monitor> getAllMonitor() {
        return monitorRepo.getAllMonitors();
    }

    public void deleteById(int id) {
        monitorRepo.deleteMonitor(id);
    }


    public void updateStatus(int id,String status) {
        monitorRepo.updateStatus(id,status);
    }

    public void updateInterval(int id, String interval) {
        monitorRepo.setUpdateInterval(id, interval);
    }

    public void updateActive(int id, boolean isActive) {
        monitorRepo.setUpdateEnabled(id,isActive);
    }
}
