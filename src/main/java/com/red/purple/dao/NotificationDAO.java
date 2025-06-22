package com.red.purple.dao;

import com.red.purple.model.Notification; // Correct import for the Notification model

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final Connection connection;

    public NotificationDAO(Connection connection) {
        this.connection = connection;
    }

    public int createNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO Notifications (UserID, type, content, is_read, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getType());
            stmt.setString(3, notification.getContent());
            stmt.setBoolean(4, notification.isRead());
            stmt.setTimestamp(5, notification.getCreatedAt()); // Assuming createdAt is set before calling
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating notification failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating notification failed, no ID obtained.");
            }
        }
    }

    public Notification getNotificationById(int notificationId) throws SQLException {
        String sql = "SELECT NotificationID, UserID, type, content, is_read, created_at FROM Notifications WHERE NotificationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapNotification(rs);
                }
            }
        }
        return null;
    }

    public void markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE NotificationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        }
    }

    public List<Notification> getNotificationsForUser(int userId) throws SQLException {
        String sql = "SELECT NotificationID, UserID, type, content, is_read, created_at FROM Notifications WHERE UserID = ? ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapNotification(rs));
                }
            }
        }
        return notifications;
    }

    private Notification mapNotification(ResultSet rs) throws SQLException {
        return new Notification(
            rs.getInt("NotificationID"),
            rs.getInt("UserID"),
            rs.getString("type"),
            rs.getString("content"),
            rs.getBoolean("is_read"),
            rs.getTimestamp("created_at")
        );
    }
}