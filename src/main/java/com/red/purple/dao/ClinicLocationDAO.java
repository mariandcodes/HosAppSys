/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.dao;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClinicLocationDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClinicLocationDAO.class);
    private final Connection connection;

    public ClinicLocationDAO(Connection connection) {
        this.connection = connection;
    }

    public int createLocation(String name, String address, String phone) throws SQLException {
        String sql = "INSERT INTO ClinicLocations (name, address, phone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating location failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating location failed, no ID obtained.");
            }
        } catch (SQLException e) {
            logger.error("Error creating clinic location with name: {}", name, e);
            throw e;
        }
    }

    public ClinicLocation getLocationById(int locationId) throws SQLException {
        String sql = "SELECT * FROM ClinicLocations WHERE LocationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ClinicLocation(
                        rs.getInt("LocationID"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting clinic location by ID: {}", locationId, e);
            throw e;
        }
        return null;
    }
}

class ClinicLocation {
    private int locationId;
    private String name;
    private String address;
    private String phone;

    public ClinicLocation(int locationId, String name, String address, String phone) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    // Getters and setters omitted for brevity

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
