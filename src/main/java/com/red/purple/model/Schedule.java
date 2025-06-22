package com.red.purple.model;

import java.sql.Timestamp;

/**
 * Model class representing a doctor's schedule entry.
 * This can represent availability blocks, meetings, breaks, etc.
 * Separate from Appointments which involve patients.
 */
public class Schedule {
    private int id;
    private int doctorId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String scheduleType; // AVAILABILITY, MEETING, BREAK, CONFERENCE, etc.
    private String title;
    private String description;
    private String location;
    private String status; // ACTIVE, CANCELLED, COMPLETED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Schedule() {}

    public Schedule(int doctorId, Timestamp startTime, Timestamp endTime, String scheduleType) {
        this.doctorId = doctorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scheduleType = scheduleType;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", scheduleType='" + scheduleType + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}