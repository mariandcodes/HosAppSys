package com.red.purple.model;

import java.sql.Timestamp;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Timestamp scheduledDatetime;
    private String status;
    private String reason;
    private String preferredLanguage;
    private String specialNeeds;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Appointment() {}

    public Appointment(int appointmentId, int patientId, int doctorId, Timestamp scheduledDatetime,
                       String status, String reason, String preferredLanguage, String specialNeeds,
                       Timestamp createdAt, Timestamp updatedAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scheduledDatetime = scheduledDatetime;
        this.status = status;
        this.reason = reason;
        this.preferredLanguage = preferredLanguage;
        this.specialNeeds = specialNeeds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public Timestamp getScheduledDatetime() { return scheduledDatetime; }
    public void setScheduledDatetime(Timestamp scheduledDatetime) { this.scheduledDatetime = scheduledDatetime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public String getSpecialNeeds() { return specialNeeds; }
    public void setSpecialNeeds(String specialNeeds) { this.specialNeeds = specialNeeds; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
