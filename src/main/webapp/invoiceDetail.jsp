<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Invoice Details</title>
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
            color: #2c3e50;
            margin-bottom: 24px;
            text-align: center;
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
            text-align: center;
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
    <h1>Invoice Details</h1>
    <c:choose>
        <c:when test="${not empty invoice}">
            <dl>
                <dt>Invoice ID:</dt>
                <dd><c:out value="${invoice.invoiceId}"/></dd>

                <dt>Payment ID:</dt>
                <dd><c:out value="${invoice.paymentId}"/></dd>

                <dt>Amount:</dt>
                <dd>$<c:out value="${invoice.amount}"/></dd>

                <dt>Issued At:</dt>
                <dd><fmt:formatDate value="${invoice.issuedAt}" pattern="yyyy-MM-dd HH:mm"/></dd>

                <dt>Invoice PDF:</dt>
                <dd><a href="${invoice.invoicePdfPath}" target="_blank">Download PDF</a></dd>
            </dl>
            <div class="actions">
                <a href="invoice?action=listByPayment&paymentId=${invoice.paymentId}" class="button">Back to Invoices</a>
            </div>
        </c:when>
        <c:otherwise>
            <p>Invoice not found.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>