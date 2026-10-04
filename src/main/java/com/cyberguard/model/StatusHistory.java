package com.cyberguard.model;

import java.sql.Timestamp;

public class StatusHistory {
    private String oldStatus;
    private String newStatus;
    private String updatedBy;
    private Timestamp updatedAt;

    public StatusHistory(String oldStatus, String newStatus, String updatedBy, Timestamp updatedAt) {
        this.oldStatus = oldStatus; this.newStatus = newStatus; this.updatedBy = updatedBy; this.updatedAt = updatedAt;
    }
    public String getOldStatus() { return oldStatus; }
    public String getNewStatus() { return newStatus; }
    public String getUpdatedBy() { return updatedBy; }
    public Timestamp getUpdatedAt() { return updatedAt; }
}
