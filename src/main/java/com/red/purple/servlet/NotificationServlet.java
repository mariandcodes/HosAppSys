package com.red.purple.servlet;

import com.red.purple.dao.NotificationDAO;
import com.red.purple.model.Notification;

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
 * Servlet to manage user notifications: listing and marking as read.
 */
@WebServlet("/notification")
public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            notificationDAO = new NotificationDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("list".equalsIgnoreCase(action)) {
                listNotifications(req, resp);
            } else if ("view".equalsIgnoreCase(action)) {
                viewNotification(req, resp);
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
            if ("create".equalsIgnoreCase(action)) {
                createNotification(req, resp);
            } else if ("markAsRead".equalsIgnoreCase(action)) {
                markAsRead(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void createNotification(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        Notification notification = formToNotification(req);
        int newId = notificationDAO.createNotification(notification);
        resp.sendRedirect(req.getContextPath() + "/notification?action=view&id=" + newId);
    }

    private void viewNotification(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String notificationIdStr = req.getParameter("id");
        if (notificationIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing notification ID.");
            return;
        }
        int notificationId;
        try {
            notificationId = Integer.parseInt(notificationIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid notification ID format.");
            return;
        }
        Notification notification = notificationDAO.getNotificationById(notificationId);
        if (notification == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Notification not found.");
            return;
        }
        req.setAttribute("notification", notification);
        req.getRequestDispatcher("/notificationDetail.jsp").forward(req, resp);
    }

    private void listNotifications(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID.");
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format.");
            return;
        }
        List<Notification> notifications = notificationDAO.getNotificationsForUser(userId);
        req.setAttribute("notifications", notifications);
        req.getRequestDispatcher("/notificationList.jsp").forward(req, resp);
    }

    private void markAsRead(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String notificationIdStr = req.getParameter("notificationId");
        if (notificationIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing notification ID.");
            return;
        }
        int notificationId;
        try {
            notificationId = Integer.parseInt(notificationIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid notification ID format.");
            return;
        }
        notificationDAO.markAsRead(notificationId);
        resp.getWriter().write("Notification marked as read.");
    }

    private Notification formToNotification(HttpServletRequest req) throws ServletException {
        Notification notification = new Notification();
        try {
            notification.setUserId(getIntegerParameter(req, "userId"));
            notification.setType(req.getParameter("type"));
            notification.setContent(req.getParameter("content"));
            notification.setRead(getBooleanParameter(req, "isRead"));

            String createdAtStr = req.getParameter("createdAt");
            if (createdAtStr != null && !createdAtStr.trim().isEmpty()) {
                notification.setCreatedAt(Timestamp.valueOf(createdAtStr.trim()));
            } else {
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis())); // Default to current time if not provided
            }
        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid timestamp format for 'createdAt'", e);
        }
        return notification;
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

    private boolean getBooleanParameter(HttpServletRequest req, String paramName) throws ServletException {
        String paramValue = req.getParameter(paramName);
        // Treat null or empty string as false, or throw exception if strictness is required
        if (paramValue == null || paramValue.trim().isEmpty()) {
            return false; // Default to false if not provided
        }
        return Boolean.parseBoolean(paramValue.trim());
    }
}