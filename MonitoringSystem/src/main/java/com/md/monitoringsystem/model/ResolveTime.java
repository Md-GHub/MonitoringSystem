package com.md.monitoringsystem.model;

import java.sql.Timestamp;

public class ResolveTime {
    private int monitorId;
    private Timestamp downAt;
    private Timestamp upAt;
    private String Remarks;
    public int getMonitorId() {
        return monitorId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public void setMonitorId(int monitorId) {
        this.monitorId = monitorId;
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
}
