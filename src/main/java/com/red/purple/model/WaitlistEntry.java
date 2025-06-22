/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.model;

import java.sql.Date;
import java.sql.Timestamp;

public class WaitlistEntry {
    private int waitlistId;
    private int patientId;
    private int doctorId;
    private Date requestedDate;
    private int position;
    private Timestamp createdAt;

    public WaitlistEntry() {}

    public WaitlistEntry(int waitlistId, int patientId, int doctorId,
                         Date requestedDate, int position, Timestamp createdAt) {
        this.waitlistId = waitlistId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.requestedDate = requestedDate;
        this.position = position;
        this.createdAt = createdAt;
    }

    public int getWaitlistId() { return waitlistId; }
    public void setWaitlistId(int waitlistId) { this.waitlistId = waitlistId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public Date getRequestedDate() { return requestedDate; }
    public void setRequestedDate(Date requestedDate) { this.requestedDate = requestedDate; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}

