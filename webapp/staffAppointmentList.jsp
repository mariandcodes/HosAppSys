<%-- 
    Document   : staffAppointmentList
    Created on : Jun 15, 2025, 1:40:42 AM
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Patient Appointments</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
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
        a {
            color: #0366d6;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .no-data {
            text-align: center;
            margin-top: 20px;
            font-style: italic;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <h1>Patient Appointments</h1>
    <c:choose>
        <c:when test="${not empty appointments}">
            <table>
                <thead>
                <tr>
                    <th>Appointment ID</th>
                    <th>Doctor ID</th>
                    <th>Scheduled Date & Time</th>
                    <th>Status</th>
                    <th>Reason</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="appointment" items="${appointments}">
                    <tr>
                        <td><c:out value="${appointment.appointmentId}"/></td>
                        <td><c:out value="${appointment.doctorId}"/></td>
                        <td><fmt:formatDate value="${appointment.scheduledDatetime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td><c:out value="${appointment.status}"/></td>
                        <td><c:out value="${appointment.reason}"/></td>
                        <td>
                            <a href="appointment?action=view&id=${appointment.appointmentId}">View</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-data">No appointments found for this patient.</p>
        </c:otherwise>
    </c:choose>
    <div style="text-align:center; margin-top: 20px;">
        <a href="staff?action=viewPatient&id=${param.patientId}">Back to Patient Profile</a>
    </div>
</body>
</html>