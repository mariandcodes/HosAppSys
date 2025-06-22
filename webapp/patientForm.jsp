<%-- 
    Document   : patientForm
    Created on : Jun 14, 2025, 11:20:11 PM
    Author     : Matthew
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title><c:choose>
            <c:when test="${not empty patient}">Edit Patient</c:when>
            <c:otherwise>New Patient</c:otherwise>
        </c:choose>
    </title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            color: #333;
        }
        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }
        form label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
        }
        form input[type="text"],
        form input[type="date"],
        form select {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }
        form input[type="submit"] {
            margin-top: 30px;
            width: 100%;
            padding: 14px;
            background: #4f46e5;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 700;
            cursor: pointer;
            transition: background 0.3s ease;
        }
        form input[type="submit"]:hover {
            background: #4338ca;
        }
        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
        .back-link a {
            color: #4f46e5;
            text-decoration: none;
            font-weight: 600;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>
        <c:choose>
            <c:when test="${not empty patient}">Edit Patient</c:when>
            <c:otherwise>New Patient</c:otherwise>
        </c:choose>
    </h1>

    <form action="patient" method="post">
        <input type="hidden" name="action" value="<c:choose><c:when test='${not empty patient}'>update</c:when><c:otherwise>create</c:otherwise></c:choose>" />
        <c:if test="${not empty patient}">
            <input type="hidden" name="id" value="${patient.patientId}" />
        </c:if>

        <label for="userId">User ID</label>
        <input type="text" id="userId" name="userId" required value="<c:out value='${patient.userId}'/>" />

        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" name="fullName" required value="<c:out value='${patient.fullName}'/>" />

        <label for="dob">Date of Birth</label>
        <input type="date" id="dob" name="dob" required value="<c:if test='${not empty patient.dob}'><fmt:formatDate value='${patient.dob}' pattern='yyyy-MM-dd'/></c:if>" />

        <label for="gender">Gender</label>
        <select id="gender" name="gender">
            <option value="" <c:if test='${empty patient.gender}'>selected</c:if>>--Select--</option>
            <option value="Male" <c:if test='${patient.gender == "Male"}'>selected</c:if>>Male</option>
            <option value="Female" <c:if test='${patient.gender == "Female"}'>selected</c:if>>Female</option>
            <option value="Other" <c:if test='${patient.gender == "Other"}'>selected</c:if>>Other</option>
        </select>

        <label for="contactInfo">Contact Info</label>
        <input type="text" id="contactInfo" name="contactInfo" value="<c:out value='${patient.contactInfo}'/>" />

        <label for="address">Address</label>
        <input type="text" id="address" name="address" value="<c:out value='${patient.address}'/>" />

        <label for="emergencyContact">Emergency Contact</label>
        <input type="text" id="emergencyContact" name="emergencyContact" value="<c:out value='${patient.emergencyContact}'/>" />

        <label for="insuranceProvider">Insurance Provider</label>
        <input type="text" id="insuranceProvider" name="insuranceProvider" value="<c:out value='${patient.insuranceProvider}'/>" />

        <label for="insurancePolicy">Insurance Policy</label>
        <input type="text" id="insurancePolicy" name="insurancePolicy" value="<c:out value='${patient.insurancePolicy}'/>" />

        <label for="status">Status</label>
        <select id="status" name="status">
            <option value="ACTIVE" <c:if test='${patient.status == "ACTIVE"}'>selected</c:if>>Active</option>
            <option value="INACTIVE" <c:if test='${patient.status == "INACTIVE"}'>selected</c:if>>Inactive</option>
            <option value="BLOCKED" <c:if test='${patient.status == "BLOCKED"}'>selected</c:if>>Blocked</option>
            <option value="DECEASED" <c:if test='${patient.status == "DECEASED"}'>selected</c:if>>Deceased</option>
        </select>

        <input type="submit" value="<c:choose><c:when test='${not empty patient}'>Update Patient</c:when><c:otherwise>Create Patient</c:otherwise></c:choose>"/>
    </form>

    <div class="back-link">
        <a href="patient?action=search">Back to Patient List</a>
    </div>
</body>
</html>