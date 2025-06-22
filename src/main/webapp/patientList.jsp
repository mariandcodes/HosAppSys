<%-- 
    Document   : patientList
    Created on : Jun 14, 2025, 11:15:38 PM
    Author     : Matthew
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <title>Patient Search Results</title>
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
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 14px 20px;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
            color: #4a4a4a;
        }
        th {
            background-color: #f3f4f6;
            font-weight: 700;
            color: #1f2937;
        }
        tr:hover {
            background-color: #eef2ff;
        }
        a {
            color: #4f46e5;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .no-results {
            margin-top: 20px;
            font-style: italic;
            color: #7f8c8d;
        }
    </style>
</head>
<body>
    <h1>Patient Search Results</h1>

    <c:choose>
        <c:when test="${not empty patients}">
            <table>
                <thead>
                <tr>
                    <th>Patient ID</th>
                    <th>Full Name</th>
                    <th>Date of Birth</th>
                    <th>Gender</th>
                    <th>Contact Info</th>
                    <th>Status</th>
                    <th>Profile</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="patient" items="${patients}">
                    <tr>
                        <td><c:out value="${patient.patientId}"/></td>
                        <td><c:out value="${patient.fullName}"/></td>
                        <td><fmt:formatDate value="${patient.dob}" pattern="yyyy-MM-dd"/></td>
                        <td><c:out value="${patient.gender}"/></td>
                        <td><c:out value="${patient.contactInfo}"/></td>
                        <td><c:out value="${patient.status}"/></td>
                        <td><a href="patient?action=view&id=${patient.patientId}">View</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-results">No patients found matching your search.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>