package com.md.monitoringsystem.service;

import com.md.monitoringsystem.model.MonitorHistory;
import com.md.monitoringsystem.model.NotificationDisplay;
import com.md.monitoringsystem.model.PublicUpDownTime;
import com.md.monitoringsystem.repo.MonitorRepo;
import com.md.monitoringsystem.repo.MonitorResultRepo;
import com.md.monitoringsystem.repo.NotificationRepo;

import java.util.List;

public class PublicService {
    private MonitorRepo monitorRepo = MonitorRepo.get();
    private MonitorResultRepo monitorResultRepo = MonitorResultRepo.get();
    private NotificationRepo notificationRepo = NotificationRepo.get();
    public List<PublicUpDownTime> getAllPublicUpDownTimes(String timeLine,int offset) {
        String parts[] = timeLine.split("_");
        return monitorRepo.getUptimeAndDowntime(parts[0]+" "+parts[1], offset);
    }

    public List<MonitorHistory> getAllMonitorResult(int offset) {

        return monitorResultRepo.getAllMonitorResult(offset);
    }

    public List<NotificationDisplay> getAllNotification() {
        return notificationRepo.getAllNotifications();
    }
}
