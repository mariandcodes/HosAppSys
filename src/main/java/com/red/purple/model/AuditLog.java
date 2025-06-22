/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Matthew
 */
package com.red.purple.model;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private int userId;
    private String actionType;
    private String resourceType;
    private Integer resourceId;
    private Timestamp timestamp;
    private String ipAddress;

    public AuditLog() {}

    public AuditLog(int logId, int userId, String actionType, String resourceType,
                    Integer resourceId, Timestamp timestamp, String ipAddress) {
        this.logId = logId;
        this.userId = userId;
        this.actionType = actionType;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public Integer getResourceId() { return resourceId; }
    public void setResourceId(Integer resourceId) { this.resourceId = resourceId; }
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}

