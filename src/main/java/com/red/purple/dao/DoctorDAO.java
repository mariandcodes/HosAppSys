package com.red.purple.dao;

import com.red.purple.model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for Doctor entities.
 * Handles CRUD operations for doctors in the database.
 */
public class DoctorDAO {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDAO.class);
    private final Connection connection;

    public DoctorDAO(Connection connection) {
        this.connection = connection;
    }

    public int createDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Doctors (UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, doctor.getUserId());
            stmt.setString(2, doctor.getFullName());
            stmt.setString(3, doctor.getSpecialty());
            stmt.setString(4, doctor.getQualifications());
            stmt.setString(5, doctor.getContactInfo());
            stmt.setString(6, doctor.getWorkingHours());
            stmt.setDouble(7, doctor.getConsultationFee());
            stmt.setString(8, doctor.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained.");
                }
            }
        }
    }

    public Doctor getDoctorById(int id) throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors WHERE DoctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapDoctor(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting doctor by id: {}", id, e);
            throw e;
        }
        return null;
    }

    /**
     * Retrieves a Doctor by their associated UserID.
     * @param userId The UserID to look up.
     * @return Doctor object if found, null otherwise.
     * @throws SQLException on database errors.
     */
    public Doctor getDoctorByUserId(int userId) throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapDoctor(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting doctor by user id: {}", userId, e);
            throw e;
        }
        return null;
    }

    public List<Doctor> searchDoctorsByNameOrSpecialty(String query) throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors WHERE full_name LIKE ? OR specialty LIKE ?";
        List<Doctor> doctors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching doctors with query: {}", query, e);
            throw e;
        }
        return doctors;
    }

    /**
     * Retrieves all doctors from the database.
     * @return List of all doctors.
     * @throws SQLException on database errors.
     */
    public List<Doctor> getAllDoctors() throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors ORDER BY full_name";
        List<Doctor> doctors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }
    
    /**
     * Retrieves all active doctors from the database.
     * @return List of all active doctors.
     * @throws SQLException on database errors.
     */
    public List<Doctor> getAllActiveDoctors() throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors WHERE status = 'ACTIVE' ORDER BY full_name";
        List<Doctor> doctors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }
    
    /**
     * Retrieves doctors by specialty.
     * @param specialty The specialty to filter by.
     * @return List of doctors with the specified specialty.
     * @throws SQLException on database errors.
     */
    public List<Doctor> getDoctorsBySpecialty(String specialty) throws SQLException {
        String sql = "SELECT DoctorID, UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status FROM Doctors WHERE specialty = ? AND status = 'ACTIVE' ORDER BY full_name";
        List<Doctor> doctors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specialty);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }
        return doctors;
    }

    public void updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE Doctors SET full_name = ?, specialty = ?, qualifications = ?, contact_info = ?, working_hours = ?, consultation_fee = ?, status = ? WHERE DoctorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialty());
            stmt.setString(3, doctor.getQualifications());
            stmt.setString(4, doctor.getContactInfo());
            stmt.setString(5, doctor.getWorkingHours());
            stmt.setDouble(6, doctor.getConsultationFee());
            stmt.setString(7, doctor.getStatus());
            stmt.setInt(8, doctor.getDoctorId());
            stmt.executeUpdate();
        }
    }

    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
            rs.getInt("DoctorID"),
            rs.getInt("UserID"),
            rs.getString("full_name"),
            rs.getString("specialty"),
            rs.getString("qualifications"),
            rs.getString("contact_info"),
            rs.getString("working_hours"),
            rs.getDouble("consultation_fee"),
            rs.getString("status")
        );
    }
}
