package com.red.purple.dao;

import com.red.purple.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Schedule entities.
 * Handles doctor schedules, availability blocks, meetings, etc.
 */
public class ScheduleDAO {
    private Connection connection;

    public ScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Create a new schedule entry
     */
    public int createSchedule(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO schedules (doctor_id, start_time, end_time, schedule_type, title, description, location, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getDoctorId());
            stmt.setTimestamp(2, schedule.getStartTime());
            stmt.setTimestamp(3, schedule.getEndTime());
            stmt.setString(4, schedule.getScheduleType());
            stmt.setString(5, schedule.getTitle());
            stmt.setString(6, schedule.getDescription());
            stmt.setString(7, schedule.getLocation());
            stmt.setString(8, schedule.getStatus() != null ? schedule.getStatus() : "ACTIVE");
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating schedule failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating schedule failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Get a schedule by ID
     */
    public Schedule getScheduleById(int id) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSchedule(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all schedules for a specific doctor
     */
    public List<Schedule> getSchedulesByDoctor(int doctorId) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE doctor_id = ? ORDER BY start_time ASC";
        List<Schedule> schedules = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }
        return schedules;
    }

    /**
     * Get all schedules (for admin/staff view)
     */
    public List<Schedule> getAllSchedules() throws SQLException {
        String sql = "SELECT * FROM schedules ORDER BY start_time ASC";
        List<Schedule> schedules = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }
        return schedules;
    }

    /**
     * Get schedules by doctor and date range
     */
    public List<Schedule> getSchedulesByDoctorAndDateRange(int doctorId, Timestamp startDate, Timestamp endDate) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE doctor_id = ? AND start_time >= ? AND end_time <= ? ORDER BY start_time ASC";
        List<Schedule> schedules = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }
        return schedules;
    }

    /**
     * Get schedules by type (e.g., MEETING, AVAILABILITY, etc.)
     */
    public List<Schedule> getSchedulesByType(String scheduleType) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE schedule_type = ? ORDER BY start_time ASC";
        List<Schedule> schedules = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, scheduleType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }
        return schedules;
    }

    /**
     * Update an existing schedule
     */
    public void updateSchedule(Schedule schedule) throws SQLException {
        String sql = "UPDATE schedules SET doctor_id = ?, start_time = ?, end_time = ?, schedule_type = ?, " +
                     "title = ?, description = ?, location = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, schedule.getDoctorId());
            stmt.setTimestamp(2, schedule.getStartTime());
            stmt.setTimestamp(3, schedule.getEndTime());
            stmt.setString(4, schedule.getScheduleType());
            stmt.setString(5, schedule.getTitle());
            stmt.setString(6, schedule.getDescription());
            stmt.setString(7, schedule.getLocation());
            stmt.setString(8, schedule.getStatus());
            stmt.setInt(9, schedule.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating schedule failed, no rows affected.");
            }
        }
    }

    /**
     * Update schedule status only
     */
    public void updateScheduleStatus(int id, String status) throws SQLException {
        String sql = "UPDATE schedules SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating schedule status failed, no rows affected.");
            }
        }
    }

    /**
     * Delete a schedule
     */
    public void deleteSchedule(int id) throws SQLException {
        String sql = "DELETE FROM schedules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting schedule failed, no rows affected.");
            }
        }
    }

    /**
     * Check for schedule conflicts
     */
    public boolean hasScheduleConflict(int doctorId, Timestamp startTime, Timestamp endTime, Integer excludeScheduleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM schedules WHERE doctor_id = ? AND status != 'CANCELLED' AND " +
                     "((start_time <= ? AND end_time > ?) OR (start_time < ? AND end_time >= ?) OR (start_time >= ? AND end_time <= ?))";
        
        if (excludeScheduleId != null) {
            sql += " AND id != ?";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, startTime);
            stmt.setTimestamp(3, startTime);
            stmt.setTimestamp(4, endTime);
            stmt.setTimestamp(5, endTime);
            stmt.setTimestamp(6, startTime);
            stmt.setTimestamp(7, endTime);
            
            if (excludeScheduleId != null) {
                stmt.setInt(8, excludeScheduleId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Map ResultSet to Schedule object
     */
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("id"));
        schedule.setDoctorId(rs.getInt("doctor_id"));
        schedule.setStartTime(rs.getTimestamp("start_time"));
        schedule.setEndTime(rs.getTimestamp("end_time"));
        schedule.setScheduleType(rs.getString("schedule_type"));
        schedule.setTitle(rs.getString("title"));
        schedule.setDescription(rs.getString("description"));
        schedule.setLocation(rs.getString("location"));
        schedule.setStatus(rs.getString("status"));
        schedule.setCreatedAt(rs.getTimestamp("created_at"));
        schedule.setUpdatedAt(rs.getTimestamp("updated_at"));
        return schedule;
    }
}