package com.red.purple.servlet;

import com.red.purple.dao.InvoiceDAO;
import com.red.purple.model.Invoice;

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
 * Servlet to handle Invoice viewing, listing, and generation.
 */
@WebServlet("/invoice")
public class InvoiceServlet extends HttpServlet {

    private InvoiceDAO invoiceDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            invoiceDAO = new InvoiceDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("view".equalsIgnoreCase(action)) {
                viewInvoice(req, resp);
            } else if ("listByPayment".equalsIgnoreCase(action)) {
                listInvoicesByPayment(req, resp);
            } else {
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
            if ("generate".equalsIgnoreCase(action)) {
                generateInvoice(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void viewInvoice(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int invoiceId = getIntegerParameter(req, "id");
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        if (invoice == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invoice not found.");
            return;
        }
        req.setAttribute("invoice", invoice);
        req.getRequestDispatcher("/invoiceDetail.jsp").forward(req, resp);
    }

    private void listInvoicesByPayment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int paymentId = getIntegerParameter(req, "paymentId");
        List<Invoice> invoices = invoiceDAO.getInvoicesByPaymentId(paymentId);
        req.setAttribute("invoices", invoices);
        req.getRequestDispatcher("/invoiceList.jsp").forward(req, resp);
    }

    private void generateInvoice(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        // Extract parameters from request using helper methods
        int paymentId = getIntegerParameter(req, "paymentId");
        double amount = getDoubleParameter(req, "amount");
        String pdfPath = req.getParameter("invoicePdfPath"); // PDF path can be an empty string, so no need for strict `getIntegerParameter` like check.
        if (pdfPath == null || pdfPath.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: invoicePdfPath");
        }

        // Create invoice object
        Invoice invoice = new Invoice();
        invoice.setPaymentId(paymentId);
        invoice.setAmount(amount);
        invoice.setInvoicePdfPath(pdfPath.trim());
        invoice.setIssuedAt(new java.sql.Timestamp(System.currentTimeMillis())); // Set issued_at to current time

        int newInvoiceId = invoiceDAO.createInvoice(invoice);

        resp.sendRedirect(req.getContextPath() + "/invoice?action=view&id=" + newInvoiceId);
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

    private double getDoubleParameter(HttpServletRequest req, String paramName) throws ServletException {
        String paramValue = req.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new ServletException("Missing or empty required parameter: " + paramName);
        }
        try {
            return Double.parseDouble(paramValue.trim());
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid numeric format for parameter: " + paramName, e);
        }
    }
}