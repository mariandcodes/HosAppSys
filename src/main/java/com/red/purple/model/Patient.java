/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.model;

import java.sql.Date;

public class Patient {
    private int patientId;
    private int userId;
    private String fullName;
    private Date dob;
    private String gender;
    private String contactInfo;
    private String address;
    private String emergencyContact;
    private String insuranceProvider;
    private String insurancePolicy;
    private String status;

    public Patient() {}

    public Patient(int patientId, int userId, String fullName, Date dob, String gender,
                   String contactInfo, String address, String emergencyContact,
                   String insuranceProvider, String insurancePolicy, String status) {
        this.patientId = patientId;
        this.userId = userId;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.insuranceProvider = insuranceProvider;
        this.insurancePolicy = insurancePolicy;
        this.status = status;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    public String getInsurancePolicy() { return insurancePolicy; }
    public void setInsurancePolicy(String insurancePolicy) { this.insurancePolicy = insurancePolicy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
