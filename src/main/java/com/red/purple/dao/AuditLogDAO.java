/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.dao;

import java.sql.*;

public class AuditLogDAO {
    private final Connection connection;

    public AuditLogDAO(Connection connection) {
        this.connection = connection;
    }

    public int logAction(int userId, String actionType, String resourceType, int resourceId, String ipAddress) throws SQLException {
        String sql = "INSERT INTO AuditLogs (UserID, action_type, resource_type, resource_id, timestamp, ip_address) " +
                     "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setString(2, actionType);
            stmt.setString(3, resourceType);
            stmt.setInt(4, resourceId);
            stmt.setString(5, ipAddress);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Logging action failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Logging action failed, no ID obtained.");
            }
        }
    }

    // Additional methods for querying logs can be added here
}
