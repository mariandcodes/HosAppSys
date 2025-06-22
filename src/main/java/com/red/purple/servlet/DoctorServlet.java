/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.red.purple.servlet;

import com.red.purple.dao.DoctorDAO;
import com.red.purple.model.Doctor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to handle Doctor CRUD and search operations.
 */
@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {

    private DoctorDAO doctorDAO;

    @Override
    public void init() throws jakarta.servlet.ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            this.doctorDAO = new DoctorDAO(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new jakarta.servlet.ServletException("Database connection problem", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, jakarta.servlet.ServletException {
        String action = req.getParameter("action");
        try {
            if ("view".equals(action)) {
                viewDoctor(req, resp);
            } else if ("search".equals(action)) {
                searchDoctors(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            throw new jakarta.servlet.ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, jakarta.servlet.ServletException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                createDoctor(req, resp);
            } else if ("update".equals(action)) {
                updateDoctor(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            throw new jakarta.servlet.ServletException(e);
        }
    }

    private void viewDoctor(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, jakarta.servlet.ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing doctor ID");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID format");
            return;
        }
        Doctor doctor = doctorDAO.getDoctorById(id);
        if (doctor == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Doctor not found");
            return;
        }
        req.setAttribute("doctor", doctor);
        req.getRequestDispatcher("/doctorProfile.jsp").forward(req, resp);
    }

    private void searchDoctors(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, jakarta.servlet.ServletException {
        String query = req.getParameter("query");
        if (query == null || query.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty search query");
            return;
        }
        List<Doctor> doctors = doctorDAO.searchDoctorsByNameOrSpecialty(query);
        req.setAttribute("doctors", doctors);
        req.getRequestDispatcher("/doctorList.jsp").forward(req, resp);
    }

    private void createDoctor(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, jakarta.servlet.ServletException {
        Doctor doctor = formToDoctor(req);
        int newId = doctorDAO.createDoctor(doctor);
        resp.sendRedirect(req.getContextPath() + "/doctor?action=view&id=" + newId);
    }

    private void updateDoctor(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, jakarta.servlet.ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing doctor ID");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID format");
            return;
        }
        Doctor existing = doctorDAO.getDoctorById(id);
        if (existing == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Doctor not found");
            return;
        }
        Doctor updated = formToDoctor(req);
        updated.setDoctorId(id);
        doctorDAO.updateDoctor(updated);
        resp.sendRedirect(req.getContextPath() + "/doctor?action=view&id=" + id);
    }

    private Doctor formToDoctor(HttpServletRequest req) throws jakarta.servlet.ServletException {
        Doctor doctor = new Doctor();
        try {
            String userIdStr = req.getParameter("userId");
            if (userIdStr != null && !userIdStr.isEmpty()) {
                doctor.setUserId(Integer.parseInt(userIdStr));
            } else {
                throw new jakarta.servlet.ServletException("User ID is missing or invalid.");
            }
            doctor.setFullName(req.getParameter("fullName"));
            doctor.setSpecialty(req.getParameter("specialty"));
            doctor.setQualifications(req.getParameter("qualifications"));
            doctor.setContactInfo(req.getParameter("contactInfo"));
            doctor.setWorkingHours(req.getParameter("workingHours"));
            String feeStr = req.getParameter("consultationFee");
            doctor.setConsultationFee(feeStr != null && !feeStr.isEmpty() ? Double.parseDouble(feeStr) : 0.0);
            doctor.setStatus(req.getParameter("status"));
        } catch (NumberFormatException e) {
            throw new jakarta.servlet.ServletException("Invalid number format in parameters.", e);
        }
        return doctor;
    }
}
