package com.md.monitoringsystem.model;

import java.sql.Timestamp;

public class MonitorHistory {
    private int monitorId;
    private String monitorName;
    private Timestamp downAt;
    private Timestamp upAt;
    private String remarks;

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

    public Timestamp getDownAt() {
        return downAt;
    }

    public void setDownAt(Timestamp downAt) {
        this.downAt = downAt;
    }

    public Timestamp getUpAt() {
        return upAt;
    }

    public void setUpAt(Timestamp upAt) {
        this.upAt = upAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
