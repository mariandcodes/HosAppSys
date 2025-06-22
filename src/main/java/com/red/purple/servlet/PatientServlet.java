package com.red.purple.servlet;

import com.red.purple.dao.PatientDAO;
import com.red.purple.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

@WebServlet("/patient")
public class PatientServlet extends HttpServlet {

    private PatientDAO patientDAO; // Declare the PatientDAO field

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            this.patientDAO = new PatientDAO(connection); // Assign the created PatientDAO object to the field
        } catch (ClassNotFoundException | SQLException ex) {
            throw new ServletException("Database connection problem", ex);
        }
    }

    // ... (doGet, doPost methods would go here, using this.patientDAO) ...

    private Patient formToPatient(HttpServletRequest req) throws ServletException {
        Patient patient = new Patient();
        try {
            String userIdStr = req.getParameter("userId");
            if (userIdStr != null && !userIdStr.isEmpty()) {
                patient.setUserId(Integer.parseInt(userIdStr));
            } else {
                throw new ServletException("User ID is missing or invalid.");
            }
            patient.setFullName(req.getParameter("fullName"));

            String dobStr = req.getParameter("dob");
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    patient.setDob(Date.valueOf(dobStr));
                } catch (IllegalArgumentException e) {
                    throw new ServletException("Invalid date format for DOB. Expected YYYY-MM-dd.", e);
                }
            } else {
                patient.setDob(null);
            }

            patient.setGender(req.getParameter("gender"));
            patient.setContactInfo(req.getParameter("contactInfo"));
            patient.setAddress(req.getParameter("address"));
            patient.setEmergencyContact(req.getParameter("emergencyContact"));
            patient.setInsuranceProvider(req.getParameter("insuranceProvider"));
            patient.setInsurancePolicy(req.getParameter("insurancePolicy"));
            patient.setStatus(req.getParameter("status"));
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid number format in parameters.", e);
        }
        return patient;
    }
}