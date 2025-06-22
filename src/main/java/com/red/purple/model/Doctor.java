package com.red.purple.model;

import com.red.purple.model.User; // Ensure User class is imported if it's in a different package

public class Doctor {
    private int doctorId;
    private int userId;
    private User user; // This can be null if not loaded
    private String fullName;
    private String specialty;
    private String qualifications;
    private String contactInfo;
    private String workingHours;
    private double consultationFee;
    private String status;

    public Doctor() {}

    // Existing constructor (can remain if still used elsewhere)
    public Doctor(int doctorId, int userId, User user, String fullName, String specialty,
                  String qualifications, String contactInfo,
                  String workingHours, double consultationFee, String status) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.user = user;
        this.fullName = fullName;
        this.specialty = specialty;
        this.qualifications = qualifications;
        this.contactInfo = contactInfo;
        this.workingHours = workingHours;
        this.consultationFee = consultationFee;
        this.status = status;
    }

    // NEW CONSTRUCTOR for mapping directly from ResultSet (without User object initially)
    public Doctor(int doctorId, int userId, String fullName, String specialty,
                  String qualifications, String contactInfo,
                  String workingHours, double consultationFee, String status) {
        this.doctorId = doctorId;
        this.userId = userId;
        // this.user remains null or is set later if needed
        this.fullName = fullName;
        this.specialty = specialty;
        this.qualifications = qualifications;
        this.contactInfo = contactInfo;
        this.workingHours = workingHours;
        this.consultationFee = consultationFee;
        this.status = status;
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getUserId(); // sync userId
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // This method seems redundant as doctorId is already a property.
    // Consider removing it if not explicitly used, or ensure it's consistent.
    public int id() {
        return doctorId;
    }
}
