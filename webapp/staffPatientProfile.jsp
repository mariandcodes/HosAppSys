<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Patient Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
            text-align: center;
        }
        dl {
            display: grid;
            grid-template-columns: 150px 1fr;
            gap: 12px 24px;
            background: white;
            padding: 25px;
            border-radius: 12px;
        }
        dt {
            font-weight: bold;
            color: #444e56;
            align-self: center;
        }
        dd {
            margin: 0;
            color: #586069;
        }
        .actions {
            margin-top: 30px;
            text-align: center;
        }
        .button {
            padding: 12px 24px;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            cursor: pointer;
            display: inline-block;
            margin: 0 10px;
        }
        .button:hover {
            background-color: #4338ca;
        }
    </style>
</head>
<body>
    <h1>Patient Profile</h1>
    <c:choose>
        <c:when test="${not empty patient}">
            <dl>
                <dt>Full Name:</dt>
                <dd><c:out value="${patient.fullName}"/></dd>

                <dt>Date of Birth:</dt>
                <dd><c:out value="${patient.dob}"/></dd>

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

            <div class="actions">
                <a href="staff?action=listAppointments&patientId=${patient.patientId}" class="button">View Appointments</a>
                <a href="staff?action=searchPatient" class="button" style="background-color: #6b7280;">Back to Search</a>
            </div>
        </c:when>
        <c:otherwise>
            <p>Patient details not found.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>