package com.red.purple.dao;

import com.red.purple.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private final Connection connection;

    public PatientDAO(Connection connection) {
        this.connection = connection;
    }

    public int createPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patients (UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, patient.getUserId());
            stmt.setString(2, patient.getFullName());
            stmt.setDate(3, patient.getDob());
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getContactInfo());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getEmergencyContact()); // Added emergency_contact
            stmt.setString(8, patient.getInsuranceProvider());
            stmt.setString(9, patient.getInsurancePolicy());
            stmt.setString(10, patient.getStatus());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Creating patient failed, no ID obtained.");
            }
        }
    }

    public Patient getPatientById(int patientId) throws SQLException {
        String sql = "SELECT PatientID, UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status FROM Patients WHERE PatientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a Patient by their associated UserID.
     * @param userId The UserID to look up.
     * @return Patient object if found, null otherwise.
     * @throws SQLException on database errors.
     */
    public Patient getPatientByUserId(int userId) throws SQLException {
        String sql = "SELECT PatientID, UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status FROM Patients WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
            }
        }
        return null;
    }

    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE Patients SET full_name = ?, dob = ?, gender = ?, contact_info = ?, address = ?, emergency_contact = ?, insurance_provider = ?, insurance_policy = ?, status = ? WHERE PatientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patient.getFullName());
            stmt.setDate(2, patient.getDob());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getContactInfo());
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getEmergencyContact()); // Added emergency_contact
            stmt.setString(7, patient.getInsuranceProvider());
            stmt.setString(8, patient.getInsurancePolicy());
            stmt.setString(9, patient.getStatus());
            stmt.setInt(10, patient.getPatientId());
            stmt.executeUpdate();
        }
    }

    public void deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM Patients WHERE PatientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.executeUpdate();
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        String sql = "SELECT PatientID, UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status FROM Patients";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }
        return patients;
    }

    public List<Patient> searchPatients(String query) throws SQLException {
        String sql = "SELECT PatientID, UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status FROM Patients WHERE full_name LIKE ? OR contact_info LIKE ?";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }
        }
        return patients;
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getInt("PatientID"),
            rs.getInt("UserID"),
            rs.getString("full_name"),
            rs.getDate("dob"),
            rs.getString("gender"),
            rs.getString("contact_info"),
            rs.getString("address"),
            rs.getString("emergency_contact"), // Corrected to include emergency_contact
            rs.getString("insurance_provider"),
            rs.getString("insurance_policy"),
            rs.getString("status")
        );
    }
}
