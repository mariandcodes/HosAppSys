/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.model;

import java.sql.Timestamp;

public class Invoice {
    private int invoiceId;
    private int paymentId;
    private double amount;
    private Timestamp issuedAt;
    private String invoicePdfPath;

    public Invoice() {}

    public Invoice(int invoiceId, int paymentId, double amount,
                   Timestamp issuedAt, String invoicePdfPath) {
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.issuedAt = issuedAt;
        this.invoicePdfPath = invoicePdfPath;
    }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Timestamp getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Timestamp issuedAt) { this.issuedAt = issuedAt; }
    public String getInvoicePdfPath() { return invoicePdfPath; }
    public void setInvoicePdfPath(String invoicePdfPath) { this.invoicePdfPath = invoicePdfPath; }
}

