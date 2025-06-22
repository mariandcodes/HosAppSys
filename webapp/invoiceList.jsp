<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Invoice List</title>
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
            text-align: center;
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
            text-align: center;
        }
    </style>
</head>
<body>
<h1>Invoice List</h1>

<c:choose>
    <c:when test="${not empty invoices}">
        <table>
            <thead>
            <tr>
                <th>Invoice ID</th>
                <th>Payment ID</th>
                <th>Amount</th>
                <th>Issued At</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="invoice" items="${invoices}">
                <tr>
                    <td><c:out value="${invoice.invoiceId}"/></td>
                    <td><c:out value="${invoice.paymentId}"/></td>
                    <td>$<c:out value="${invoice.amount}"/></td>
                    <td><fmt:formatDate value="${invoice.issuedAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td>
                        <a href="invoice?action=view&id=${invoice.invoiceId}">View</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p class="no-data">No invoices found.</p>
    </c:otherwise>
</c:choose>
</body>
</html>