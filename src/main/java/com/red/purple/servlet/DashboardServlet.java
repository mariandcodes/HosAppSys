package com.red.purple.servlet;

import com.red.purple.dao.AppointmentDAO;
import com.red.purple.dao.ReportDAO;
import com.red.purple.dao.UserDAO;
import com.red.purple.dao.PatientDAO; // Added for getting patientId from userId
import com.red.purple.dao.DoctorDAO; // Added for getting doctorId from userId
import com.red.purple.model.User;
import com.red.purple.model.Appointment; // Import Appointment model
import com.red.purple.model.Patient;    // Import Patient model
import com.red.purple.model.Doctor;     // Import Doctor model


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Servlet to render role-based dashboards for Admin, Manager/Receptionist,
 * Doctor, and Patient with KPIs and widget data.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private UserDAO userDAO;
    private AppointmentDAO appointmentDAO;
    private ReportDAO reportDAO;
    private PatientDAO patientDAO; // Added
    private DoctorDAO doctorDAO;   // Added

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            userDAO = new UserDAO(connection);
            appointmentDAO = new AppointmentDAO(connection);
            reportDAO = new ReportDAO(connection);
            patientDAO = new PatientDAO(connection); // Initialized
            doctorDAO = new DoctorDAO(connection);   // Initialized
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String roleName = (String) session.getAttribute("roleName"); // Get roleName from session

        if (roleName == null || roleName.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User role not found in session.");
            return;
        }

        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            // Prepare data based on roleName
            switch (roleName) {
                case "ADMIN":
                    prepareAdminDashboard(req);
                    break;
                case "STAFF":
                    prepareManagerDashboard(req, userId);
                    break;
                case "DOCTOR":
                    prepareDoctorDashboard(req, userId);
                    break;
                case "PATIENT":
                    preparePatientDashboard(req, userId);
                    break;
                case "USER":
                    req.setAttribute("dashboardTitle", "User Dashboard");
                    req.setAttribute("userWelcomeMessage", "Welcome, " + user.getUsername() + "!");
                    break;
                case "ACCOUNTANT":
                    req.setAttribute("dashboardTitle", "Accountant Dashboard");
                    req.setAttribute("accountantWelcomeMessage", "Welcome, " + user.getUsername() + "!");
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unknown or unhandled role: " + roleName);
                    return;
            }

            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Failed to load dashboard data", e);
        }
    }

    private void prepareAdminDashboard(HttpServletRequest req) throws SQLException {
        List<Map<String, Object>> appointmentSummary = reportDAO.getAppointmentSummary();
        List<Map<String, Object>> revenueReports = reportDAO.getRevenueReports();
        req.setAttribute("appointmentSummary", appointmentSummary);
        req.setAttribute("revenueReports", revenueReports);
        req.setAttribute("dashboardTitle", "Admin Dashboard");
    }

    private void prepareManagerDashboard(HttpServletRequest req, int userId) throws SQLException {
        req.setAttribute("dashboardTitle", "Manager Dashboard");
        req.setAttribute("managerWelcomeMessage", "Welcome, Manager!");
        // You can add logic here to fetch data relevant to managers, e.g., today's appointments, waitlist
    }

    private void prepareDoctorDashboard(HttpServletRequest req, int doctorUserId) throws SQLException {
        // Fetch DoctorID using userId
        Doctor doctor = doctorDAO.getDoctorByUserId(doctorUserId); // Assuming a getDoctorByUserId method in DoctorDAO
        if (doctor != null) {
            List<Appointment> doctorAppointments = appointmentDAO.getAppointmentsByDoctor(doctor.getDoctorId());
            req.setAttribute("doctorAppointments", doctorAppointments);
            req.setAttribute("doctorName", doctor.getFullName()); // Set doctor's full name
        } else {
            System.err.println("Doctor record not found for userId: " + doctorUserId);
            req.setAttribute("doctorAppointments", new java.util.ArrayList<Appointment>()); // Empty list
        }
        req.setAttribute("dashboardTitle", "Doctor Dashboard");
        req.setAttribute("doctorWelcomeMessage", "Welcome, Doctor!");
    }

    private void preparePatientDashboard(HttpServletRequest req, int patientUserId) throws SQLException {
        // Fetch PatientID using userId
        Patient patient = patientDAO.getPatientByUserId(patientUserId); // Assuming a getPatientByUserId method in PatientDAO
        if (patient != null) {
            List<Appointment> patientAppointments = appointmentDAO.getAppointmentsByPatient(patient.getPatientId());
            req.setAttribute("patientAppointments", patientAppointments);
            req.setAttribute("patientName", patient.getFullName()); // Set patient's full name
        } else {
            System.err.println("Patient record not found for userId: " + patientUserId);
            req.setAttribute("patientAppointments", new java.util.ArrayList<Appointment>()); // Empty list
        }
        req.setAttribute("dashboardTitle", "Patient Dashboard");
        req.setAttribute("patientWelcomeMessage", "Welcome, Patient!");
    }
}
