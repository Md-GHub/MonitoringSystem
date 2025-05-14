package com.md.monitoringsystem.model;

import java.sql.Date;
import java.sql.Timestamp;

public class PublicMonitorHistoryBuilder {
    private int monitorId;
    private String monitorName;
    private String url;
    private String statusExpected;
    private String interval;
    private boolean active;
    private int responseTime;
    private Date timeStamp;
    private String statusReceived;
    private Timestamp time;
    public String getRemark() {
        return remark;
    }

    public Timestamp getTime() {
        return time;
    }

    public PublicMonitorHistoryBuilder setTime(Timestamp time) {
        this.time = time;
        return this;
    }

    public PublicMonitorHistoryBuilder setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    private boolean success_status;
    private String remark;
    public PublicMonitorHistoryBuilder setMonitorId(int monitorId) {
        this.monitorId = monitorId;
        return this;
    }

    public PublicMonitorHistoryBuilder setMonitorName(String monitorName) {
        this.monitorName = monitorName;
        return this;
    }

    public PublicMonitorHistoryBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public PublicMonitorHistoryBuilder setStatusExpected(String statusExpected) {
        this.statusExpected = statusExpected;
        return this;
    }

    public PublicMonitorHistoryBuilder setInterval(String interval) {
        this.interval = interval;
        return this;
    }

    public PublicMonitorHistoryBuilder setActive(boolean active) {
        this.active = active;
        return this;
    }

    public PublicMonitorHistoryBuilder setResponseTime(int responseTime) {
        this.responseTime = responseTime;
        return this;
    }

    public PublicMonitorHistoryBuilder setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public PublicMonitorHistoryBuilder setStatusReceived(String statusReceived) {
        this.statusReceived = statusReceived;
        return this;
    }

    public PublicMonitorHistoryBuilder setSuccess_status(boolean success_status) {
        this.success_status = success_status;
        return this;
    }

    public int getMonitorId() {
        return monitorId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public String getUrl() {
        return url;
    }

    public String getStatusExpected() {
        return statusExpected;
    }

    public String getInterval() {
        return interval;
    }

    public boolean isActive() {
        return active;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getStatusReceived() {
        return statusReceived;
    }

    public boolean isSuccess_status() {
        return success_status;
    }

    public PublicMonitorHistory build() {
        return new PublicMonitorHistory(this);
    }
}
