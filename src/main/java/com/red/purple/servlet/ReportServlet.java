package com.red.purple.servlet;

import com.red.purple.dao.ReportDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Servlet to handle generation and export of reports.
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private ReportDAO reportDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            reportDAO = new ReportDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportType = req.getParameter("type");
        if (reportType == null || reportType.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Report type is required");
            return;
        }

        try {
            // Example: You can define individual methods or service calls here depending on reportType
            switch (reportType) {
                case "appointmentSummary":
                    generateAppointmentSummary(req, resp);
                    break;
                case "doctorUtilization":
                    generateDoctorUtilization(req, resp);
                    break;
                case "noShowAnalysis":
                    generateNoShowAnalysis(req, resp);
                    break;
                case "revenueReports":
                    generateRevenueReports(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown report type");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Placeholder methods - Implement details according to your ReportDAO and model structure

    private void generateAppointmentSummary(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        // Fetch data from reportDAO
        var summary = reportDAO.getAppointmentSummary();
        req.setAttribute("summary", summary);
        req.getRequestDispatcher("/reportAppointmentSummary.jsp").forward(req, resp);
    }

    private void generateDoctorUtilization(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        var utilization = reportDAO.getDoctorUtilization();
        req.setAttribute("utilization", utilization);
        req.getRequestDispatcher("/reportDoctorUtilization.jsp").forward(req, resp);
    }

    private void generateNoShowAnalysis(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        var analysis = reportDAO.getNoShowAnalysis();
        req.setAttribute("analysis", analysis);
        req.getRequestDispatcher("/reportNoShowAnalysis.jsp").forward(req, resp);
    }

    private void generateRevenueReports(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        var revenue = reportDAO.getRevenueReports();
        req.setAttribute("revenue", revenue);
        req.getRequestDispatcher("/reportRevenue.jsp").forward(req, resp);
    }
}
