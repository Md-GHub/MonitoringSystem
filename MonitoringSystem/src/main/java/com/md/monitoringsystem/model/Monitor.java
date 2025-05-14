package com.md.monitoringsystem.model;


import java.util.Date;

public class Monitor {
    private int id;
    private String monitorName;
    private String url;
    private String status;
    private String interval;
    private int noOfFails = 3;
    private boolean isActive = true;
    private int tempCount = 0;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getTempCount() {
        return tempCount;
    }

    public void setTempCount(int tempCount) {
        this.tempCount = tempCount;
    }

    public int getNoOfFails() {
        return noOfFails;
    }

    public void setNoOfFails(int noOfFails) {
        this.noOfFails = noOfFails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
