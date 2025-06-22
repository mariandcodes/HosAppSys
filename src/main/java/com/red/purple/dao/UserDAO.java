/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.dao;

import com.red.purple.model.User;

import java.sql.*;

/**
 * DAO to manage user-related database operations.
 */
public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve a User by their ID.
     * @param userId The user ID.
     * @return User object or null if not found.
     * @throws SQLException on DB errors.
     */
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT UserID, username, password_hash, role_id, status, created_at FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getInt("role_id"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retrieve a User by their username.
     * @param username The username.
     * @return User object or null if not found.
     * @throws SQLException on DB errors.
     */
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT UserID, username, password_hash, role_id, status, created_at FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getInt("role_id"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Create a new user in the database.
     * @param user The user object to create.
     * @return The generated UserID.
     * @throws SQLException on DB errors.
     */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (username, password_hash, role_id, status, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getRoleId());
            stmt.setString(4, user.getStatus());
            stmt.setTimestamp(5, user.getCreatedAt()); // Use the timestamp from the user object
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Update user password.
     */
    public void updateUserPassword(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE Users SET password_hash = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPasswordHash);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    /**
     * Update user status.
     */
    public void updateUserStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE Users SET status = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}