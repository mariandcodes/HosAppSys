package com.red.purple.servlet;

import com.red.purple.dao.PatientDAO;
import com.red.purple.dao.AppointmentDAO;
import com.red.purple.model.Patient;
import com.red.purple.model.Appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Servlet to handle front-desk staff functionalities such as
 * assisting patients, managing bookings, searching patients,
 * and booking appointments on behalf of patients.
 */
@WebServlet("/staff")
public class StaffServlet extends HttpServlet {

    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            patientDAO = new PatientDAO(connection);
            appointmentDAO = new AppointmentDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "viewPatient":
                    viewPatient(req, resp);
                    break;
                case "searchPatients":
                    searchPatients(req, resp);
                    break;
                case "listAppointments":
                    listAppointments(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "bookAppointment":
                    bookAppointment(req, resp);
                    break;
                case "updateAppointmentStatus":
                    updateAppointmentStatus(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void viewPatient(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int patientId = getIntegerParameter(req, "id");
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient not found.");
            return;
        }
        req.setAttribute("patient", patient);
        req.getRequestDispatcher("/patientDetail.jsp").forward(req, resp);
    }

    private void searchPatients(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String query = req.getParameter("query");
        List<Patient> patients;
        if (query != null && !query.trim().isEmpty()) {
            patients = patientDAO.searchPatients(query);
        } else {
            patients = patientDAO.getAllPatients(); // Assuming this method exists or you handle it
        }
        req.setAttribute("patients", patients);
        req.getRequestDispatcher("/patientList.jsp").forward(req, resp);
    }

    private void listAppointments(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int patientId = getIntegerParameter(req, "patientId");
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatient(patientId);
        req.setAttribute("appointments", appointments);
        req.getRequestDispatcher("/appointmentList.jsp").forward(req, resp);
    }

    private void bookAppointment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        Appointment appointment = formToAppointment(req);
        int newId = appointmentDAO.createAppointment(appointment);
        resp.sendRedirect(req.getContextPath() + "/staff?action=listAppointments&patientId=" + appointment.getPatientId());
    }

    private void updateAppointmentStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int appointmentId = getIntegerParameter(req, "id");
        String status = req.getParameter("status");

        if (status == null || status.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: status");
        }

        appointmentDAO.updateAppointmentStatus(appointmentId, status);
        resp.getWriter().write("Status updated.");
    }

    private Appointment formToAppointment(HttpServletRequest req) throws ServletException {
        Appointment appointment = new Appointment();
        try {
            appointment.setPatientId(getIntegerParameter(req, "patientId"));
            appointment.setDoctorId(getIntegerParameter(req, "doctorId"));
            appointment.setScheduledDatetime(getTimestampParameter(req, "scheduledDatetime"));
            appointment.setStatus("Scheduled"); // Default status for new booking
            appointment.setReason(req.getParameter("reason"));
            appointment.setPreferredLanguage(req.getParameter("preferredLanguage"));
            appointment.setSpecialNeeds(req.getParameter("specialNeeds"));

        } catch (ServletException e) {
            // Re-throw ServletException as it contains specific error message
            throw e;
        } catch (Exception e) {
            // Catch any other unexpected exceptions during form processing
            throw new ServletException("Error processing appointment form parameters", e);
        }
        return appointment;
    }

    private int getIntegerParameter(HttpServletRequest req, String paramName) throws ServletException {
        String paramValue = req.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: " + paramName);
        }
        try {
            return Integer.parseInt(paramValue.trim());
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid numeric format for parameter: " + paramName, e);
        }
    }

    private Timestamp getTimestampParameter(HttpServletRequest req, String paramName) throws ServletException {
        String paramValue = req.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: " + paramName);
        }
        try {
            return Timestamp.valueOf(paramValue.trim());
        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid timestamp format for parameter: " + paramName + ". Expected YYYY-MM-DD HH:MM:SS.mmm (e.g., 2023-10-27 10:00:00.0).", e);
        }
    }
}