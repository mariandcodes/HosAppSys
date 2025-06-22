<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title><c:choose>
        <c:when test="${not empty payment}">Edit Payment</c:when>
        <c:otherwise>Record New Payment</c:otherwise>
    </c:choose></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9fafb;
            color: #333;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 24px;
            color: #2c3e50;
        }
        form label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
        }
        form input[type="text"],
        form input[type="number"],
        form input[type="datetime-local"],
        form select {
            width: 100%;
            padding: 8px 12px;
            margin-top: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        form input[type="submit"] {
            margin-top: 28px;
            width: 100%;
            padding: 12px 0;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
        }
        form input[type="submit"]:hover {
            background-color: #4338ca;
        }
        .back-link {
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
<h1><c:choose>
        <c:when test="${not empty payment}">Edit Payment</c:when>
        <c:otherwise>Record New Payment</c:otherwise>
    </c:choose></h1>

<form action="payment" method="post">
    <input type="hidden" name="action" value="<c:choose><c:when test='${not empty payment}'>update</c:when><c:otherwise>record</c:otherwise></c:choose>"/>
    <c:if test="${not empty payment}">
        <input type="hidden" name="id" value="${payment.paymentId}"/>
    </c:if>

    <label for="patientId">Patient ID</label>
    <input type="number" id="patientId" name="patientId" required value="${payment.patientId}"/>

    <label for="appointmentId">Appointment ID</label>
    <input type="number" id="appointmentId" name="appointmentId" required value="${payment.appointmentId}"/>

    <label for="amount">Amount</label>
    <input type="number" step="0.01" id="amount" name="amount" required value="${payment.amount}"/>

    <label for="method">Payment Method</label>
    <input type="text" id="method" name="method" value="${payment.method}"/>

    <label for="transactionId">Transaction ID</label>
    <input type="text" id="transactionId" name="transactionId" value="${payment.transactionId}"/>

    <label for="status">Status</label>
    <input type="text" id="status" name="status" value="${payment.status}"/>

    <label for="paidAt">Paid At (YYYY-MM-DDThh:mm)</label>
    <input type="datetime-local" id="paidAt" name="paidAt" value="<c:if test='${not empty payment.paidAt}'><fmt:formatDate value='${payment.paidAt}' pattern='yyyy-MM-dd\'T\'HH:mm'/></c:if>"/>

    <input type="submit" value="<c:choose><c:when test='${not empty payment}'>Update Payment</c:when><c:otherwise>Record Payment</c:otherwise></c:choose>"/>
</form>

<div class="back-link">
    <a href="payment?action=listByPatient&patientId=${payment.patientId}">Back to Payment List</a>
</div>
</body>
</html>