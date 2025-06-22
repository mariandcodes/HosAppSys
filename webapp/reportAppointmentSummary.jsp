<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Appointment Summary Report</title>
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
            text-align: center;
            color: #586069;
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
    <h1>Appointment Summary</h1>

    <c:choose>
        <c:when test="${not empty summary}">
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Scheduled</th>
                        <th>Completed</th>
                        <th>Cancelled</th>
                        <th>No Show</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${summary}">
                        <tr>
                            <td><fmt:formatDate value="${row.day}" pattern="yyyy-MM-dd" /></td>
                            <td><c:out value="${row.scheduledCount}" /></td>
                            <td><c:out value="${row.completedCount}" /></td>
                            <td><c:out value="${row.cancelledCount}" /></td>
                            <td><c:out value="${row.noShowCount}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-data">No appointment summary data available.</p>
        </c:otherwise>
    </c:choose>

</body>
</html>