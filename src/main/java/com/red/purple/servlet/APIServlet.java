package com.red.purple.servlet;

import com.red.purple.dao.AppointmentDAO;
import com.red.purple.dao.PatientDAO;
import com.red.purple.model.Appointment;
import com.red.purple.model.Patient;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api")
public class APIServlet extends HttpServlet {

    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;
    private Gson gson = new Gson();

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
        String resource = req.getParameter("resource");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            switch (resource) {
                case "patients":
                    List<Patient> patients = patientDAO.getAllPatients();
                    out.print(gson.toJson(patients));
                    break;
                case "appointments":
                    List<Appointment> appointments = appointmentDAO.getAllAppointments();
                    out.print(gson.toJson(appointments));
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown resource");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resource = req.getParameter("resource");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            switch (resource) {
                case "patients":
                    Patient newPatient = gson.fromJson(req.getReader(), Patient.class);
                    patientDAO.createPatient(newPatient);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    out.print(gson.toJson(newPatient));
                    break;
                case "appointments":
                    Appointment newAppointment = gson.fromJson(req.getReader(), Appointment.class);
                    appointmentDAO.createAppointment(newAppointment);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    out.print(gson.toJson(newAppointment));
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown resource");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resource = req.getParameter("resource");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            switch (resource) {
                case "patients":
                    Patient updatedPatient = gson.fromJson(req.getReader(), Patient.class);
                    patientDAO.updatePatient(updatedPatient);
                    out.print(gson.toJson(updatedPatient));
                    break;
                case "appointments":
                    Appointment updatedAppointment = gson.fromJson(req.getReader(), Appointment.class);
                    appointmentDAO.updateAppointment(updatedAppointment);
                    out.print(gson.toJson(updatedAppointment));
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown resource");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resource = req.getParameter("resource");
        String idStr = req.getParameter("id");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            if (idStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required");
                return;
            }
            int id = Integer.parseInt(idStr);
            switch (resource) {
                case "patients":
                    patientDAO.deletePatient(id);
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    break;
                case "appointments":
                    appointmentDAO.deleteAppointment(id);
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown resource");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            out.close();
        }
    }
}
