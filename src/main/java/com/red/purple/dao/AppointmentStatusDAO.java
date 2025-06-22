/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentStatusDAO {
    private final Connection connection;

    public AppointmentStatusDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> getAllStatuses() throws SQLException {
        String sql = "SELECT status_name FROM AppointmentStatuses";
        List<String> statuses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statuses.add(rs.getString("status_name"));
                }
            }
        }
        return statuses;
    }
}
