<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>${dashboardTitle}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 24px;
            text-align: center;
        }
        .section {
            background: white;
            padding: 20px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 40px;
        }
        .section h2 {
            margin-top: 0;
            color: #4f46e5;
            border-bottom: 2px solid #c7d2fe;
            padding-bottom: 8px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
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
        }
        tr:hover {
            background-color: #f4f4ff;
        }
        .no-data {
            font-style: italic;
            color: #777;
            padding: 20px 0;
            text-align: center;
        }
        .dashboard-buttons {
            text-align: center;
            margin-top: 20px;
            margin-bottom: 20px;
        }
        .dashboard-buttons button, .dashboard-buttons a, .logout-button { /* Added .logout-button */
            background-color: #4f46e5;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1em;
            margin: 0 10px;
            text-decoration: none; /* For anchor tags */
            display: inline-block; /* For anchor tags */
        }
        .dashboard-buttons button:hover, .dashboard-buttons a:hover, .logout-button:hover { /* Added .logout-button */
            background-color: #4338ca;
        }
        .table-actions button {
            background-color: #3b82f6; /* Blue for view */
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 5px;
        }
        .table-actions button.cancel {
            background-color: #ef4444; /* Red for cancel */
        }
        .table-actions button:hover {
            opacity: 0.9;
        }
        .top-bar { /* Style for positioning the logout button */
            text-align: right;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="top-bar">
        <a href="user?action=logout" class="logout-button">Logout</a>
    </div>

    <h1>${dashboardTitle}</h1>

    <c:choose>
        <c:when test="${dashboardTitle == 'Admin Dashboard'}">
            <div class="section">
                <h2>Appointment Summary</h2>
                <c:if test="${not empty appointmentSummary}">
                    <table>
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Scheduled</th>
                                <th>Completed</th>
                                <th>Cancelled</th>
                                <th>No-Show</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${appointmentSummary}">
                                <tr>
                                    <td><fmt:formatDate value="${row.day}" pattern="yyyy-MM-dd"/></td>
                                    <td>${row.scheduledCount}</td>
                                    <td>${row.completedCount}</td>
                                    <td>${row.cancelledCount}</td>
                                    <td>${row.noShowCount}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty appointmentSummary}">
                    <div class="no-data">No appointment summary data available.</div>
                </c:if>
            </div>

            <div class="section">
                <h2>Revenue Reports</h2>
                <c:if test="${not empty revenueReports}">
                    <table>
                        <thead>
                            <tr>
                                <th>Doctor ID</th>
                                <th>Doctor Name</th>
                                <th>Total Revenue</th>
                                <th>Payment Count</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${revenueReports}">
                                <tr>
                                    <td>${row.doctorId}</td>
                                    <td>${row.doctorName}</td>
                                    <td>$<fmt:formatNumber value="${row.totalRevenue}" maxFractionDigits="2"/></td>
                                    <td>${row.paymentCount}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty revenueReports}">
                    <div class="no-data">No revenue data available.</div>
                </c:if>
            </div>
        </c:when>
    
        <c:when test="${dashboardTitle == 'Manager Dashboard'}">
            <div class="section">
                <h2>Manager Dashboard Overview</h2>
                <p>${managerWelcomeMessage}</p>
                <div class="dashboard-buttons">
                    <a href="appointment?action=list">View All Appointments</a>
                    <a href="patient?action=list">Manage Patients</a>
                    <%-- Add more manager-specific actions --%>
                </div>
                <div class="no-data">No specific tasks for today.</div>
            </div>
        </c:when>

        <c:when test="${dashboardTitle == 'Doctor Dashboard'}">
            <div class="section">
                <h2>Doctor's Daily Schedule</h2>
                <p>${doctorWelcomeMessage}</p>
                <div class="dashboard-buttons">
                    <a href="schedule?action=list">View My Schedule</a>
                    <a href="appointment?action=listByDoctor&doctorId=${sessionScope.doctorId}">View My Appointments</a> <%-- Assuming doctorId is in session --%>
                    <%-- You might need to set doctorId in session or fetch it here if not already available --%>
                </div>

                <h3>Upcoming Appointments</h3>
                <c:if test="${not empty doctorAppointments}">
                    <table>
                        <thead>
                            <tr>
                                <th>Appointment ID</th>
                                <th>Patient ID</th>
                                <th>Scheduled Time</th>
                                <th>Status</th>
                                <th>Reason</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${doctorAppointments}">
                                <tr>
                                    <td>${appt.appointmentId}</td>
                                    <td>${appt.patientId}</td>
                                    <td><fmt:formatDate value="${appt.scheduledDatetime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td>${appt.status}</td>
                                    <td>${appt.reason}</td>
                                    <td class="table-actions">
                                        <button onclick="window.location.href='appointment?action=view&id=${appt.appointmentId}'">View</button>
                                        <button class="cancel" onclick="if(confirm('Are you sure you want to cancel this appointment?')) window.location.href='appointment?action=updateStatus&id=${appt.appointmentId}&status=CANCELLED'">Cancel</button>
                                        <%-- Add complete/reschedule actions as needed --%>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty doctorAppointments}">
                    <div class="no-data">You have no upcoming appointments.</div>
                </c:if>
            </div>
        </c:when>

        <c:when test="${dashboardTitle == 'Patient Dashboard'}">
            <div class="section">
                <h2>Your Appointments</h2>
                <p>${patientWelcomeMessage}</p>
                <div class="dashboard-buttons">
                    <a href="appointment?action=book">Book New Appointment</a>
                    <a href="appointment?action=listByPatient&patientId=${sessionScope.patientId}">View My Appointments</a> <%-- Assuming patientId is in session --%>
                    <%-- You might need to set patientId in session or fetch it here if not already available --%>
                </div>

                <h3>Upcoming Appointments</h3>
                <c:if test="${not empty patientAppointments}">
                    <table>
                        <thead>
                            <tr>
                                <th>Appointment ID</th>
                                <th>Doctor ID</th>
                                <th>Scheduled Time</th>
                                <th>Status</th>
                                <th>Reason</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${patientAppointments}">
                                <tr>
                                    <td>${appt.appointmentId}</td>
                                    <td>${appt.doctorId}</td>
                                    <td><fmt:formatDate value="${appt.scheduledDatetime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td>${appt.status}</td>
                                    <td>${appt.reason}</td>
                                    <td class="table-actions">
                                        <button onclick="window.location.href='appointment?action=view&id=${appt.appointmentId}'">View</button>
                                        <button class="cancel" onclick="if(confirm('Are you sure you want to cancel this appointment?')) window.location.href='appointment?action=updateStatus&id=${appt.appointmentId}&status=CANCELLED'">Cancel</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty patientAppointments}">
                    <div class="no-data">You have no upcoming appointments.</div>
                </c:if>
            </div>
        </c:when>
    
        <c:otherwise>
            <div class="section">
                <h2>Welcome to Your Dashboard!</h2>
                <p>No specific dashboard view configured for your role, but you can navigate using the menu.</p>
                <p>No work queue. Do some work!</p>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
