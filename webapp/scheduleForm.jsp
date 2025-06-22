<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title><c:choose><c:when test="${not empty schedule}">Edit Schedule</c:when><c:otherwise>Create Schedule</c:otherwise></c:choose></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        .form-container {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #4f46e5;
        }
        input[type="text"], input[type="datetime-local"], select, textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #e5e7eb;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        input[type="text"]:focus, input[type="datetime-local"]:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #4f46e5;
        }
        textarea {
            height: 100px;
            resize: vertical;
        }
        .button-group {
            text-align: center;
            margin-top: 30px;
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
        .btn-secondary {
            background-color: #6b7280;
        }
        .btn-secondary:hover {
            background-color: #5b6470;
        }
        .error {
            color: #ef4444;
            background-color: #fee2e2;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .message {
            color: #10b981;
            background-color: #d1fae5;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .required {
            color: #ef4444;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1><c:choose><c:when test="${not empty schedule}">Edit Schedule</c:when><c:otherwise>Create New Schedule</c:otherwise></c:choose></h1>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>

        <form method="post" action="schedule">
            <input type="hidden" name="action" value="<c:choose><c:when test='${not empty schedule}'>update</c:when><c:otherwise>create</c:otherwise></c:choose>">
            <c:if test="${not empty schedule}">
                <input type="hidden" name="id" value="${schedule.id}">
            </c:if>

            <div class="form-group">
                <label for="doctorId">Doctor <span class="required">*</span></label>
                <select id="doctorId" name="doctorId" required>
                    <option value="">Select a doctor</option>
                    <c:forEach var="doctor" items="${doctors}">
                        <option value="${doctor.doctorId}" 
                                <c:if test="${doctor.doctorId == schedule.doctorId}">selected</c:if>>
                            Dr. ${doctor.fullName} (ID: ${doctor.doctorId})