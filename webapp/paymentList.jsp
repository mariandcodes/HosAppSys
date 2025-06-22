<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Payment List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9fafb;
            color: #333;
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
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
            margin-top: 20px;
            font-style: italic;
            color: #6c757d;
        }
    </style>
</head>
<body>
<h1>Payment List</h1>

<c:choose>
    <c:when test="${not empty payments}">
        <table>
            <thead>
            <tr>
                <th>Payment ID</th>
                <th>Patient ID</th>
                <th>Appointment ID</th>
                <th>Amount</th>
                <th>Method</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="payment" items="${payments}">
                <tr>
                    <td><c:out value="${payment.paymentId}"/></td>
                    <td><c:out value="${payment.patientId}"/></td>
                    <td><c:out value="${payment.appointmentId}"/></td>
                    <td>$<c:out value="${payment.amount}"/></td>
                    <td><c:out value="${payment.method}"/></td>
                    <td><c:out value="${payment.status}"/></td>
                    <td>
                        <a href="payment?action=view&id=${payment.paymentId}">View</a>
                        |
                        <a href="payment?action=update&id=${payment.paymentId}">Edit</a>
                        |
                        <a href="payment?action=delete&id=${payment.paymentId}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p class="no-data">No payments found.</p>
    </c:otherwise>
</c:choose>
</body>
</html>