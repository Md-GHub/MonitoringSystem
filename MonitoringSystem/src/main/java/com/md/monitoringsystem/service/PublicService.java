package com.md.monitoringsystem.service;

import com.md.monitoringsystem.model.MonitorResult;
import com.md.monitoringsystem.model.PublicMonitorHistory;
import com.md.monitoringsystem.model.PublicUpDownTime;
import com.md.monitoringsystem.repo.MonitorRepo;
import com.md.monitoringsystem.repo.MonitorResultRepo;

import java.util.List;

public class PublicService {
    private MonitorRepo monitorRepo = MonitorRepo.get();
    private MonitorResultRepo monitorResultRepo = MonitorResultRepo.get();

    public List<PublicUpDownTime> getAllPublicUpDownTimes(String timeLine,int offset) {
        String parts[] = timeLine.split("_");
        return monitorRepo.getUptimeAndDowntime(parts[0]+" "+parts[1], offset);
    }

    public List<PublicMonitorHistory> getAllMonitorResult(int offset) {

        return monitorResultRepo.getAllMonitorResult(offset);
    }
}
