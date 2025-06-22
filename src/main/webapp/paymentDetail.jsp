<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Payment Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9fafb;
            color: #333;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
        }
        dl {
            display: grid;
            grid-template-columns: 180px 1fr;
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
            display: flex;
            gap: 20px;
        }
        .button {
            padding: 12px 24px;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            font-weight: 600;
        }
        .button:hover {
            background-color: #4338ca;
        }
    </style>
</head>
<body>
    <h1>Payment Details</h1>
    <c:choose>
        <c:when test="${not empty payment}">
            <dl>
                <dt>Payment ID:</dt>
                <dd><c:out value="${payment.paymentId}"/></dd>

                <dt>Patient ID:</dt>
                <dd><c:out value="${payment.patientId}"/></dd>

                <dt>Appointment ID:</dt>
                <dd><c:out value="${payment.appointmentId}"/></dd>

                <dt>Amount:</dt>
                <dd>$<c:out value="${payment.amount}"/></dd>

                <dt>Method:</dt>
                <dd><c:out value="${payment.method}"/></dd>

                <dt>Transaction ID:</dt>
                <dd><c:out value="${payment.transactionId}"/></dd>

                <dt>Status:</dt>
                <dd><c:out value="${payment.status}"/></dd>

                <dt>Paid At:</dt>
                <dd><fmt:formatDate value="${payment.paidAt}" pattern="yyyy-MM-dd HH:mm"/></dd>
            </dl>

            <div class="actions">
                <a href="payment?action=update&id=${payment.paymentId}" class="button">Edit</a>
                <a href="payment?action=delete&id=${payment.paymentId}" class="button" style="background-color: #dc2626;">Delete</a>
                <a href="payment?action=listByPatient&patientId=${payment.patientId}" class="button" style="background-color: #6b7280;">Back to List</a>
            </div>
        </c:when>
        <c:otherwise>
            <p>Payment details not found.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>