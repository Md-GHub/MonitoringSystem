package com.md.monitoringsystem.service;

import com.md.monitoringsystem.repo.MonitorRepo;

public class PublicService {
    private MonitorRepo monitorRepo = MonitorRepo.get();

    public int getUpTime(){
        int totalCount = monitorRepo.countOfMonitor();
        int uptimeReq = monitorRepo.countUptimReq();
        return (uptimeReq/totalCount)*100;
    }
}
