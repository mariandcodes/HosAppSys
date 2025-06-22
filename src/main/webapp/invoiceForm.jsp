<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Generate Invoice</title>
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
        form input[type="number"] {
            width: 100%;
            padding: 10px 12px;
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
    </style>
</head>
<body>
<h1>Generate Invoice</h1>

<form action="invoice" method="post">
    <input type="hidden" name="action" value="generate"/>

    <label for="paymentId">Payment ID</label>
    <input type="number" id="paymentId" name="paymentId" required/>

    <label for="amount">Amount</label>
    <input type="number" id="amount" name="amount" required step="0.01"/>

    <label for="invoicePdfPath">Invoice PDF Path</label>
    <input type="text" id="invoicePdfPath" name="invoicePdfPath" required/>

    <input type="submit" value="Generate Invoice"/>
</form>

<div class="back-link">
    <a href="invoice?action=listByPayment&paymentId=${paymentId}">Back to Invoice List</a>
</div>
</body>
</html>