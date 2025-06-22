/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.dao;

import com.red.purple.model.DoctorSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoctorScheduleDAO {
    private static final Logger logger = LoggerFactory.getLogger(DoctorScheduleDAO.class);
    private final Connection connection;

    public DoctorScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    public int createSchedule(DoctorSchedule schedule) throws SQLException {
        String sql = "INSERT INTO DoctorSchedules (DoctorID, day_of_week, start_time, end_time, slot_duration_minutes, location_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getDoctorId());
            stmt.setString(2, schedule.getDayOfWeek());
            stmt.setTime(3, schedule.getStartTime());
            stmt.setTime(4, schedule.getEndTime());
            stmt.setInt(5, schedule.getSlotDurationMinutes());
            stmt.setInt(6, schedule.getLocationId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating schedule failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating schedule failed, no ID obtained.");
            }
        } catch (SQLException e) {
            logger.error("Error creating schedule for doctor ID: {}", schedule.getDoctorId(), e);
            throw e;
        }
    }

    public List<DoctorSchedule> getSchedulesForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT * FROM DoctorSchedules WHERE DoctorID = ?";
        List<DoctorSchedule> schedules = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapSchedule(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting schedules for doctor ID: {}", doctorId, e);
            throw e;
        }
        return schedules;
    }

    private DoctorSchedule mapSchedule(ResultSet rs) throws SQLException {
        return new DoctorSchedule(
            rs.getInt("ScheduleID"),
            rs.getInt("DoctorID"),
            rs.getString("day_of_week"),
            rs.getTime("start_time"),
            rs.getTime("end_time"),
            rs.getInt("slot_duration_minutes"),
            rs.getInt("location_id")
        );
    }
}
