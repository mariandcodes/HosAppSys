<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
    <title>Appointments List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        .top-bar {
            text-align: right;
            margin-bottom: 20px;
        }
        .logout-button {
            background-color: #4f46e5;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1em;
            text-decoration: none;
            display: inline-block;
        }
        .logout-button:hover {
            background-color: #4338ca;
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
            text-align: center;
        }
        .section {
            background: white;
            padding: 20px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 40px;
        }
        .dashboard-buttons {
            text-align: center;
            margin-bottom: 30px;
        }
        .btn {
            background-color: #4f46e5;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            margin: 0 10px;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #4338ca;
        }
        .btn-back {
            background-color: #10b981;
        }
        .btn-back:hover {
            background-color: #059669;
        }
        .btn-secondary {
            background-color: #6b7280;
        }
        .btn-secondary:hover {
            background-color: #5b6470;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #eaeaea;
            text-align: left;
            color: #555;
        }
        th {
            background-color: #ede9fe;
            font-weight: 700;
            color: #4f46e5;
        }
        tr:hover {
            background-color: #f4f4ff;
        }
        .table-actions a {
            color: #3b82f6;
            text-decoration: none;
            margin-right: 15px;
            font-weight: 500;
        }
        .table-actions a:hover {
            text-decoration: underline;
        }
        .table-actions a.cancel {
            color: #ef4444;
        }
        .no-data {
            font-style: italic;
            color: #777;
            padding: 40px 0;
            text-align: center;
            font-size: 18px;
        }
        @media (max-width: 768px) {
            .dashboard-buttons {
                flex-direction: column;
                align-items: center;
            }
            .btn {
                margin: 5px 0;
                width: 200px;
            }
            table {
                font-size: 14px;
            }
            th, td {
                padding: 8px 10px;
            }
        }
    </style>
</head>
<body>
    <div class="top-bar">
        <a href="user?action=logout" class="logout-button">Logout</a>
    </div>

    <h1>Appointment List</h1>

    <div class="dashboard-buttons">
        <a href="dashboard" class="btn btn-back">Back to Dashboard</a>
        <a href="appointment?action=book" class="btn">Book New Appointment</a>
    </div>

    <div class="section">
        <c:choose>
            <c:when test="${not empty appointments}">
                <table>
                    <thead>
                    <tr>
                        <th>Appointment ID</th>
                        <th>Patient ID</th>
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
                            <td><c:out value="${appointment.patientId}"/></td>
                            <td><c:out value="${appointment.doctorId}"/></td>
                            <td><fmt:formatDate value="${appointment.scheduledDatetime}" pattern="yyyy-MM-dd HH:mm"/></td>
                            <td><c:out value="${appointment.status}"/></td>
                            <td><c:out value="${appointment.reason}"/></td>
                            <td class="table-actions">
                                <a href="appointment?action=view&id=${appointment.appointmentId}">View</a>
                                <a href="appointmentForm.jsp?id=${appointment.appointmentId}">Edit</a>
                                <a href="appointment?action=cancel&id=${appointment.appointmentId}&patientId=${appointment.patientId}" 
                                   class="cancel" onclick="return confirm('Are you sure you want to cancel this appointment?')">Cancel</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="no-data">No appointments found.</div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>