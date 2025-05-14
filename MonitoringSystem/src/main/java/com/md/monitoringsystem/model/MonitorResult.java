package com.md.monitoringsystem.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MonitorResult {
    private int resultId;
    private int responseTime;
    private Date timestamp;
    private String status;
    private boolean isSuccess;
    private Timestamp time;
    public int getResultId() {
        return resultId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
