<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
    <title>Appointment Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            background: #f9fafb;
            color: #333;
        }
        .top-bar {
            text-align: right;
            margin-bottom: 20px;
        }
        .logout-button {
            background-color: #4f46e5;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1em;
            text-decoration: none;
            display: inline-block;
        }
        .logout-button:hover {
            background-color: #4338ca;
        }
        h1 {
            margin-bottom: 24px;
            color: #2c3e50;
            text-align: center;
        }
        .section {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        .section h2 {
            margin-top: 0;
            color: #4f46e5;
            border-bottom: 2px solid #c7d2fe;
            padding-bottom: 8px;
        }
        dl {
            display: grid;
            grid-template-columns: 200px 1fr;
            gap: 15px 25px;
            margin: 0;
        }
        dt {
            font-weight: 600;
            color: #4f46e5;
            align-self: center;
        }
        dd {
            margin: 0;
            color: #555;
            background: #f8fafc;
            padding: 8px 12px;
            border-radius: 6px;
            border-left: 3px solid #e2e8f0;
        }
        .actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
            margin-top: 20px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            font-weight: 600;
            font-size: 16px;
            text-align: center;
            transition: background-color 0.3s ease;
            min-width: 140px;
        }
        .btn-primary {
            background-color: #4f46e5;
            color: white;
        }
        .btn-primary:hover {
            background-color: #4338ca;
        }
        .btn-danger {
            background-color: #dc2626;
            color: white;
        }
        .btn-danger:hover {
            background-color: #b91c1c;
        }
        .btn-secondary {
            background-color: #6b7280;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #5b6470;
        }
        .btn-back {
            background-color: #10b981;
            color: white;
        }
        .btn-back:hover {
            background-color: #059669;
        }
        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
            text-transform: uppercase;
        }
        .status-scheduled { background-color: #dbeafe; color: #1e40af; }
        .status-confirmed { background-color: #dcfce7; color: #166534; }
        .status-completed { background-color: #f0fdf4; color: #15803d; }
        .status-cancelled { background-color: #fef2f2; color: #dc2626; }
        .status-no-show { background-color: #fef3c7; color: #d97706; }
        
        @media (max-width: 768px) {
            dl {
                grid-template-columns: 1fr;
                gap: 10px;
            }
            dt {
                font-size: 14px;
                margin-bottom: 3px;
            }
            dd {
                margin-bottom: 15px;
            }
            .actions {
                flex-direction: column;
                align-items: center;
            }
            .btn {
                width: 100%;
                max-width: 300px;
            }
        }
    </style>
</head>
<body>
    <div class="top-bar">
        <a href="user?action=logout" class="logout-button">Logout</a>
    </div>

    <h1>Appointment Details</h1>
    
    <c:choose>
        <c:when test="${not empty appointment}">
            <div class="section">
                <h2>Appointment Information</h2>
                <dl>
                    <dt>Appointment ID:</dt>
                    <dd><c:out value="${appointment.appointmentId}"/></dd>

                    <dt>Patient ID:</dt>
                    <dd><c:out value="${appointment.patientId}"/></dd>

                    <dt>Doctor ID:</dt>
                    <dd><c:out value="${appointment.doctorId}"/></dd>

                    <dt>Scheduled Date & Time:</dt>
                    <dd><fmt:formatDate value="${appointment.scheduledDatetime}" pattern="EEEE, MMMM d, yyyy 'at' h:mm a"/></dd>

                    <dt>Status:</dt>
                    <dd>
                        <span class="status-badge status-${fn:toLowerCase(appointment.status)}">
                            <c:out value="${appointment.status}"/>
                        </span>
                    </dd>

                    <dt>Reason for Visit:</dt>
                    <dd><c:out value="${not empty appointment.reason ? appointment.reason : 'Not specified'}"/></dd>

                    <dt>Preferred Language:</dt>
                    <dd><c:out value="${not empty appointment.preferredLanguage ? appointment.preferredLanguage : 'Not specified'}"/></dd>

                    <dt>Special Needs:</dt>
                    <dd><c:out value="${not empty appointment.specialNeeds ? appointment.specialNeeds : 'None specified'}"/></dd>

                    <c:if test="${not empty appointment.createdAt}">
                        <dt>Created At:</dt>
                        <dd><fmt:formatDate value="${appointment.createdAt}" pattern="yyyy-MM-dd HH:mm"/></dd>
                    </c:if>

                    <c:if test="${not empty appointment.updatedAt}">
                        <dt>Last Updated:</dt>
                        <dd><fmt:formatDate value="${appointment.updatedAt}" pattern="yyyy-MM-dd HH:mm"/></dd>
                    </c:if>
                </dl>
            </div>

            <div class="actions">
                <a href="dashboard" class="btn btn-back">Back to Dashboard</a>
                <a href="appointmentForm.jsp?id=${appointment.appointmentId}" class="btn btn-primary">Edit</a>
                <a href="appointment?action=cancel&id=${appointment.appointmentId}&patientId=${appointment.patientId}" 
                   class="btn btn-danger" onclick="return confirm('Are you sure you want to cancel this appointment?')">Cancel</a>
                <a href="appointment?action=listByPatient&patientId=${appointment.patientId}" class="btn btn-secondary">Back to List</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="section">
                <h2>Appointment Not Found</h2>
                <p>The requested appointment details could not be found.</p>
                <div class="actions">
                    <a href="dashboard" class="btn btn-back">Back to Dashboard</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>