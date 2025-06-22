<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>User Login</title>
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
        input[type="text"], input[type="password"] {
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
    <h1>Login</h1>
    <%-- Display error message from servlet attribute or parameter --%>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="error">${param.error}</div>
    </c:if>
    <%-- Display success message from servlet redirect parameter --%>
    <c:if test="${not empty param.message}">
        <div class="message">${param.message}</div>
    </c:if>

    <form action="user" method="post">
        <input type="hidden" name="action" value="login"/>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required/>

        <label for="password">Password:</label>
        <div class="password-container">
            <input type="password" id="password" name="password" required/>
            <button type="button" class="toggle-password" id="togglePassword" onclick="togglePasswordVisibility('password', 'togglePassword')">Show</button>
        </div>

        <label>
            <input type="checkbox" name="rememberMe"/> Remember Me
        </label>

        <input type="submit" value="Login"/>
    </form>
    <p style="text-align: center;">Don't have an account? <a href="register.jsp">Register here</a></p>
</body>
</html>
