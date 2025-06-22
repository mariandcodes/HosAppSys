/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Matthew
 * Created: Jun 14, 2025
 */
-- Derby SQL Database Schema for Healthcare Appointment System

-- Roles Table
CREATE TABLE Roles (
    RoleID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Users Table
CREATE TABLE Users (
    UserID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Roles(RoleID)
);

-- Patients Table
CREATE TABLE Patients (
    PatientID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    UserID INT NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(10),
    contact_info VARCHAR(255),
    address VARCHAR(255),
    emergency_contact VARCHAR(255),
    insurance_provider VARCHAR(255),
    insurance_policy VARCHAR(255),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Doctors Table
CREATE TABLE Doctors (
    DoctorID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    UserID INT NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    specialty VARCHAR(100),
    qualifications VARCHAR(500),
    contact_info VARCHAR(255),
    working_hours VARCHAR(255),
    consultation_fee DECIMAL(10,2),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- ClinicLocations Table
CREATE TABLE ClinicLocations (
    LocationID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50)
);

-- DoctorSchedules Table
CREATE TABLE DoctorSchedules (
    ScheduleID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    DoctorID INT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,  -- e.g., 'Monday'
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration_minutes INT NOT NULL,
    location_id INT,
    CONSTRAINT no_overlap_per_doctor UNIQUE (DoctorID, day_of_week, start_time, end_time),
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID),
    FOREIGN KEY (location_id) REFERENCES ClinicLocations(LocationID)
);

-- AppointmentStatuses Table
CREATE TABLE AppointmentStatuses (
    StatusID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

-- Appointments Table
CREATE TABLE Appointments (
    AppointmentID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    scheduled_datetime TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason VARCHAR(1000),
    preferred_language VARCHAR(50),
    special_needs VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID)
);

CREATE INDEX IDX_Appointments_ScheduledDate ON Appointments(scheduled_datetime);
CREATE INDEX IDX_Appointments_Doctor ON Appointments(DoctorID);
CREATE INDEX IDX_Appointments_Patient ON Appointments(PatientID);

-- WaitlistEntries Table
CREATE TABLE WaitlistEntries (
    WaitlistID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    requested_date DATE NOT NULL,
    position INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID)
);

-- Notifications Table
CREATE TABLE Notifications (
    NotificationID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    UserID INT NOT NULL,
    type VARCHAR(50),
    content VARCHAR(2000),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE INDEX IDX_Notifications_User ON Notifications(UserID);

-- Payments Table
CREATE TABLE Payments (
    PaymentID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PatientID INT NOT NULL,
    AppointmentID INT,
    amount DECIMAL(12,2) NOT NULL,
    method VARCHAR(50),
    transaction_id VARCHAR(255),
    status VARCHAR(50),
    paid_at TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID)
);

-- Invoices Table
CREATE TABLE Invoices (
    InvoiceID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PaymentID INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    invoice_pdf_path VARCHAR(512),
    FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID)
);

-- AuditLogs Table
CREATE TABLE AuditLogs (
    LogID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    UserID INT NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100),
    resource_id INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE INDEX IDX_AuditLogs_User ON AuditLogs(UserID);
CREATE INDEX IDX_AuditLogs_Timestamp ON AuditLogs(timestamp);

-- Additional constraints and triggers to enforce no overlapping appointments and schedule availability should be implemented at the application/DAO level or with stored procedures if needed.


