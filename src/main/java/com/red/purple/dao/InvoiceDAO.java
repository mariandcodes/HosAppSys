package com.red.purple.dao;

import com.red.purple.model.Invoice; // Correct import for the Invoice model

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private final Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }

    public int createInvoice(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO Invoices (PaymentID, amount, issued_at, invoice_pdf_path) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, invoice.getPaymentId());
            stmt.setDouble(2, invoice.getAmount());
            stmt.setTimestamp(3, invoice.getIssuedAt());
            stmt.setString(4, invoice.getInvoicePdfPath());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating invoice failed, no ID obtained.");
            }
        }
    }

    public Invoice getInvoiceById(int invoiceId) throws SQLException {
        String sql = "SELECT InvoiceID, PaymentID, amount, issued_at, invoice_pdf_path FROM Invoices WHERE InvoiceID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapInvoice(rs);
                }
            }
        }
        return null;
    }

    public List<Invoice> getInvoicesByPaymentId(int paymentId) throws SQLException {
        String sql = "SELECT InvoiceID, PaymentID, amount, issued_at, invoice_pdf_path FROM Invoices WHERE PaymentID = ?";
        List<Invoice> invoices = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapInvoice(rs));
                }
            }
        }
        return invoices;
    }

    private Invoice mapInvoice(ResultSet rs) throws SQLException {
        return new Invoice(
            rs.getInt("InvoiceID"),
            rs.getInt("PaymentID"),
            rs.getDouble("amount"),
            rs.getTimestamp("issued_at"),
            rs.getString("invoice_pdf_path")
        );
    }
}