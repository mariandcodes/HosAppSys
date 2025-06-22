package com.red.purple.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * ReportDAO provides methods to generate various reports such as
 * appointment summaries, doctor utilization, no-show analysis, and revenue.
 */
public class ReportDAO {
    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    // Example report: total appointments scheduled, completed, cancelled, no-shows per day
    public List<Map<String, Object>> getAppointmentSummary() throws SQLException {
        String sql = "SELECT CAST(scheduled_datetime AS DATE) as day, " +
                     "SUM(CASE WHEN status = 'Scheduled' THEN 1 ELSE 0 END) AS scheduled_count, " +
                     "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) AS completed_count, " +
                     "SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) AS cancelled_count, " +
                     "SUM(CASE WHEN status = 'NoShow' THEN 1 ELSE 0 END) AS no_show_count " +
                     "FROM Appointments GROUP BY CAST(scheduled_datetime AS DATE) ORDER BY day DESC";
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("day", rs.getDate("day"));
                row.put("scheduledCount", rs.getInt("scheduled_count"));
                row.put("completedCount", rs.getInt("completed_count"));
                row.put("cancelledCount", rs.getInt("cancelled_count"));
                row.put("noShowCount", rs.getInt("no_show_count"));
                results.add(row);
            }
        }
        return results;
    }

    // Example report: doctor utilization - % of slots filled vs available
    public List<Map<String, Object>> getDoctorUtilization() throws SQLException {
        String sql = "SELECT d.DoctorID, d.full_name, " +
                     "COUNT(a.AppointmentID) AS appointments_count, " +
                     "(SELECT SUM(TIMESTAMPDIFF(MINUTE, start_time, end_time) / slot_duration_minutes) " +
                     " FROM DoctorSchedules WHERE DoctorID = d.DoctorID) AS total_slots " +
                     "FROM Doctors d LEFT JOIN Appointments a ON d.DoctorID = a.DoctorID " +
                     "GROUP BY d.DoctorID, d.full_name";
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("doctorId", rs.getInt("DoctorID"));
                row.put("doctorName", rs.getString("full_name"));
                row.put("appointmentsCount", rs.getInt("appointments_count"));
                row.put("totalSlots", rs.getInt("total_slots"));
                if (rs.getInt("total_slots") != 0) {
                    row.put("utilizationPercent", rs.getInt("appointments_count") * 100.0 / rs.getInt("total_slots"));
                } else {
                    row.put("utilizationPercent", 0);
                }
                results.add(row);
            }
        }
        return results;
    }

    // Example report: no-show rate by doctor
    public List<Map<String, Object>> getNoShowAnalysis() throws SQLException {
        String sql = "SELECT d.DoctorID, d.full_name, " +
                     "COUNT(a.AppointmentID) AS total_appointments, " +
                     "SUM(CASE WHEN a.status = 'NoShow' THEN 1 ELSE 0 END) AS no_show_count, " +
                     "CASE WHEN COUNT(a.AppointmentID) > 0 THEN " +
                     "  SUM(CASE WHEN a.status = 'NoShow' THEN 1 ELSE 0 END) * 100.0 / COUNT(a.AppointmentID) " +
                     "ELSE 0 END AS no_show_rate " +
                     "FROM Doctors d LEFT JOIN Appointments a ON d.DoctorID = a.DoctorID " +
                     "GROUP BY d.DoctorID, d.full_name";
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("doctorId", rs.getInt("DoctorID"));
                row.put("doctorName", rs.getString("full_name"));
                row.put("totalAppointments", rs.getInt("total_appointments"));
                row.put("noShowCount", rs.getInt("no_show_count"));
                row.put("noShowRate", rs.getDouble("no_show_rate"));
                results.add(row);
            }
        }
        return results;
    }

    // Example report: revenue per doctor per period
    public List<Map<String, Object>> getRevenueReports() throws SQLException {
        String sql = "SELECT d.DoctorID, d.full_name, " +
                     "SUM(p.amount) AS totalRevenue, " +
                     "COUNT(p.PaymentID) AS paymentCount " +
                     "FROM Doctors d " +
                     "LEFT JOIN Payments p ON d.DoctorID = p.DoctorID " +
                     "GROUP BY d.DoctorID, d.full_name";
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("doctorId", rs.getInt("DoctorID"));
                row.put("doctorName", rs.getString("full_name"));
                row.put("totalRevenue", rs.getDouble("totalRevenue"));
                row.put("paymentCount", rs.getInt("paymentCount"));
                results.add(row);
            }
        }
        return results;
    }
}
