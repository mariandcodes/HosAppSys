package com.red.purple.dao;

import com.red.purple.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final Connection connection;

    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    public int createAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO Appointments (PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, appointment.getScheduledDatetime());
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getReason());
            stmt.setString(6, appointment.getPreferredLanguage());
            stmt.setString(7, appointment.getSpecialNeeds());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating appointment failed, no ID obtained.");
            }
        }
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException {
        String sql = "SELECT AppointmentID, PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs, created_at, updated_at FROM Appointments WHERE AppointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAppointment(rs);
                }
            }
        }
        return null;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        String sql = "SELECT AppointmentID, PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs, created_at, updated_at FROM Appointments";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE Appointments SET PatientID = ?, DoctorID = ?, scheduled_datetime = ?, status = ?, reason = ?, preferred_language = ?, special_needs = ?, updated_at = CURRENT_TIMESTAMP WHERE AppointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, appointment.getScheduledDatetime());
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getReason());
            stmt.setString(6, appointment.getPreferredLanguage());
            stmt.setString(7, appointment.getSpecialNeeds());
            stmt.setInt(8, appointment.getAppointmentId());
            stmt.executeUpdate();
        }
    }

    public void updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE Appointments SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE AppointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    public void deleteAppointment(int appointmentId) throws SQLException { // Renamed from cancelAppointment
        String sql = "DELETE FROM Appointments WHERE AppointmentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        String sql = "SELECT AppointmentID, PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs, created_at, updated_at FROM Appointments WHERE PatientID = ?";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) throws SQLException {
        String sql = "SELECT AppointmentID, PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs, created_at, updated_at FROM Appointments WHERE DoctorID = ?";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
            rs.getInt("AppointmentID"),
            rs.getInt("PatientID"),
            rs.getInt("DoctorID"),
            rs.getTimestamp("scheduled_datetime"),
            rs.getString("status"),
            rs.getString("reason"),
            rs.getString("preferred_language"),
            rs.getString("special_needs"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}