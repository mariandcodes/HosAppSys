<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.text.SimpleDateFormat"%>
<%
    // Application-scope user store and audit log initialization
    ServletContext app = application;
    Map&lt;String, Map&lt;String,String&gt;&gt; users = (Map&lt;String, Map&lt;String,String&gt;&gt;) app.getAttribute("users");
    if (users == null) {
        users = new HashMap&lt;&gt;();
        app.setAttribute("users", users);
    }
    List&lt;String&gt; auditLog = (List&lt;String&gt;) app.getAttribute("auditLog");
    if (auditLog == null) {
        auditLog = new ArrayList&lt;&gt;();
        app.setAttribute("auditLog", auditLog);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Session User
    String loggedInUser = (String) session.getAttribute("username");
    Map&lt;String,String&gt; userProfile = null;
    if (loggedInUser != null) {
        userProfile = users.get(loggedInUser);
    }

    // Helper to add audit log entry
    void audit(String action, String user) {
        String timestamp = sdf.format(new Date());
        String entry = timestamp + " - [" + action + "] by user: " + (user != null ? user : "Anonymous");
        auditLog.add(entry);
    }

    // Action handling variables
    String message = null;
    String error = null;

    String action = request.getParameter("action");
    if ("register".equals(action)) {
        // Registration processing
        String regUsername = request.getParameter("regUsername");
        String regEmail = request.getParameter("regEmail");
        String regPassword = request.getParameter("regPassword");
        String regPasswordConfirm = request.getParameter("regPasswordConfirm");

        if (regUsername == null || regUsername.trim().isEmpty() ||
            regEmail == null || regEmail.trim().isEmpty() ||
            regPassword == null || regPassword.trim().isEmpty() ||
            regPasswordConfirm == null || regPasswordConfirm.trim().isEmpty()) {
            error = "All registration fields are required.";
        } else if (!regPassword.equals(regPasswordConfirm)) {
            error = "Passwords do not match.";
        } else if (users.containsKey(regUsername)) {
            error = "Username already exists.";
        } else {
            // Save user profile info: store in map with username key
            Map&lt;String,String&gt; newUser = new HashMap&lt;&gt;();
            newUser.put("username", regUsername);
            newUser.put("email", regEmail);
            newUser.put("password", regPassword); // WARNING: storing plain text password for demo only
            newUser.put("fullname", "");
            newUser.put("bio", "");
            users.put(regUsername, newUser);
            audit("REGISTER", regUsername);
            message = "Registration successful! Please login.";
        }
    } else if ("login".equals(action)) {
        // Login processing
        String loginUsername = request.getParameter("loginUsername");
        String loginPassword = request.getParameter("loginPassword");
        if (loginUsername == null || loginUsername.trim().isEmpty() ||
            loginPassword == null || loginPassword.trim().isEmpty()) {
            error = "Both username and password are required for login.";
        } else if (!users.containsKey(loginUsername)) {
            error = "Invalid username or password.";
        } else {
            Map&lt;String,String&gt; user = users.get(loginUsername);
            if (!loginPassword.equals(user.get("password"))) {
                error = "Invalid username or password.";
            } else {
                session.setAttribute("username", loginUsername);
                audit("LOGIN", loginUsername);
                response.sendRedirect("user-auth-audit.jsp");
                return; // redirect to avoid repost
            }
        }
    } else if ("logout".equals(action)) {
        // Logout processing
        if (loggedInUser != null) {
            audit("LOGOUT", loggedInUser);
        }
        session.invalidate();
        response.sendRedirect("user-auth-audit.jsp");
        return;
    } else if ("updateProfile".equals(action)) {
        if (loggedInUser != null) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String bio = request.getParameter("bio");
            // Basic validation - allow empty optional bio
            if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                error = "Full name and email are required in profile.";
            } else {
                userProfile.put("fullname", fullName);
                userProfile.put("email", email);
                userProfile.put("bio", bio != null ? bio : "");
                audit("PROFILE UPDATE", loggedInUser);
                message = "Profile updated successfully.";
            }
        } else {
            error = "Unauthorized: Please login to update profile.";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>User Registration, Authentication & Audit Trail</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet" />
    <style>
        /* Reset and base */
        * {
            box-sizing: border-box;
        }
        body {
            margin: 0;
            font-family: 'Inter', sans-serif;
            background: #ffffff;
            color: #6b7280;
            line-height: 1.6;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        a {
            color: #2563eb;
            text-decoration: none;
            transition: color 0.3s ease;
        }
        a:hover {
            color: #1e40af;
            text-decoration: underline;
        }
        header {
            position: sticky;
            top: 0;
            background: #fff;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            padding: 1rem 2rem;
            z-index: 1000;
        }
        .container {
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
            padding: 0 2rem;
            width: 100%;
        }
        nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .logo {
            font-weight: 800;
            font-size: 1.75rem;
            color: #111827;
            user-select: none;
        }
        .nav-links {
            display: flex;
            gap: 1.5rem;
        }
        .btn-primary {
            display: inline-block;
            background: #111827;
            color: #fff;
            padding: 0.75rem 1.5rem;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1rem;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover, .btn-primary:focus {
            background: #1f2937;
            outline: none;
        }
        main {
            flex: 1;
            padding: 4rem 0 6rem 0;
        }
        h1, h2, h3 {
            color: #111827;
            margin-bottom: 0.5rem;
        }
        h1 {
            font-size: 3rem;
            font-weight: 800;
            line-height: 1.1;
            margin-bottom: 1rem;
        }
        h2 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.75rem;
        }
        /* Card style */
        .card {
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.05);
            padding: 2rem;
            margin-bottom: 3rem;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }
        label {
            display: block;
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: #374151;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"],
        textarea {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1.5px solid #d1d5db;
            border-radius: 8px;
            font-size: 1rem;
            color: #111827;
            transition: border-color 0.3s ease;
            font-family: 'Inter', sans-serif;
        }
        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus,
        textarea:focus {
            border-color: #2563eb;
            outline: none;
        }
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        .form-actions {
            margin-top: 1.5rem;
            display: flex;
            justify-content: flex-end;
        }
        .tabs {
            max-width: 600px;
            margin: 0 auto 3rem auto;
            display: flex;
            border-bottom: 2px solid #e5e7eb;
        }
        .tab {
            flex: 1;
            text-align: center;
            padding: 1rem 0;
            cursor: pointer;
            font-weight: 600;
            font-size: 1.125rem;
            color: #6b7280;
            border-bottom: 4px solid transparent;
            transition: all 0.3s ease;
            user-select: none;
        }
        .tab.active {
            border-color: #2563eb;
            color: #2563eb;
        }
        .message {
            max-width: 600px;
            margin: 1rem auto;
            padding: 1rem 1.5rem;
            border-radius: 8px;
            font-weight: 600;
        }
        .message.success {
            background-color: #dcfce7;
            color: #166534;
            border: 1px solid #4ade80;
        }
        .message.error {
            background-color: #fee2e2;
            color: #991b1b;
            border: 1px solid #f87171;
        }
        /* Audit trail list */
        .audit-log {
            max-width: 600px;
            margin: 2rem auto 0 auto;
            background: #f9fafb;
            border-radius: 12px;
            padding: 1.5rem 2rem;
            box-shadow: inset 0 0 5px rgba(0,0,0,0.05);
            font-family: 'Inter', sans-serif;
            font-size: 0.875rem;
            color: #4b5563;
            max-height: 300px;
            overflow-y: auto;
            line-height: 1.4;
            user-select: text;
        }
        /* Responsive */
        @media (max-width: 640px) {
            body {
                padding: 0 1rem;
            }
            main {
                padding: 2rem 0 4rem 0;
            }
            .card {
                padding: 1.5rem;
                margin-bottom: 2rem;
                width: 100%;
            }
            h1 {
                font-size: 2.25rem;
            }
            .nav-links {
                gap: 1rem;
            }
        }
    </style>
</head>
<body>
    <header role="banner">
        <div class="container">
            <nav role="navigation" aria-label="Main Navigation">
                <div class="logo" aria-label="Application Logo" tabindex="0">ProfileApp</div>
                <div class="nav-links" aria-live="polite">
                    <%
                        if (loggedInUser != null) {
                    %>
                        <span>Hello, <strong><%= loggedInUser %></strong></span>
                        <a href="user-auth-audit.jsp?action=logout" class="btn-primary" role="button" aria-label="Logout">Logout</a>
                    <%
                        } else {
                    %>
                        <span>Welcome, please login or register</span>
                    <%
                        }
                    %>
                </div>
            </nav>
        </div>
    </header>
    <main role="main" class="container">
        <% if (message != null) { %>
            <div class="message success" role="alert"><%= message %></div>
        <% } %>
        <% if (error != null) { %>
            <div class="message error" role="alert"><%= error %></div>
        <% } %>

        <%
            if (loggedInUser == null) {
                // Show login and registration tabs and forms
        %>
        <section aria-label="User Authentication Forms" class="card">
            <div class="tabs" role="tablist">
                <button class="tab active" id="loginTab" role="tab" aria-selected="true" aria-controls="loginPanel" tabindex="0" onclick="switchTab('login')">Login</button>
                <button class="tab" id="registerTab" role="tab" aria-selected="false" aria-controls="registerPanel" tabindex="-1" onclick="switchTab('register')">Register</button>
            </div>
            <div id="loginPanel" role="tabpanel" aria-labelledby="loginTab">
                <form method="post" novalidate>
                    <input type="hidden" name="action" value="login" />
                    <label for="loginUsername">Username</label>
                    <input type="text" id="loginUsername" name="loginUsername" autocomplete="username" required />
                    <label for="loginPassword">Password</label>
                    <input type="password" id="loginPassword" name="loginPassword" autocomplete="current-password" required />
                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Login</button>
                    </div>
                </form>
            </div>
            <div id="registerPanel" role="tabpanel" aria-labelledby="registerTab" hidden>
                <form method="post" novalidate>
                    <input type="hidden" name="action" value="register" />
                    <label for="regUsername">Username</label>
                    <input type="text" id="regUsername" name="regUsername" autocomplete="username" required />
                    <label for="regEmail">Email</label>
                    <input type="email" id="regEmail" name="regEmail" autocomplete="email" required />
                    <label for="regPassword">Password</label>
                    <input type="password" id="regPassword" name="regPassword" autocomplete="new-password" required />
                    <label for="regPasswordConfirm">Confirm Password</label>
                    <input type="password" id="regPasswordConfirm" name="regPasswordConfirm" autocomplete="new-password" required />
                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Register</button>
                    </div>
                </form>
            </div>
        </section>
        <% 
            } else {
                // User logged in, show profile and audit trail
        %>
        <section aria-label="User Profile" class="card" tabindex="0">
            <h1>Hello, <%= loggedInUser %>!</h1>
            <h2>Your Profile</h2>
            <form method="post" novalidate>
                <input type="hidden" name="action" value="updateProfile" />
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<%= userProfile.get("fullname") != null ? userProfile.get("fullname") : "" %>" required />
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="<%= userProfile.get("email") != null ? userProfile.get("email") : "" %>" required />
                <label for="bio">Bio</label>
                <textarea id="bio" name="bio" maxlength="500" placeholder="Tell us a little about yourself"><%= userProfile.get("bio") != null ? userProfile.get("bio") : "" %></textarea>
                <div class="form-actions">
                    <button type="submit" class="btn-primary">Update Profile</button>
                </div>
            </form>
        </section>
        <section aria-label="Audit Trail" class="card" tabindex="0">
            <h2>Audit Trail</h2>
            <div class="audit-log" aria-live="polite" aria-atomic="false" aria-relevant="additions">
                <%
                    if (auditLog.isEmpty()) {
                %>
                <p>No audit records available.</p>
                <%
                    } else {
                        for (int i = auditLog.size() - 1; i >= 0; i--) { // newest first
                %>
                <div><%= auditLog.get(i) %></div>
                <%
                        }
                    }
                %>
            </div>
        </section>
        <%
            }
        %>
    </main>

    <script>
        // Tab switching logic for login/register forms
        function switchTab(tab) {
            const loginTabBtn = document.getElementById('loginTab');
            const registerTabBtn = document.getElementById('registerTab');
            const loginPanel = document.getElementById('loginPanel');
            const registerPanel = document.getElementById('registerPanel');

            if (tab === 'login') {
                loginTabBtn.classList.add('active');
                registerTabBtn.classList.remove('active');
                loginTabBtn.setAttribute('aria-selected', 'true');
                registerTabBtn.setAttribute('aria-selected', 'false');
                loginTabBtn.tabIndex = 0;
                registerTabBtn.tabIndex = -1;

                loginPanel.hidden = false;
                registerPanel.hidden = true;
                document.getElementById('loginUsername').focus();
            } else {
                registerTabBtn.classList.add('active');
                loginTabBtn.classList.remove('active');
                registerTabBtn.setAttribute('aria-selected', 'true');
                loginTabBtn.setAttribute('aria-selected', 'false');
                registerTabBtn.tabIndex = 0;
                loginTabBtn.tabIndex = -1;

                registerPanel.hidden = false;
                loginPanel.hidden = true;
                document.getElementById('regUsername').focus();
            }
        }
        // Preserve tab state on page load based on errors (if registration failed, open register tab)
        window.addEventListener('DOMContentLoaded', function() {
            const errorText = "<%= error != null ? error.replace("\"", "\\\"").replace("\'", "\\\'") : "" %>";
            if (errorText.toLowerCase().includes('register') || errorText.toLowerCase().includes('passwords do not match')) {
                switchTab('register');
            } else {
                switchTab('login');
            }
        });
    </script>
</body>
</html>
