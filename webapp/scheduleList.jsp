<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Doctor Schedules</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1000px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }
        .message {
            color: green;
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            border-radius: 8px;
            overflow: hidden; /* Ensures rounded corners apply to content */
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
            text-transform: uppercase;
            font-size: 0.9em;
        }
        tr:nth-child(even) {
            background-color: #fcfcff;
        }
        tr:hover {
            background-color: #f4f4ff;
        }
        .no-data {
            font-style: italic;
            color: #777;
            padding: 20px 0;
            text-align: center;
            background: white;
            border-radius: 8px;
            margin-top: 20px;
        }
        .action-buttons {
            display: flex;
            gap: 8px;
            justify-content: flex-end; /* Align to right */
        }
        .action-buttons a, .action-buttons button {
            background-color: #4f46e5;
            color: white;
            padding: 8px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 0.9em;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.2s ease;
        }
        .action-buttons a.add-new {
            background-color: #28a745; /* Green for add */
        }
        .action-buttons a:hover, .action-buttons button:hover {
            background-color: #4338ca;
        }
        .action-buttons button.delete {
            background-color: #dc3545; /* Red for delete */
        }
        .action-buttons button.delete:hover {
            background-color: #c82333;
        }
    </style>
    <script>
        // Simple confirmation for delete
        function confirmDelete(scheduleId) {
            if (confirm('Are you sure you want to delete this schedule?')) {
                window.location.href = 'schedule?action=delete&id=' + scheduleId;
            }
        }
    </script>
</head>
<body>
    <h1>Doctor Schedules</h1>

    <%-- Display messages --%>
    <c:if test="${not empty requestScope.message}">
        <div class="message">${requestScope.message}</div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div class="error">${requestScope.error}</div>
    </c:if>

    <div class="action-buttons">
        <a href="schedule?action=create" class="add-new">Add New Schedule</a>
        <a href="dashboard">Back to Dashboard</a>
    </div>

    <c:if test="${not empty requestScope.schedules}">
        <table>
            <thead>
                <tr>
                    <th>Schedule ID</th>
                    <th>Doctor ID</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="schedule" items="${requestScope.schedules}">
                    <tr>
                        <td>${schedule.id}</td>
                        <td>${schedule.doctorId}</td>
                        <td><fmt:formatDate value="${schedule.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td><fmt:formatDate value="${schedule.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td class="action-buttons">
                            <a href="schedule?action=view&id=${schedule.id}">View</a>
                            <a href="schedule?action=edit&id=${schedule.id}">Edit</a>
                            <button class="delete" onclick="confirmDelete(${schedule.id})">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty requestScope.schedules}">
        <div class="no-data">No schedules found.</div>
    </c:if>
</body>
</html>
