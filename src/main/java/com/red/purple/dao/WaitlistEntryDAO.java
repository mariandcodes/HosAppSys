package com.red.purple.dao;

import com.red.purple.model.WaitlistEntry; // Correct import for the WaitlistEntry model

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaitlistEntryDAO {
    private final Connection connection;

    public WaitlistEntryDAO(Connection connection) {
        this.connection = connection;
    }

    public int addWaitlistEntry(WaitlistEntry entry) throws SQLException {
        String sql = "INSERT INTO WaitlistEntries (PatientID, DoctorID, requested_date, position, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entry.getPatientId());
            stmt.setInt(2, entry.getDoctorId());
            stmt.setDate(3, entry.getRequestedDate());
            stmt.setInt(4, entry.getPosition()); // Ensure position is set in the model or handled by DAO
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding waitlist entry failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Adding waitlist entry failed, no ID obtained.");
            }
        }
    }

    public WaitlistEntry getWaitlistEntryById(int waitlistId) throws SQLException {
        String sql = "SELECT WaitlistID, PatientID, DoctorID, requested_date, position, created_at FROM WaitlistEntries WHERE WaitlistID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, waitlistId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapWaitlistEntry(rs);
                }
            }
        }
        return null;
    }

    public void removeWaitlistEntry(int waitlistId) throws SQLException {
        String sql = "DELETE FROM WaitlistEntries WHERE WaitlistID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, waitlistId);
            stmt.executeUpdate();
        }
    }

    public List<WaitlistEntry> getWaitlistEntriesByDoctor(int doctorId) throws SQLException {
        String sql = "SELECT WaitlistID, PatientID, DoctorID, requested_date, position, created_at FROM WaitlistEntries WHERE DoctorID = ? ORDER BY requested_date ASC, created_at ASC";
        List<WaitlistEntry> entries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapWaitlistEntry(rs));
                }
            }
        }
        return entries;
    }

    public List<WaitlistEntry> getWaitlistEntriesByDate(Date date) throws SQLException {
        String sql = "SELECT WaitlistID, PatientID, DoctorID, requested_date, position, created_at FROM WaitlistEntries WHERE requested_date = ? ORDER BY created_at ASC";
        List<WaitlistEntry> entries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapWaitlistEntry(rs));
                }
            }
        }
        return entries;
    }

    private WaitlistEntry mapWaitlistEntry(ResultSet rs) throws SQLException {
        return new WaitlistEntry(
            rs.getInt("WaitlistID"),
            rs.getInt("PatientID"),
            rs.getInt("DoctorID"),
            rs.getDate("requested_date"),
            rs.getInt("position"),
            rs.getTimestamp("created_at")
        );
    }
}