package com.md.monitoringsystem.monitorexecution;

import com.md.monitoringsystem.model.*;
import com.md.monitoringsystem.repo.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class Task implements Runnable {
    private Monitor monitor;
    private MonitorResult result;
    private MonitorResultRepo monitorResultRepo = com.md.monitoringsystem.repo.MonitorResultRepo.get();
    private MonitorCurrentStatusRepo currentStatusRepo = MonitorCurrentStatusRepo.get();
    private ResolveTImeRepo resolveTImeRepo = new ResolveTImeRepo();
    private static List<Integer> tracks = new ArrayList<>();
    private  NotificationRepo notificationRepo = NotificationRepo.get();
    long startTime;
    long endTime;
    MonitorCurrentStatus mcs;
    public Task(Monitor monitor) {
        this.monitor = monitor;
        result = new MonitorResult();
        result.setResultId(monitor.getId());
    }

    @Override
    public void run() {
        try{
            startTime = System.currentTimeMillis();
            String urlString="http://"+monitor.getUrl();
            URL url=new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10);
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println(monitor.getUrl()+" - Status Code: " + statusCode + " "+new Date());
            connection.disconnect();
            endTime = System.currentTimeMillis();

            boolean successStatus = (monitor.getStatus().equals(statusCode+""));
            check(successStatus);
            mcs = new MonitorCurrentStatus();
            mcs.setStatus(successStatus);
            mcs.setMonitor(monitor);


            result.setResponseTime((int) (endTime-startTime));
            result.setStatus(statusCode+"");
            result.setSuccess(successStatus);
            result.setTimestamp(new java.sql.Date(System.currentTimeMillis()));

        }catch(Exception e){
            check(false,e.getMessage());
            endTime = System.currentTimeMillis();
            result.setResponseTime((int) (endTime-startTime));
            result.setStatus(500+"");
            result.setSuccess(false);
            result.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            System.out.println(e.getMessage());
            mcs = new MonitorCurrentStatus();
            mcs.setStatus(false);
            mcs.setMonitor(monitor);
        }finally {
            monitorResultRepo.insert(result); // frequent entry
            currentStatusRepo.updateMonitorCurrentStatus(mcs);
        }
    }


    private void check(boolean successStatus){
        if(!successStatus){
            monitor.setTempCount(monitor.getTempCount()+1);
        }else{
            monitor.setTempCount(0);
        }
        System.out.println(tracks.size());
        if(!successStatus && !tracks.contains(monitor.getId()) && monitor.getNoOfFails() == monitor.getTempCount()){
            // notify and hold the monitor
            Notification notification = new Notification();
            notification.setMonitor(monitor);
            notification.setRemark(monitor.getMonitorName()+" currently in down");
            notificationRepo.insertIntoTable(notification);
            // add to set
            tracks.add(monitor.getId());
            ResolveTime time = new ResolveTime();
            time.setMonitorId(monitor.getId());
            time.setDownAt(new Timestamp(System.currentTimeMillis()));
            time.setRemarks("Automated entry : "+monitor.getMonitorName()+" currently in down (expected status error)");
            resolveTImeRepo.insert(time);
            System.out.println("notification sent");
        }else if(successStatus && tracks.contains(monitor.getId()) && monitor.getNoOfFails() != monitor.getTempCount()){
            // notify
            // add to db
            Notification notification = new Notification();
            notification.setMonitor(monitor);
            notification.setRemark(monitor.getMonitorName()+" back to work");
            notificationRepo.insertIntoTable(notification);
            ResolveTime time = new ResolveTime();
            time.setMonitorId(monitor.getId());
            time.setUpAt(new Timestamp(System.currentTimeMillis()));
            resolveTImeRepo.update(time);
            // remove from set
            for(int i=0;i<tracks.size();i++){
                if(tracks.get(i)==monitor.getId()){
                    tracks.remove(i);
                }
            }
        }
    }

    private void check(boolean successStatus,String message){
        if(!successStatus){
            monitor.setTempCount(monitor.getTempCount()+1);
        }else{
            monitor.setTempCount(0);
        }
        if(!successStatus && !tracks.contains(monitor.getId()) && monitor.getNoOfFails() == monitor.getTempCount()){
            // notify and hold the monitor
            Notification notification = new Notification();
            notification.setMonitor(monitor);
            notification.setRemark(monitor.getMonitorName()+" currently in down :"+message);
            notificationRepo.insertIntoTable(notification);
            // add to set
            tracks.add(monitor.getId());
            ResolveTime time = new ResolveTime();
            time.setMonitorId(monitor.getId());
            time.setDownAt(new Timestamp(System.currentTimeMillis()));
            time.setRemarks("Automated entry : "+monitor.getMonitorName()+" currently in down");
            resolveTImeRepo.insert(time);
            System.out.println(tracks.size());
            System.out.println("notification sent");
        }else if(successStatus && tracks.contains(monitor.getId()) && monitor.getTempCount() == 0){
            // notify
            // add to db
            Notification notification = new Notification();
            notification.setMonitor(monitor);
            notification.setRemark("Resolved : "+monitor.getMonitorName()+" back to work");
            notificationRepo.insertIntoTable(notification);
            ResolveTime time = new ResolveTime();
            time.setMonitorId(monitor.getId());
            time.setUpAt(new Timestamp(System.currentTimeMillis()));
            resolveTImeRepo.update(time);
            // remove from set
            tracks.remove(monitor.getId());
        }
    }
}
