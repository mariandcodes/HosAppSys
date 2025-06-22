<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Doctor Utilization Report</title>
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
            margin-bottom: 30px;
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
            color: #586069;
            text-align: center;
        }
        th {
            background-color: #f6f8fa;
            font-weight: 700;
            color: #24292e;
        }
        tr:hover {
            background-color: #f1f8ff;
        }
        .no-data {
            text-align: center;
            margin-top: 40px;
            font-style: italic;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <h1>Doctor Utilization</h1>

    <c:choose>
        <c:when test="${not empty utilization}">
            <table>
                <thead>
                    <tr>
                        <th>Doctor ID</th>
                        <th>Doctor Name</th>
                        <th>Appointments Count</th>
                        <th>Total Available Slots</th>
                        <th>Utilization (%)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${utilization}">
                        <tr>
                            <td><c:out value="${row.doctorId}" /></td>
                            <td><c:out value="${row.doctorName}" /></td>
                            <td><c:out value="${row.appointmentsCount}" /></td>
                            <td><c:out value="${row.totalSlots}" /></td>
                            <td><fmt:formatNumber value="${row.utilizationPercent}" maxFractionDigits="2" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-data">No utilization data available.</p>
        </c:otherwise>
    </c:choose>

</body>
</html>