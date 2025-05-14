package com.md.monitoringsystem.model;

public class PublicUpDownTime {
    private int monitorId;
    private String monitorName;
    private int upCount;
    private int downCount;
    private boolean status;

    public int getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(int monitorId) {
        this.monitorId = monitorId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
