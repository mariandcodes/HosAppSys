<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Search Payments</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f9fafb;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            color: #333;
        }
        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }
        form label {
            display: block;
            margin-top: 15px;
            font-weight: 600;
        }
        form input[type="text"],
        form input[type="number"],
        form input[type="date"] {
            width: 100%;
            padding: 10px 12px;
            margin-top: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }
        form input[type="submit"] {
            margin-top: 28px;
            width: 100%;
            padding: 14px 0;
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
            font-weight: 600;
            text-decoration: none;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>Search Payments</h1>
    <form action="payment" method="get">
        <input type="hidden" name="action" value="listByPatient"/>
        
        <label for="patientId">Patient ID</label>
        <input type="number" id="patientId" name="patientId" placeholder="Enter Patient ID" />
        
        <label for="appointmentId">Appointment ID</label>
        <input type="number" id="appointmentId" name="appointmentId" placeholder="Enter Appointment ID" />
        
        <label for="date">Payment Date</label>
        <input type="date" id="date" name="date" />
        
        <input type="submit" value="Search Payments" />
    </form>
    <div class="back-link">
        <a href="dashboard.jsp">Back to Dashboard</a>
    </div>
</body>
</html>