package com.red.purple.dao;

import com.red.purple.model.Role; // Import the Role class
import java.sql.*;

public class RoleDAO {
    private final Connection connection;

    public RoleDAO(Connection connection) {
        this.connection = connection;
    }

    public int createRole(String roleName) throws SQLException {
        // Change: "INSERT INTO Roles" to "INSERT INTO MATT.Roles"
        String sql = "INSERT INTO MATT.Roles (role_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, roleName);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating role failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
        }
    }

    public Role getRoleById(int roleId) throws SQLException {
        // Change: "SELECT RoleID, role_name FROM Roles" to "SELECT RoleID, role_name FROM MATT.Roles"
        String sql = "SELECT RoleID, role_name FROM MATT.Roles WHERE RoleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("RoleID"), rs.getString("role_name"));
                }
            }
        }
        return null;
    }

    public Role getRoleByName(String roleName) throws SQLException {
        // Change: "SELECT RoleID, role_name FROM Roles" to "SELECT RoleID, role_name FROM MATT.Roles"
        String sql = "SELECT RoleID, role_name FROM MATT.Roles WHERE role_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("RoleID"), rs.getString("role_name"));
                }
            }
        }
        return null;
    }
}