/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int patientId;
    private int appointmentId;
    private double amount;
    private String method;
    private String transactionId;
    private String status;
    private Timestamp paidAt;

    public Payment() {}

    public Payment(int paymentId, int patientId, int appointmentId, double amount,
                   String method, String transactionId, String status, Timestamp paidAt) {
        this.paymentId = paymentId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.method = method;
        this.transactionId = transactionId;
        this.status = status;
        this.paidAt = paidAt;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getPaidAt() { return paidAt; }
    public void setPaidAt(Timestamp paidAt) { this.paidAt = paidAt; }
}
