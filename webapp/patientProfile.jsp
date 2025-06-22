<%-- 
    Document   : PatienProfile
    Created on : Jun 14, 2025, 11:13:48 PM
    Author     : Matthew
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Patient Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px auto;
            max-width: 800px;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 25px;
        }
        dl {
            display: grid;
            grid-template-columns: 150px 1fr;
            gap: 10px 20px;
            background: white;
            padding: 25px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        dt {
            font-weight: bold;
            color: #34495e;
            align-self: center;
        }
        dd {
            margin: 0;
            color: #5a6a7a;
        }
        .btn {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 24px;
            background: #4f46e5;
            color: white;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s ease;
        }
        .btn:hover {
            background: #4338ca;
        }
        .header {
            margin-bottom: 24px;
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Patient Profile</h1>
    </div>

    <c:choose>
      <c:when test="${not empty patient}">
        <dl>
            <dt>Full Name:</dt>
            <dd><c:out value="${patient.fullName}"/></dd>

            <dt>Date of Birth:</dt>
            <dd><fmt:formatDate value="${patient.dob}" pattern="yyyy-MM-dd"/></dd>

            <dt>Gender:</dt>
            <dd><c:out value="${patient.gender}"/></dd>

            <dt>Contact Info:</dt>
            <dd><c:out value="${patient.contactInfo}"/></dd>

            <dt>Address:</dt>
            <dd><c:out value="${patient.address}"/></dd>

            <dt>Emergency Contact:</dt>
            <dd><c:out value="${patient.emergencyContact}"/></dd>

            <dt>Insurance Provider:</dt>
            <dd><c:out value="${patient.insuranceProvider}"/></dd>

            <dt>Insurance Policy:</dt>
            <dd><c:out value="${patient.insurancePolicy}"/></dd>

            <dt>Status:</dt>
            <dd><c:out value="${patient.status}"/></dd>
        </dl>

        <a href="patient?action=edit&id=${patient.patientId}" class="btn">Edit Profile</a>
      </c:when>
      <c:otherwise>
        <p>Sorry, patient details not found.</p>
      </c:otherwise>
    </c:choose>
</body>
</html>

