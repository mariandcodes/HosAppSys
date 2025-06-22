package com.red.purple.servlet;

import com.red.purple.dao.WaitlistEntryDAO;
import com.red.purple.model.WaitlistEntry;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to handle waitlist entries: join, notify, remove.
 */
@WebServlet("/waitlist")
public class WaitlistServlet extends HttpServlet {

    private WaitlistEntryDAO waitlistEntryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            waitlistEntryDAO = new WaitlistEntryDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("list".equalsIgnoreCase(action)) {
                listWaitlistEntries(req, resp);
            } else if ("view".equalsIgnoreCase(action)) {
                viewWaitlistEntry(req, resp);
            } else if ("listByDoctor".equalsIgnoreCase(action)) {
                listWaitlistEntriesByDoctor(req, resp);
            } else if ("listByDate".equalsIgnoreCase(action)) {
                listWaitlistEntriesByDate(req, resp);
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("add".equalsIgnoreCase(action)) {
                addWaitlistEntry(req, resp);
            } else if ("remove".equalsIgnoreCase(action)) {
                removeWaitlistEntry(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void addWaitlistEntry(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        WaitlistEntry entry = formToWaitlistEntry(req);
        int newId = waitlistEntryDAO.addWaitlistEntry(entry);
        resp.sendRedirect(req.getContextPath() + "/waitlist?action=view&id=" + newId);
    }

    private void viewWaitlistEntry(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int waitlistId = getIntegerParameter(req, "id");
        WaitlistEntry entry = waitlistEntryDAO.getWaitlistEntryById(waitlistId);
        if (entry == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Waitlist entry not found.");
            return;
        }
        req.setAttribute("waitlistEntry", entry);
        req.getRequestDispatcher("/waitlistDetail.jsp").forward(req, resp);
    }

    private void listWaitlistEntries(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        // This method implies listing all, but the DAO only has byDoctor/byDate.
        // Assuming 'list' will be a general overview or if no specific filter is applied,
        // it might show an empty list or redirect to a filtered view.
        // For now, let's just show an empty list or require a filter.
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Please specify 'userId', 'doctorId', or 'date' for listing waitlist entries.");
        // A more complete implementation might list all if no parameter, or require patientId/doctorId/date
        // Example: List<WaitlistEntry> entries = waitlistEntryDAO.getAllWaitlistEntries(); // if such a method existed
        // req.setAttribute("waitlistEntries", entries);
        // req.getRequestDispatcher("/waitlistList.jsp").forward(req, resp);
    }

    private void listWaitlistEntriesByDoctor(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int doctorId = getIntegerParameter(req, "doctorId");
        List<WaitlistEntry> entries = waitlistEntryDAO.getWaitlistEntriesByDoctor(doctorId);
        req.setAttribute("waitlistEntries", entries);
        req.getRequestDispatcher("/waitlistList.jsp").forward(req, resp);
    }

    private void listWaitlistEntriesByDate(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Date requestedDate = getDateParameter(req, "requestedDate");
        List<WaitlistEntry> entries = waitlistEntryDAO.getWaitlistEntriesByDate(requestedDate);
        req.setAttribute("waitlistEntries", entries);
        req.getRequestDispatcher("/waitlistList.jsp").forward(req, resp);
    }

    private void removeWaitlistEntry(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int waitlistId = getIntegerParameter(req, "id");
        waitlistEntryDAO.removeWaitlistEntry(waitlistId);
        resp.getWriter().write("Waitlist entry removed.");
    }

    private WaitlistEntry formToWaitlistEntry(HttpServletRequest req) throws ServletException {
        WaitlistEntry entry = new WaitlistEntry();
        try {
            entry.setPatientId(getIntegerParameter(req, "patientId"));
            entry.setDoctorId(getIntegerParameter(req, "doctorId"));
            entry.setRequestedDate(getDateParameter(req, "requestedDate"));
            entry.setPosition(0); // Position can be managed by DAO or other logic (e.g., set dynamically in DAO)
        } catch (ServletException e) {
            // Re-throw ServletException as it contains specific error message
            throw e;
        } catch (Exception e) {
            // Catch any other unexpected exceptions during form processing
            throw new ServletException("Error processing waitlist entry form parameters", e);
        }
        return entry;
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

    private Date getDateParameter(HttpServletRequest req, String paramName) throws ServletException {
        String paramValue = req.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: " + paramName);
        }
        try {
            return Date.valueOf(paramValue.trim());
        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid date format for parameter: " + paramName + ". Expected YYYY-MM-DD.", e);
        }
    }
}