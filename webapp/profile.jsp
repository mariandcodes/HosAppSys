<%-- 
    Document   : profile
    Created on : Jun 14, 2025, 11:18:18 PM
    Author     : Matthew
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>User Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 40px auto;
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
        }
        label {
            display: block;
            margin: 10px 0 5px;
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
</head>
<body>
    <h1>User Profile</h1>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <form action="user" method="post">
        <input type="hidden" name="action" value="updateProfile"/>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${user.username}" readonly/>

        <label for="newPassword">New Password:</label>
        <input type="password" id="newPassword" name="newPassword"/>

        <input type="submit" value="Update Profile"/>
    </form>
    <p style="text-align: center;"><a href="user?action=logout">Logout</a></p>
</body>
</html>