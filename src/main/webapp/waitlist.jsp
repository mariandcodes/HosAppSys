<%-- 
    Document   : waitlist
    Created on : Jun 15, 2025, 1:09:32 AM
    Author     : Matthew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Waitlist</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9fafb;
            color: #333;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 24px;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 14px 20px;
            border-bottom: 1px solid #e1e4e8;
            text-align: left;
            color: #586069;
        }
        th {
            background-color: #f6f8fa;
            font-weight: 600;
            color: #24292e;
        }
        tr:hover {
            background-color: #f1f8ff;
        }
        .no-data {
            margin-top: 20px;
            font-style: italic;
            color: #6c757d;
            text-align: center;
        }
        form {
            margin-top: 20px;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        form label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
        }
        form input[type="number"],
        form input[type="date"] {
            width: 100%;
            padding: 10px 12px;
            margin-top: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        form input[type="submit"] {
            margin-top: 20px;
            padding: 12px 0;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            width: 100%;
        }
        form input[type="submit"]:hover {
            background-color: #4338ca;
        }
    </style>
</head>
<body>
    <h1>Waitlist</h1>
    <c:choose>
        <c:when test="${not empty waitlist}">
            <table>
                <thead>
                <tr>
                    <th>Patient ID</th>
                    <th>Requested Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="entry" items="${waitlist}">
                    <tr>
                        <td><c:out value="${entry.patientId}"/></td>
                        <td><c:out value="${entry.requestedDate}"/></td>
                        <td>
                            <form action="waitlist" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="remove"/>
                                <input type="hidden" name="id" value="${entry.id}"/>
                                <input type="submit" value="Remove"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-data">No entries in the waitlist.</p>
        </c:otherwise>
    </c:choose>

    <form action="waitlist" method="post">
        <input type="hidden" name="action" value="add"/>
        <label for="patientId">Patient ID</label>
        <input type="number" id="patientId" name="patientId" required/>

        <label for="doctorId">Doctor ID</label>
        <input type="number" id="doctorId" name="doctorId" required/>

        <label for="requestedDate">Requested Date</label>
        <input type="date" id="requestedDate" name="requestedDate" required/>

        <input type="submit" value="Add to Waitlist"/>
    </form>
</body>
</html>