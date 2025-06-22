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
    <h1>Patient Search Results</h1>
    <c:choose>
        <c:when test="${not empty patients}">
            <table>
                <thead>
                    <tr>
                        <th>Patient ID</th>
                        <th>Full Name</th>
                        <th>Date of Birth</th>
                        <th>Contact Info</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="patient" items="${patients}">
                        <tr>
                            <td><c:out value="${patient.patientId}"/></td>
                            <td><c:out value="${patient.fullName}"/></td>
                            <td><c:out value="${patient.dob}"/></td>
                            <td><c:out value="${patient.contactInfo}"/></td>
                            <td>
                                <a href="staff?action=viewPatient&id=${patient.patientId}">View Profile</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-data">No patients found.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>