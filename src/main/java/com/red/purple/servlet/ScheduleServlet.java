package com.red.purple.servlet;

import com.red.purple.dao.ScheduleDAO;
import com.red.purple.dao.DoctorDAO;
import com.red.purple.model.Schedule;
import com.red.purple.model.Doctor;

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
 * Servlet to handle doctor schedules, meetings, and availability blocks.
 * This is separate from appointments which involve patients.
 */
@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {

    private ScheduleDAO scheduleDAO;
    private DoctorDAO doctorDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            scheduleDAO = new ScheduleDAO(connection);
            doctorDAO = new DoctorDAO(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new ServletException("Database connection problem", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if (action == null) {
            action = "list"; // Default action
        }
        
        try {
            switch (action) {
                case "list":
                    listSchedules(req, resp);
                    break;
                case "view":
                    viewSchedule(req, resp);
                    break;
                case "create":
                    showCreateForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteSchedule(req, resp);
                    break;
                case "listByDoctor":
                    listSchedulesByDoctor(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        try {
            switch (action) {
                case "create":
                    createSchedule(req, resp);
                    break;
                case "update":
                    updateSchedule(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid POST action: " + action);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listSchedules(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        String roleName = (String) session.getAttribute("roleName");
        
        List<Schedule> schedules;
        
        // If user is a doctor, show only their schedules
        if ("DOCTOR".equals(roleName)) {
            int userId = (int) session.getAttribute("userId");
            Doctor doctor = doctorDAO.getDoctorByUserId(userId);
            if (doctor != null) {
                schedules = scheduleDAO.getSchedulesByDoctor(doctor.getDoctorId());
            } else {
                schedules = List.of(); // Empty list
            }
        } else {
            // Admin/Staff can see all schedules
            schedules = scheduleDAO.getAllSchedules();
        }
        
        req.setAttribute("schedules", schedules);
        req.getRequestDispatcher("/scheduleList.jsp").forward(req, resp);
    }

    private void listSchedulesByDoctor(HttpServletRequest req, HttpServletResponse resp)
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
        
        List<Schedule> schedules = scheduleDAO.getSchedulesByDoctor(doctorId);
        req.setAttribute("schedules", schedules);
        req.setAttribute("doctorId", doctorId);
        req.getRequestDispatcher("/scheduleList.jsp").forward(req, resp);
    }

    private void viewSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing schedule ID");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid schedule ID format");
            return;
        }
        
        Schedule schedule = scheduleDAO.getScheduleById(id);
        if (schedule == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Schedule not found");
            return;
        }
        
        req.setAttribute("schedule", schedule);
        req.getRequestDispatcher("/scheduleDetail.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Get list of doctors for the form dropdown
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            req.setAttribute("doctors", doctors);
        } catch (SQLException e) {
            throw new ServletException("Error loading doctors", e);
        }
        req.getRequestDispatcher("/scheduleForm.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing schedule ID");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid schedule ID format");
            return;
        }
        
        Schedule schedule = scheduleDAO.getScheduleById(id);
        if (schedule == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Schedule not found");
            return;
        }
        
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        req.setAttribute("schedule", schedule);
        req.setAttribute("doctors", doctors);
        req.getRequestDispatcher("/scheduleForm.jsp").forward(req, resp);
    }

    private void createSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        try {
            Schedule schedule = formToSchedule(req);
            int newId = scheduleDAO.createSchedule(schedule);
            
            req.setAttribute("message", "Schedule created successfully!");
            resp.sendRedirect(req.getContextPath() + "/schedule?action=view&id=" + newId);
        } catch (Exception e) {
            req.setAttribute("error", "Error creating schedule: " + e.getMessage());
            showCreateForm(req, resp);
        }
    }

    private void updateSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing schedule ID");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid schedule ID format");
            return;
        }
        
        try {
            Schedule schedule = formToSchedule(req);
            schedule.setId(id);
            scheduleDAO.updateSchedule(schedule);
            
            req.setAttribute("message", "Schedule updated successfully!");
            resp.sendRedirect(req.getContextPath() + "/schedule?action=view&id=" + id);
        } catch (Exception e) {
            req.setAttribute("error", "Error updating schedule: " + e.getMessage());
            showEditForm(req, resp);
        }
    }

    private void deleteSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing schedule ID");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid schedule ID format");
            return;
        }
        
        scheduleDAO.deleteSchedule(id);
        req.getSession().setAttribute("message", "Schedule deleted successfully!");
        resp.sendRedirect(req.getContextPath() + "/schedule?action=list");
    }

    private Schedule formToSchedule(HttpServletRequest req) throws ServletException {
        Schedule schedule = new Schedule();
        
        try {
            String doctorIdStr = req.getParameter("doctorId");
            String startTimeStr = req.getParameter("startTime");
            String endTimeStr = req.getParameter("endTime");
            String scheduleType = req.getParameter("scheduleType");
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String location = req.getParameter("location");
            String statusStr = req.getParameter("status");

            if (doctorIdStr == null || startTimeStr == null || endTimeStr == null) {
                throw new ServletException("Missing required parameters");
            }

            schedule.setDoctorId(Integer.parseInt(doctorIdStr));
            schedule.setStartTime(Timestamp.valueOf(startTimeStr.replace("T", " ") + ":00"));
            schedule.setEndTime(Timestamp.valueOf(endTimeStr.replace("T", " ") + ":00"));
            schedule.setScheduleType(scheduleType != null ? scheduleType : "AVAILABILITY");
            schedule.setTitle(title);
            schedule.setDescription(description);
            schedule.setLocation(location);
            schedule.setStatus(statusStr != null ? statusStr : "ACTIVE");

        } catch (IllegalArgumentException e) {
            throw new ServletException("Invalid date/time format or number format", e);
        }
        
        return schedule;
    }
}