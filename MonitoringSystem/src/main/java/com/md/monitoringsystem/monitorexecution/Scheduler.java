package com.md.monitoringsystem.monitorexecution;



import com.md.monitoringsystem.model.Monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Scheduler {
    private HashMap<Integer, List<Monitor>> monitors = new HashMap<>();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ThreadPool pool = new ThreadPool();
    public void serve(){
        service.scheduleAtFixedRate(() -> {
            for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
                int currentSeconds = (int) (System.currentTimeMillis() / 1000);
                int currentInterval = entry.getKey();
                if(currentSeconds%currentInterval == 0){
                    List<Monitor> batch = monitors.get(currentInterval);
                    for(Monitor monitor : batch){
                        if(!monitor.isActive()){
                            continue;
                        }
                        pool.submit(new Task(monitor));
                    }
                }
            }
        },0, 1L, TimeUnit.SECONDS);
    }

    public void addMonitor(Monitor monitor){
        if(!monitors.containsKey(monitor.getInterval())){
            List<Monitor> batch = new ArrayList<>();
            batch.add(monitor);
            monitors.put(Integer.parseInt(monitor.getInterval()), batch);
        }else{
            monitors.get(monitor.getInterval()).add(monitor);
        }
    }

    public HashMap<Integer, List<Monitor>> getMonitors() {
        return monitors;
    }

    public void setMonitors(HashMap<Integer, List<Monitor>> monitors) {
        this.monitors = monitors;
    }

    public void updateMonitorStatus(int id, String status) {
        for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
            List<Monitor> batch = entry.getValue();
            for(Monitor monitor : batch){
                if(monitor.getId() == id){
                    monitor.setStatus(status);
                    break;
                }
            }
        }
    }

    public void updateMonitorInterval(int id, String interval) {
        for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
            List<Monitor> batch = entry.getValue();
            for(Monitor monitor : batch){
                if(monitor.getId() == id){
                    monitor.setInterval(interval);
                    break;
                }
            }
        }
    }

    public void updateMonitorActive(int id, boolean Active) {
        for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
            List<Monitor> batch = entry.getValue();
            for(Monitor monitor : batch){
                if(monitor.getId() == id){
                    monitor.setActive(Active);
                    break;
                }
            }
        }
    }

    public void deleteMonitor(int id) {
        for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
            List<Monitor> batch = entry.getValue();
            for(Monitor monitor : batch){
                if(monitor.getId() == id){
                    batch.remove(monitor);
                    break;
                }
            }
        }
    }

    public void updateNoOfFails(int id, int noOfFails) {
        for(Map.Entry<Integer, List<Monitor>> entry : monitors.entrySet()){
            List<Monitor> batch = entry.getValue();
            for(Monitor monitor : batch){
                if(monitor.getId() == id){
                    monitor.setNoOfFails(noOfFails);
                    break;
                }
            }
        }
    }
}
