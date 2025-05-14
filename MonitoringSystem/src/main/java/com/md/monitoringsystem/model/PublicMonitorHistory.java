package com.md.monitoringsystem.model;

import java.sql.Date;
import java.sql.Timestamp;

public class PublicMonitorHistory {
    private int monitorId;
    private String monitorName;
    private String url;
    private boolean active;
    private boolean success_status;
    private String remark;
    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;

    }

    public PublicMonitorHistory(PublicMonitorHistoryBuilder p) {
        this.monitorId = p.getMonitorId();
        this.monitorName = p.getMonitorName();
        this.url = p.getUrl();
        this.active = p.isActive();
        this.success_status = p.isSuccess_status();
        this.remark = p.getRemark();
        this.time = p.getTime();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSuccess_status() {
        return success_status;
    }

    public void setSuccess_status(boolean success_status) {
        this.success_status = success_status;
    }
}
