package com.red.purple.servlet;

import com.red.purple.dao.AppointmentDAO;
import com.red.purple.model.Appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


/**
 * Servlet to handle appointment booking, modification, cancellation, and status updates.
 */
@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            appointmentDAO = new AppointmentDAO(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new ServletException("Database connection problem", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "view":
                    viewAppointment(req, resp);
                    break;
                case "listByPatient":
                    listAppointmentsByPatient(req, resp);
                    break;
                case "listByDoctor":
                    listAppointmentsByDoctor(req, resp);
                    break;
                case "book":
                    showBookingForm(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
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
                case "book":
                    bookAppointment(req, resp);
                    break;
                case "update":
                    updateAppointment(req, resp);
                    break;
                case "cancel":
                    cancelAppointment(req, resp);
                    break;
                case "changeStatus":
                    changeAppointmentStatus(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void viewAppointment(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appointment ID");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID format");
            return;
        }
        Appointment appointment = appointmentDAO.getAppointmentById(id);
        if (appointment == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Appointment not found");
            return;
        }
        req.setAttribute("appointment", appointment);
        req.getRequestDispatcher("/appointmentDetail.jsp").forward(req, resp);
    }

    private void listAppointmentsByPatient(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String patientIdStr = req.getParameter("patientId");
        if (patientIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing patient ID");
            return;
        }
        int patientId;
        try {
            patientId = Integer.parseInt(patientIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID format");
            return;
        }
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatient(patientId);
        req.setAttribute("appointments", appointments);
        req.setAttribute("listType", "patient");
        req.getRequestDispatcher("/appointmentList.jsp").forward(req, resp);
    }

    private void listAppointmentsByDoctor(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String doctorIdStr = req.getParameter("doctorId");
        if (doctorIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing doctor ID");
            return;
        }
        int doctorId;
        try {
            doctorId = Integer.parseInt(doctorIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID format");
            return;
        }
        List<Appointment> appointments = appointmentDAO.getAppointmentsByDoctor(doctorId);
        req.setAttribute("appointments", appointments);
        req.setAttribute("listType", "doctor");
        req.getRequestDispatcher("/appointmentList.jsp").forward(req, resp);
    }

    private void showBookingForm(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/appointmentDetail.jsp").forward(req, resp);
    }

    private void bookAppointment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        Appointment appointment = formToAppointment(req);
        int newId = appointmentDAO.createAppointment(appointment);
        
        // Set success message and redirect to view the new appointment
        req.getSession().setAttribute("successMessage", "Appointment booked successfully!");
        resp.sendRedirect(req.getContextPath() + "/appointment?action=view&id=" + newId);
    }

    private void updateAppointment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appointment ID");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID format");
            return;
        }
        Appointment appointment = formToAppointment(req);
        appointment.setAppointmentId(id);
        appointmentDAO.updateAppointment(appointment);
        
        // Set success message and redirect back to dashboard
        req.getSession().setAttribute("successMessage", "Appointment updated successfully!");
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private void cancelAppointment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appointment ID");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID format");
            return;
        }
        appointmentDAO.deleteAppointment(id);
        
        // Set success message and redirect back to dashboard
        req.getSession().setAttribute("successMessage", "Appointment cancelled successfully!");
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private void changeAppointmentStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String idStr = req.getParameter("id");
        String status = req.getParameter("status");
        if (idStr == null || status == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appointment ID or status");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID format");
            return;
        }
        appointmentDAO.updateAppointmentStatus(id, status);
        
        // Set success message and redirect back to dashboard
        req.getSession().setAttribute("successMessage", "Appointment status updated successfully!");
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private Appointment formToAppointment(HttpServletRequest req) throws ServletException {
        Appointment appointment = new Appointment();
        try {
            String patientIdStr = req.getParameter("patientId");
            String doctorIdStr = req.getParameter("doctorId");
            String scheduledStr = req.getParameter("scheduledDatetime");

            if (patientIdStr == null || doctorIdStr == null || scheduledStr == null) {
                throw new ServletException("Missing required parameters");
            }

            appointment.setPatientId(Integer.parseInt(patientIdStr));
            appointment.setDoctorId(Integer.parseInt(doctorIdStr));
            appointment.setScheduledDatetime(Timestamp.valueOf(scheduledStr));
            appointment.setStatus(req.getParameter("status"));
            appointment.setReason(req.getParameter("reason"));
            appointment.setPreferredLanguage(req.getParameter("preferredLanguage"));
            appointment.setSpecialNeeds(req.getParameter("specialNeeds"));

        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid date/time format or number format", e);
        }
        return appointment;
    }
}