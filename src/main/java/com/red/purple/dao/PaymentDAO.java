package com.red.purple.dao;

import com.red.purple.model.Payment; // Correct import for the Payment model

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private final Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public int createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payments (PatientID, AppointmentID, amount, method, transaction_id, status, paid_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getPatientId());
            stmt.setInt(2, payment.getAppointmentId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getMethod());
            stmt.setString(5, payment.getTransactionId());
            stmt.setString(6, payment.getStatus());
            stmt.setTimestamp(7, payment.getPaidAt());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating payment failed, no ID obtained.");
            }
        }
    }

    public Payment getPaymentById(int paymentId) throws SQLException {
        String sql = "SELECT PaymentID, PatientID, AppointmentID, amount, method, transaction_id, status, paid_at FROM Payments WHERE PaymentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        }
        return null;
    }

    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE Payments SET PatientID = ?, AppointmentID = ?, amount = ?, method = ?, transaction_id = ?, status = ?, paid_at = ? WHERE PaymentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, payment.getPatientId());
            stmt.setInt(2, payment.getAppointmentId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getMethod());
            stmt.setString(5, payment.getTransactionId());
            stmt.setString(6, payment.getStatus());
            stmt.setTimestamp(7, payment.getPaidAt());
            stmt.setInt(8, payment.getPaymentId());
            stmt.executeUpdate();
        }
    }

    public void updatePaymentStatus(int paymentId, String status) throws SQLException {
        String sql = "UPDATE Payments SET status = ? WHERE PaymentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            stmt.executeUpdate();
        }
    }

    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        }
    }

    public List<Payment> getPaymentsByPatient(int patientId) throws SQLException {
        String sql = "SELECT PaymentID, PatientID, AppointmentID, amount, method, transaction_id, status, paid_at FROM Payments WHERE PatientID = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        }
        return payments;
    }

    public List<Payment> getPaymentsByAppointment(int appointmentId) throws SQLException {
        String sql = "SELECT PaymentID, PatientID, AppointmentID, amount, method, transaction_id, status, paid_at FROM Payments WHERE AppointmentID = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        }
        return payments;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("PaymentID"),
            rs.getInt("PatientID"),
            rs.getInt("AppointmentID"),
            rs.getDouble("amount"),
            rs.getString("method"),
            rs.getString("transaction_id"),
            rs.getString("status"),
            rs.getTimestamp("paid_at")
        );
    }
}