package com.red.purple.servlet;

import com.red.purple.dao.UserDAO;
import com.red.purple.dao.RoleDAO;
import com.red.purple.model.User;
import com.red.purple.model.Role;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Servlet for User Authentication, Registration, and Profile management.
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialize database connection - replace with your DB config
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/hospitalsysDB;create=true", "matt", "123");
            this.userDAO = new UserDAO(connection);
            this.roleDAO = new RoleDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "login"; // Default action if none specified
        }

        try {
            switch (action) {
                case "login":
                    // Redirect to login page or show login form
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    break;
                case "logout":
                    handleLogout(req, resp);
                    break;
                case "profile":
                    viewProfile(req, resp);
                    break;
                case "register":
                    req.getRequestDispatcher("/register.jsp").forward(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in doGet: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "login"; // Default action for POST if none specified
        }

        try {
            switch (action) {
                case "login":
                    handleLogin(req, resp);
                    break;
                case "register":
                    handleRegistration(req, resp);
                    break;
                case "updateProfile":
                    handleProfileUpdate(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
                    break;
            }
        } catch (SQLException e) {
            // This catch block handles SQL exceptions from the specific action handlers
            // For better user experience, we can try to forward back to appropriate page
            // For now, re-throwing as ServletException will lead to a 500 error page.
            // More granular error handling for each action is done within their respective methods.
            throw new ServletException("Database error in doPost: " + e.getMessage(), e);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userDAO.getUserByUsername(username);

        if (user != null && verifyPassword(password, user.getPasswordHash())) {
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("roleId", user.getRoleId());

            // Retrieve role name for display or further logic
            Role userRole = roleDAO.getRoleById(user.getRoleId());
            if (userRole != null) {
                session.setAttribute("roleName", userRole.getRoleName());
            }

            resp.sendRedirect("dashboard"); // Redirect to a dashboard or home page
        } else {
            req.setAttribute("error", "Invalid username or password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void handleRegistration(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String roleParam = req.getParameter("role"); // Get the selected role from the form

        if (username == null || username.trim().isEmpty()) {
            req.setAttribute("error", "Username cannot be empty.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (isInvalidPassword(password)) {
            req.setAttribute("error", "Password must be at least 8 characters.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (roleParam == null || roleParam.trim().isEmpty()) {
            req.setAttribute("error", "Role cannot be empty.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // Map the JSP role value to the database role name
        String roleName;
        switch (roleParam) {
            case "ROLE_PATIENT":
                roleName = "PATIENT"; // Corrected to match DatabaseInitializer role names
                break;
            case "ROLE_DOCTOR":
                roleName = "DOCTOR"; // Corrected to match DatabaseInitializer role names
                break;
            case "ROLE_RECEPTIONIST": // Assuming "STAFF" or another specific role in DB
                roleName = "STAFF"; // Corrected to match DatabaseInitializer role names
                break;
            // Add other roles as necessary, e.g., for ADMIN, USER, ACCOUNTANT if they can self-register
            default:
                req.setAttribute("error", "Invalid role selected.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
        }

        // Check if username already exists
        try {
            if (userDAO.getUserByUsername(username) != null) {
                req.setAttribute("error", "Username already exists. Please choose a different one.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }

            String hashedPassword = hashPassword(password);
            
            // Get the Role object using the mapped roleName
            Role selectedRole = roleDAO.getRoleByName(roleName); 
            if (selectedRole == null) {
                req.setAttribute("error", "Selected role '" + roleName + "' not found in database. Please ensure database roles are initialized.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPasswordHash(hashedPassword);
            newUser.setRoleId(selectedRole.getRoleId()); // Set the RoleID based on selected role
            newUser.setStatus("ACTIVE"); // Assuming a default status, consistent with DatabaseInitializer

            userDAO.createUser(newUser); 
            
            // Success: Redirect to login.jsp with a success message
            resp.sendRedirect("login.jsp?message=Registration successful! Please login.");
        } catch (SQLException e) {
            // Handle specific SQL errors or general database errors
            String errorMessage = "Error during user registration: " + e.getMessage();
            if (e.getSQLState().equals("23505")) { // Common SQLSTATE for unique constraint violation (username already exists)
                errorMessage = "Username already exists. Please choose a different one.";
            } else {
                // Log the full exception for debugging in server logs
                System.err.println(errorMessage);
                e.printStackTrace(); // Print stack trace to console for server-side debugging
            }
            req.setAttribute("error", errorMessage);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }


    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        resp.sendRedirect("login.jsp?message=You have been logged out."); // Redirect to login page
    }

    private void viewProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userDAO.getUserById(userId);
        if (user != null) {
            req.setAttribute("user", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User profile not found.");
        }
    }

    private void handleProfileUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        String newPassword = req.getParameter("newPassword");
        if (newPassword != null && !newPassword.isEmpty()) {
            if (isInvalidPassword(newPassword)) {
                req.setAttribute("error", "Password must be at least 8 characters");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            String newPasswordHash = hashPassword(newPassword);
            userDAO.updateUserPassword(userId, newPasswordHash); // Corrected
        }

        // Update other profile details if implemented...

        resp.sendRedirect("user?action=profile&message=Profile updated");
    }

    private boolean isInvalidPassword(String password) {
        return password == null || password.length() < 8;
    }

    private String hashPassword(String passwordPlain) {
        return BCrypt.hashpw(passwordPlain, BCrypt.gensalt());
    }

    private boolean verifyPassword(String passwordPlain, String storedHash) {
        return BCrypt.checkpw(passwordPlain, storedHash);
    }
}
