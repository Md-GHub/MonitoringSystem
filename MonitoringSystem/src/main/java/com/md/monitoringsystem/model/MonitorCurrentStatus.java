package com.md.monitoringsystem.model;

public class MonitorCurrentStatus {
    private Monitor monitor;
    private boolean status;

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
