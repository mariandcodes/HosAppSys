<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>User Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 400px;
            margin: 100px auto;
            padding: 20px;
            background: #f9fafb;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #2c3e50;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 15px; /* Added margin */
        }
        .message { /* Added style for success messages */
            color: green;
            text-align: center;
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        .password-container { /* Style for password input and toggle */
            position: relative;
            margin-bottom: 15px;
        }
        .password-container input[type="password"],
        .password-container input[type="text"] {
            width: 100%;
            padding-right: 40px; /* Make space for the button */
            box-sizing: border-box; /* Include padding in width */
        }
        .toggle-password {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            cursor: pointer;
            color: #777;
            font-size: 0.9em;
            padding: 0;
        }
        input[type="text"], input[type="password"], select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background: #4f46e5;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background: #4338ca;
        }
    </style>
    <script>
        function validateForm() {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const passwordMismatchDiv = document.getElementById('passwordMismatchError');

            if (password !== confirmPassword) {
                passwordMismatchDiv.innerText = "Passwords do not match.";
                passwordMismatchDiv.style.display = 'block'; // Show the error message
                return false;
            } else {
                passwordMismatchDiv.innerText = ""; // Clear the error if they match
                passwordMismatchDiv.style.display = 'none'; // Hide the error message
            }
            return true;
        }

        // Function to toggle password visibility
        function togglePasswordVisibility(fieldId, buttonId) {
            const passwordField = document.getElementById(fieldId);
            const toggleButton = document.getElementById(buttonId);
            if (passwordField.type === "password") {
                passwordField.type = "text";
                toggleButton.textContent = "Hide";
            } else {
                passwordField.type = "password";
                toggleButton.textContent = "Show";
            }
        }
    </script>
</head>
<body>
    <h1>Register</h1>
    <%-- Display error message from servlet attribute (on forward) --%>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <%-- Display error message from request parameter (less common for forward, but good to have) --%>
    <c:if test="${not empty param.error}">
        <div class="error">${param.error}</div>
    </c:if>

    <form action="user" method="post" onsubmit="return validateForm();">
        <input type="hidden" name="action" value="register"/>
        
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required value="${param.username != null ? param.username : ''}"/>

        <label for="password">Password:</label>
        <div class="password-container">
            <input type="password" id="password" name="password" required/>
            <button type="button" class="toggle-password" id="togglePassword" onclick="togglePasswordVisibility('password', 'togglePassword')">Show</button>
        </div>

        <label for="confirmPassword">Confirm Password:</label>
        <div class="password-container">
            <input type="password" id="confirmPassword" name="confirmPassword" required/>
            <button type="button" class="toggle-password" id="toggleConfirmPassword" onclick="togglePasswordVisibility('confirmPassword', 'toggleConfirmPassword')">Show</button>
        </div>
        <div id="passwordMismatchError" class="error" style="display:none;"></div>

        <label for="role">Role:</label>
        <select id="role" name="role" required>
            <option value="ROLE_PATIENT" selected>Patient</option>
            <option value="ROLE_DOCTOR">Doctor</option>
            <option value="ROLE_RECEPTIONIST">Receptionist</option>
            <%-- Admin role is excluded for security reasons from self-registration --%>
        </select>

        <input type="submit" value="Register"/>
    </form>
    <p style="text-align: center;">Already have an account? <a href="login.jsp">Login here</a></p>
</body>
</html>
