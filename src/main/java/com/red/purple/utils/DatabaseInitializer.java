package com.red.purple.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Added for retrieving role_id and user_id
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // Added for java.sql.Date

import org.mindrot.jbcrypt.BCrypt; // Added for password hashing

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:derby://localhost:1527/hospitalsysDB;create=true";
        String user = "matt";
        String password = "123";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            // Execute the schema SQL statements (CREATE TABLEs)

            // Roles Table
            String createRolesTable = "CREATE TABLE Roles (" +
                    "RoleID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "role_name VARCHAR(50) NOT NULL UNIQUE" +
                    ")";
            try {
                statement.executeUpdate(createRolesTable);
                System.out.println("Table 'Roles' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) { // X0Y32 is for "table already exists"
                    System.out.println("Table 'Roles' already exists. Skipping creation.");
                } else {
                    throw e; // Re-throw other SQL exceptions
                }
            }

            // Users Table
            String createUsersTable = "CREATE TABLE Users (" +
                    "UserID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL UNIQUE, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "role_id INT NOT NULL, " +
                    "status VARCHAR(50) DEFAULT 'ACTIVE', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (role_id) REFERENCES Roles(RoleID)" +
                    ")";
            try {
                statement.executeUpdate(createUsersTable);
                System.out.println("Table 'Users' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("Table 'Users' already exists. Skipping creation.");
                } else {
                    throw e;
                }
            }

            // Patients Table
            String createPatientsTable = "CREATE TABLE Patients (" +
                    "PatientID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "UserID INT NOT NULL UNIQUE, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "dob DATE NOT NULL, " +
                    "gender VARCHAR(10), " +
                    "contact_info VARCHAR(255), " +
                    "address VARCHAR(255), " +
                    "emergency_contact VARCHAR(255), " +
                    "insurance_provider VARCHAR(255), " +
                    "insurance_policy VARCHAR(255), " +
                    "status VARCHAR(50) DEFAULT 'ACTIVE', " +
                    "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
                    ")";
            try {
                statement.executeUpdate(createPatientsTable);
                System.out.println("Table 'Patients' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("Table 'Patients' already exists. Skipping creation.");
                } else {
                    throw e;
                }
            }
            
            // Doctors Table
            String createDoctorsTable = "CREATE TABLE Doctors (" +
                    "DoctorID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "UserID INT NOT NULL UNIQUE, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "specialty VARCHAR(100), " +
                    "qualifications VARCHAR(500), " +
                    "contact_info VARCHAR(255), " +
                    "working_hours VARCHAR(255), " +
                    "consultation_fee DECIMAL(10,2), " +
                    "status VARCHAR(50) DEFAULT 'ACTIVE', " +
                    "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
                    ")";
            try {
                statement.executeUpdate(createDoctorsTable);
                System.out.println("Table 'Doctors' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("Table 'Doctors' already exists. Skipping creation.");
                } else {
                    throw e;
                }
            }

            // Appointments Table
            String createAppointmentsTable = "CREATE TABLE Appointments (" +
                    "AppointmentID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "PatientID INT NOT NULL, " +
                    "DoctorID INT NOT NULL, " +
                    "scheduled_datetime TIMESTAMP NOT NULL, " +
                    "status VARCHAR(50) NOT NULL, " + // e.g., SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
                    "reason VARCHAR(500), " +
                    "preferred_language VARCHAR(50), " +
                    "special_needs VARCHAR(500), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (PatientID) REFERENCES Patients(PatientID), " +
                    "FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID)" +
                    ")";
            try {
                statement.executeUpdate(createAppointmentsTable);
                System.out.println("Table 'Appointments' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) { // X0Y32 is for "table already exists"
                    System.out.println("Table 'Appointments' already exists. Skipping creation.");
                } else {
                    throw e; // Re-throw other SQL exceptions
                }
            }

            // DoctorSchedules Table (NEWLY ADDED)
            String createDoctorSchedulesTable = "CREATE TABLE DoctorSchedules (" +
                    "ScheduleID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "DoctorID INT NOT NULL, " +
                    "start_time TIMESTAMP NOT NULL, " +
                    "end_time TIMESTAMP NOT NULL, " +
                    "FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID)" +
                    ")";
            try {
                statement.executeUpdate(createDoctorSchedulesTable);
                System.out.println("Table 'DoctorSchedules' created.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) { // X0Y32 is for "table already exists"
                    System.out.println("Table 'DoctorSchedules' already exists. Skipping creation.");
                } else {
                    throw e; // Re-throw other SQL exceptions
                }
            }

            System.out.println("Inserting default roles...");

            String insertRoleSql = "INSERT INTO Roles (role_name) VALUES (?)";
            try {
                insertRole(connection, insertRoleSql, "ADMIN");
                insertRole(connection, insertRoleSql, "DOCTOR");
                insertRole(connection, insertRoleSql, "PATIENT");
                insertRole(connection, insertRoleSql, "STAFF");
                insertRole(connection, insertRoleSql, "USER");
                insertRole(connection, insertRoleSql, "ACCOUNTANT");
                System.out.println("Default roles insertion process completed.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) { // 23505 is the SQLSTATE for a unique constraint violation
                    System.out.println("Some default roles already exist. Skipping duplicate insertion.");
                } else {
                    throw e; // Re-throw other SQL exceptions
                }
            }

            System.out.println("Inserting sample users...");

            // Define sample users and their specific passwords
            String adminUsername = "red";
            String adminPassword = "whatifthissongwasintagalog"; 
            String doctorUsername = "dr.smith";
            String doctorPassword = "password123";
            String patientUsername = "jane.doe";
            String patientPassword = "patientpass";
            String staffUsername = "staff.user";
            String staffPassword = "staffpass";
            String genericUserUsername = "normal.user";
            String genericUserPassword = "userpass";
            String accountantUsername = "accountant.a";
            String accountantPassword = "accountpass";
            
            // Create User accounts
            createUser(connection, adminUsername, adminPassword, "ADMIN");
            createUser(connection, doctorUsername, doctorPassword, "DOCTOR");
            createUser(connection, patientUsername, patientPassword, "PATIENT");
            createUser(connection, staffUsername, staffPassword, "STAFF");
            createUser(connection, genericUserUsername, genericUserPassword, "USER");
            createUser(connection, accountantUsername, accountantPassword, "ACCOUNTANT");

            System.out.println("Populating sample Patients and Doctors data...");

            // Get UserIDs for Patient and Doctor to link them
            int patientUserId = getUserIdByUsername(connection, patientUsername);
            int doctorUserId = getUserIdByUsername(connection, doctorUsername);

            if (patientUserId != -1) {
                insertPatient(connection, patientUserId, "Jane Doe", Date.valueOf("1990-05-15"), "Female", 
                              "jane.doe@example.com", "123 Patient St, City", "John Doe (555-123-4567)", 
                              "HealthGuard Insurance", "HG-12345", "ACTIVE");
            } else {
                System.err.println("Could not find UserID for patient: " + patientUserId + " (" + patientUsername + "). Skipping patient data insertion.");
            }

            if (doctorUserId != -1) {
                insertDoctor(connection, doctorUserId, "Dr. John Smith", "General Practice", 
                             "MD, MPH", "dr.smith@example.com", "Mon-Fri 9AM-5PM", 50.00, "ACTIVE");
            } else {
                System.err.println("Could not find UserID for doctor: " + doctorUserId + " (" + doctorUsername + "). Skipping doctor data insertion.");
            }

            // Optional: Insert some sample appointments for testing
            // Get PatientID and DoctorID from the tables after insertion
            int patientIdForAppointment = getPatientIdByUserId(connection, patientUserId);
            int doctorIdForAppointment = getDoctorIdByUserId(connection, doctorUserId);

            if (patientIdForAppointment != -1 && doctorIdForAppointment != -1) {
                // Insert a sample appointment for the patient and doctor
                insertAppointment(connection, patientIdForAppointment, doctorIdForAppointment, 
                                  java.sql.Timestamp.valueOf("2025-06-25 10:00:00.0"), "SCHEDULED", 
                                  "Routine Checkup", "English", "None");
                insertAppointment(connection, patientIdForAppointment, doctorIdForAppointment, 
                                  java.sql.Timestamp.valueOf("2025-06-26 14:00:00.0"), "SCHEDULED", 
                                  "Follow-up", "English", "Wheelchair access needed");
            } else {
                System.err.println("Could not find PatientID or DoctorID for sample appointments. Skipping appointment data insertion.");
            }

            // Optional: Insert some sample doctor schedules for testing
            if (doctorIdForAppointment != -1) {
                insertDoctorSchedule(connection, doctorIdForAppointment,
                                    java.sql.Timestamp.valueOf("2025-06-24 09:00:00.0"),
                                    java.sql.Timestamp.valueOf("2025-06-24 17:00:00.0"));
                insertDoctorSchedule(connection, doctorIdForAppointment,
                                    java.sql.Timestamp.valueOf("2025-06-25 09:00:00.0"),
                                    java.sql.Timestamp.valueOf("2025-06-25 12:00:00.0"));
            } else {
                 System.err.println("Could not find DoctorID for sample schedules. Skipping schedule data insertion.");
            }


            System.out.println("Database and tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserts a role into the Roles table if it doesn't already exist.
     * @param connection The database connection.
     * @param insertSql The SQL query for inserting a role.
     * @param roleName The name of the role to insert.
     * @throws SQLException If a database access error occurs.
     */
    private static void insertRole(Connection connection, String insertSql, String roleName) throws SQLException {
        String checkRoleSql = "SELECT COUNT(*) FROM Roles WHERE role_name = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkRoleSql)) {
            checkStmt.setString(1, roleName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) { // If role doesn't exist
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setString(1, roleName);
                        insertStmt.executeUpdate();
                        System.out.println("Inserted role: " + roleName);
                    }
                } else {
                    System.out.println("Role '" + roleName + "' already exists. Skipping.");
                }
            }
        }
    }

    /**
     * Inserts a user into the Users table if the username doesn't already exist.
     * Passwords are hashed using BCrypt before insertion.
     * @param connection The database connection.
     * @param username The username for the new user.
     * @param plainPassword The plain-text password for the new user.
     * @param roleName The role name for the new user (e.g., "ADMIN", "DOCTOR").
     * @throws SQLException If a database access error occurs.
     */
    private static void createUser(Connection connection, String username, String plainPassword, String roleName) throws SQLException {
        // First, check if the user already exists
        String checkUserSql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {
            checkUserStmt.setString(1, username);
            try (ResultSet rs = checkUserStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("User '" + username + "' already exists. Skipping creation.");
                    return; // User already exists, no need to proceed
                }
            }
        }

        // Get the role_id for the given roleName
        int roleId = -1;
        String getRoleIdSql = "SELECT RoleID FROM Roles WHERE role_name = ?";
        try (PreparedStatement getRoleIdStmt = connection.prepareStatement(getRoleIdSql)) {
            getRoleIdStmt.setString(1, roleName);
            try (ResultSet rs = getRoleIdStmt.executeQuery()) {
                if (rs.next()) {
                    roleId = rs.getInt("RoleID");
                } else {
                    System.err.println("Error: Role '" + roleName + "' not found in Roles table. Cannot create user '" + username + "'.");
                    return; // Cannot create user without a valid role
                }
            }
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // Insert the new user
        String insertUserSql = "INSERT INTO Users (username, password_hash, role_id, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSql)) {
            insertUserStmt.setString(1, username);
            insertUserStmt.setString(2, hashedPassword);
            insertUserStmt.setInt(3, roleId);
            insertUserStmt.setString(4, "ACTIVE"); // Default status
            insertUserStmt.executeUpdate();
            System.out.println("Inserted user: " + username + " with role: " + roleName);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Unique constraint violation (username already exists)
                System.out.println("User '" + username + "' already exists. Skipping duplicate insertion.");
            } else {
                throw e; // Re-throw other SQL exceptions
            }
        }
    }

    /**
     * Retrieves the UserID for a given username from the Users table.
     * @param connection The database connection.
     * @param username The username to look up.
     * @return The UserID if found, or -1 if not found.
     * @throws SQLException If a database access error occurs.
     */
    private static int getUserIdByUsername(Connection connection, String username) throws SQLException {
        String sql = "SELECT UserID FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserID");
                }
            }
        }
        return -1; // User not found
    }

    /**
     * Inserts a patient's details into the Patients table.
     * @param connection The database connection.
     * @param userId The UserID associated with this patient.
     * @param fullName The full name of the patient.
     * @param dob The date of birth of the patient.
     * @param gender The gender of the patient.
     * @param contactInfo The contact information of the patient.
     * @param address The address of the patient.
     * @param emergencyContact The emergency contact information.
     * @param insuranceProvider The insurance provider.
     * @param insurancePolicy The insurance policy number.
     * @param status The status of the patient (e.g., ACTIVE).
     * @throws SQLException If a database access error occurs.
     */
    private static void insertPatient(Connection connection, int userId, String fullName, Date dob, String gender,
                                      String contactInfo, String address, String emergencyContact,
                                      String insuranceProvider, String insurancePolicy, String status) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Patients WHERE UserID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Patient with UserID " + userId + " already exists. Skipping insertion.");
                    return;
                }
            }
        }

        String sql = "INSERT INTO Patients (UserID, full_name, dob, gender, contact_info, address, emergency_contact, insurance_provider, insurance_policy, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, fullName);
            stmt.setDate(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, contactInfo);
            stmt.setString(6, address);
            stmt.setString(7, emergencyContact);
            stmt.setString(8, insuranceProvider);
            stmt.setString(9, insurancePolicy);
            stmt.setString(10, status);
            stmt.executeUpdate();
            System.out.println("Inserted patient for UserID: " + userId + " (" + fullName + ")");
        }
    }

    /**
     * Inserts a doctor's details into the Doctors table.
     * @param connection The database connection.
     * @param userId The UserID associated with this doctor.
     * @param fullName The full name of the doctor.
     * @param specialty The doctor's specialty.
     * @param qualifications The doctor's qualifications.
     * @param contactInfo The contact information of the doctor.
     * @param workingHours The doctor's working hours.
     * @param consultationFee The consultation fee.
     * @param status The status of the doctor (e.g., ACTIVE).
     * @throws SQLException If a database access error occurs.
     */
    private static void insertDoctor(Connection connection, int userId, String fullName, String specialty,
                                     String qualifications, String contactInfo, String workingHours,
                                     double consultationFee, String status) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Doctors WHERE UserID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Doctor with UserID " + userId + " already exists. Skipping insertion.");
                    return;
                }
            }
        }

        String sql = "INSERT INTO Doctors (UserID, full_name, specialty, qualifications, contact_info, working_hours, consultation_fee, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, fullName);
            stmt.setString(3, specialty);
            stmt.setString(4, qualifications);
            stmt.setString(5, contactInfo);
            stmt.setString(6, workingHours);
            stmt.setDouble(7, consultationFee);
            stmt.setString(8, status);
            stmt.executeUpdate();
            System.out.println("Inserted doctor for UserID: " + userId + " (" + fullName + ")");
        }
    }

    /**
     * Retrieves the PatientID for a given userId from the Patients table.
     * @param connection The database connection.
     * @param userId The UserID to look up in the Patients table.
     * @return The PatientID if found, or -1 if not found.
     * @throws SQLException If a database access error occurs.
     */
    private static int getPatientIdByUserId(Connection connection, int userId) throws SQLException {
        String sql = "SELECT PatientID FROM Patients WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PatientID");
                }
            }
        }
        return -1; // Patient not found for this UserID
    }

    /**
     * Retrieves the DoctorID for a given userId from the Doctors table.
     * @param connection The database connection.
     * @param userId The UserID to look up in the Doctors table.
     * @return The DoctorID if found, or -1 if not found.
     * @throws SQLException If a database access error occurs.
     */
    private static int getDoctorIdByUserId(Connection connection, int userId) throws SQLException {
        String sql = "SELECT DoctorID FROM Doctors WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("DoctorID");
                }
            }
        }
        return -1; // Doctor not found for this UserID
    }

    /**
     * Inserts an appointment into the Appointments table.
     * @param connection The database connection.
     * @param patientId The ID of the patient.
     * @param doctorId The ID of the doctor.
     * @param scheduledDatetime The scheduled date and time of the appointment.
     * @param status The status of the appointment (e.g., SCHEDULED, COMPLETED).
     * @param reason The reason for the appointment.
     * @param preferredLanguage The patient's preferred language.
     * @param specialNeeds Any special needs for the appointment.
     * @throws SQLException If a database access error occurs.
     */
    private static void insertAppointment(Connection connection, int patientId, int doctorId,
                                        java.sql.Timestamp scheduledDatetime, String status,
                                        String reason, String preferredLanguage, String specialNeeds) throws SQLException {
        String sql = "INSERT INTO Appointments (PatientID, DoctorID, scheduled_datetime, status, reason, preferred_language, special_needs) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setTimestamp(3, scheduledDatetime);
            stmt.setString(4, status);
            stmt.setString(5, reason);
            stmt.setString(6, preferredLanguage);
            stmt.setString(7, specialNeeds);
            stmt.executeUpdate();
            System.out.println("Inserted appointment for PatientID " + patientId + " and DoctorID " + doctorId + ".");
        }
    }

    /**
     * Inserts a doctor's schedule into the DoctorSchedules table.
     * @param connection The database connection.
     * @param doctorId The ID of the doctor.
     * @param startTime The start time of the schedule slot.
     * @param endTime The end time of the schedule slot.
     * @throws SQLException If a database access error occurs.
     */
    private static void insertDoctorSchedule(Connection connection, int doctorId,
                                            java.sql.Timestamp startTime, java.sql.Timestamp endTime) throws SQLException {
        String sql = "INSERT INTO DoctorSchedules (DoctorID, start_time, end_time) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, startTime);
            stmt.setTimestamp(3, endTime);
            stmt.executeUpdate();
            System.out.println("Inserted schedule for DoctorID " + doctorId + ": " + startTime + " to " + endTime + ".");
        }
    }
}
