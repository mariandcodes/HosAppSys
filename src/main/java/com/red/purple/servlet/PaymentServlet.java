package com.red.purple.servlet;

import com.red.purple.dao.PaymentDAO;
import com.red.purple.model.Payment;

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
 * Servlet to handle Payment processing, retrieval, and history operations.
 */
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            paymentDAO = new PaymentDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("view".equalsIgnoreCase(action)) {
                viewPayment(req, resp);
            } else if ("listByPatient".equalsIgnoreCase(action)) {
                listPaymentsByPatient(req, resp);
            } else if ("listByAppointment".equalsIgnoreCase(action)) {
                listPaymentsByAppointment(req, resp);
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
            if ("record".equalsIgnoreCase(action)) {
                recordPayment(req, resp);
            } else if ("update".equalsIgnoreCase(action)) {
                updatePayment(req, resp);
            } else if ("updateStatus".equalsIgnoreCase(action)) {
                updatePaymentStatus(req, resp);
            } else if ("delete".equalsIgnoreCase(action)) {
                deletePayment(req, resp);
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void viewPayment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String paymentIdStr = req.getParameter("id");
        if (paymentIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing payment ID.");
            return;
        }
        int paymentId;
        try {
            paymentId = Integer.parseInt(paymentIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format.");
            return;
        }
        Payment payment = paymentDAO.getPaymentById(paymentId);
        if (payment == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found.");
            return;
        }
        req.setAttribute("payment", payment);
        req.getRequestDispatcher("/paymentDetail.jsp").forward(req, resp);
    }

    private void listPaymentsByPatient(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String patientIdStr = req.getParameter("patientId");
        if (patientIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing patient ID.");
            return;
        }
        int patientId;
        try {
            patientId = Integer.parseInt(patientIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID format.");
            return;
        }
        List<Payment> payments = paymentDAO.getPaymentsByPatient(patientId);
        req.setAttribute("payments", payments);
        req.getRequestDispatcher("/paymentList.jsp").forward(req, resp);
    }

    private void listPaymentsByAppointment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String appointmentIdStr = req.getParameter("appointmentId");
        if (appointmentIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appointment ID.");
            return;
        }
        int appointmentId;
        try {
            appointmentId = Integer.parseInt(appointmentIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID format.");
            return;
        }
        List<Payment> payments = paymentDAO.getPaymentsByAppointment(appointmentId);
        req.setAttribute("payments", payments);
        req.getRequestDispatcher("/paymentList.jsp").forward(req, resp);
    }

    private void recordPayment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        Payment payment = formToPayment(req);
        int newId = paymentDAO.createPayment(payment);
        resp.sendRedirect(req.getContextPath() + "/payment?action=view&id=" + newId);
    }

    private void updatePayment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing payment ID.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format.");
            return;
        }
        Payment payment = formToPayment(req);
        payment.setPaymentId(id); // Set the ID for the update operation
        paymentDAO.updatePayment(payment);
        resp.sendRedirect(req.getContextPath() + "/payment?action=view&id=" + id);
    }

    private void updatePaymentStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String paymentIdStr = req.getParameter("id");
        String status = req.getParameter("status");
        if (paymentIdStr == null || status == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters.");
            return;
        }
        int paymentId;
        try {
            paymentId = Integer.parseInt(paymentIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format.");
            return;
        }
        paymentDAO.updatePaymentStatus(paymentId, status);
        resp.getWriter().write("Payment status updated.");
    }

    private void deletePayment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing payment ID.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format.");
            return;
        }
        paymentDAO.deletePayment(id);
        resp.getWriter().write("Payment deleted.");
    }

    private Payment formToPayment(HttpServletRequest req) throws ServletException {
        Payment payment = new Payment();
        try {
            payment.setPatientId(getIntegerParameter(req, "patientId"));
            payment.setAppointmentId(getIntegerParameter(req, "appointmentId"));
            payment.setAmount(getDoubleParameter(req, "amount"));
            payment.setMethod(req.getParameter("method"));
            payment.setTransactionId(req.getParameter("transactionId"));
            payment.setStatus(req.getParameter("status"));
            String paidAtStr = req.getParameter("paidAt");
            if (paidAtStr != null && !paidAtStr.trim().isEmpty()) {
                payment.setPaidAt(Timestamp.valueOf(paidAtStr.trim()));
            }
        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid timestamp format for 'paidAt'", e);
        }
        return payment;
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