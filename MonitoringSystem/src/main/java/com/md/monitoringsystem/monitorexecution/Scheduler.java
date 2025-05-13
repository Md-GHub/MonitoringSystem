package com.md.monitoringsystem.monitorexecution;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler implements Runnable {
    private Map<Integer, List<Monitor>> monitors = new HashMap<Integer, List<Monitor>>();



    public void addMonitor(Monitor monitor) {
        int interval = Integer.parseInt(monitor.getInterval());
        monitors.put(interval , monitors.getOrDefault(interval,new ArrayList<>()));
        monitors.get(interval).add(monitor);
    }

    public Map<Integer, List<Monitor>> getMonitors() {
        return monitors;
    }

    public void setMonitors(Map<Integer, List<Monitor>> monitors) {
        this.monitors = monitors;
    }

    @Override
    public void run() {
        while (true) {
            for (Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()) {
                int interval = entry.getKey();
                List<Monitor> monitors = entry.getValue();
                long lastUpdate = monitors.get(0).getLastUpdate().getTime();
                long now = new Timestamp(System.currentTimeMillis()).getTime();
                long diff = new Timestamp(interval*60*1000).getTime(); // in min
                System.out.println(now - lastUpdate +" "+ diff);

                for (Monitor monitor : monitors) {
                    monitor.setLastUpdate(new Timestamp(now));
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
