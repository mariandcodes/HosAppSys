<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Book Appointment - Hospital System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
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
            padding: 30px;
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
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #4f46e5;
        }
        .form-group input, 
        .form-group select, 
        .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #e5e7eb;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
            box-sizing: border-box;
        }
        .form-group input:focus, 
        .form-group select:focus, 
        .form-group textarea:focus {
            outline: none;
            border-color: #4f46e5;
            box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
        }
        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }
        .form-row {
            display: flex;
            gap: 20px;
        }
        .form-row .form-group {
            flex: 1;
        }
        .button-group {
            text-align: center;
            margin-top: 30px;
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.3s ease;
        }
        .btn-primary {
            background-color: #4f46e5;
            color: white;
        }
        .btn-primary:hover {
            background-color: #4338ca;
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
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: 600;
        }
        .alert-success {
            background-color: #d1fae5;
            color: #065f46;
            border: 1px solid #a7f3d0;
        }
        .alert-error {
            background-color: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
        }
        .required {
            color: #ef4444;
        }
        .form-help {
            font-size: 14px;
            color: #6b7280;
            margin-top: 5px;
        }
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
                gap: 0;
            }
            .button-group {
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

    <h1>Book New Appointment</h1>

    <!-- Display success/error messages -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success">
            ${sessionScope.successMessage}
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-error">
            ${sessionScope.errorMessage}
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="section">
        <h2>Appointment Details</h2>
        
        <form action="appointment" method="post">
            <input type="hidden" name="action" value="book">
            
            <div class="form-row">
                <div class="form-group">
                    <label for="patientId">Patient ID <span class="required">*</span></label>
                    <input type="number" id="patientId" name="patientId" required 
                           value="${sessionScope.patientId != null ? sessionScope.patientId : ''}"
                           ${sessionScope.userRole == 'PATIENT' ? 'readonly' : ''}>
                    <div class="form-help">Enter the patient's unique ID number</div>
                </div>
                
                <div class="form-group">
                    <label for="doctorId">Doctor ID <span class="required">*</span></label>
                    <input type="number" id="doctorId" name="doctorId" required>
                    <div class="form-help">Enter the doctor's unique ID number</div>
                </div>
            </div>

            <div class="form-group">
                <label for="scheduledDatetime">Scheduled Date & Time <span class="required">*</span></label>
                <input type="datetime-local" id="scheduledDatetime" name="scheduledDatetime" required>
                <div class="form-help">Select your preferred appointment date and time</div>
            </div>

            <div class="form-group">
                <label for="reason">Reason for Visit <span class="required">*</span></label>
                <textarea id="reason" name="reason" required placeholder="Please describe the reason for your appointment..."></textarea>
                <div class="form-help">Provide details about your symptoms or the purpose of your visit</div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="SCHEDULED" selected>Scheduled</option>
                        <option value="CONFIRMED">Confirmed</option>
                        <option value="PENDING">Pending</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="preferredLanguage">Preferred Language</label>
                    <select id="preferredLanguage" name="preferredLanguage">
                        <option value="">Select Language</option>
                        <option value="English">English</option>
                        <option value="Spanish">Spanish</option>
                        <option value="French">French</option>
                        <option value="Mandarin">Mandarin</option>
                        <option value="Tagalog">Tagalog</option>
                        <option value="Other">Other</option>
                    </select>
                    <div class="form-help">Select your preferred language for communication</div>
                </div>
            </div>

            <div class="form-group">
                <label for="specialNeeds">Special Needs or Accommodations</label>
                <textarea id="specialNeeds" name="specialNeeds" placeholder="Please describe any special needs or accommodations required..."></textarea>
                <div class="form-help">Include accessibility needs, dietary restrictions, or other special requirements</div>
            </div>

            <div class="button-group">
                <button type="submit" class="btn btn-primary">Book Appointment</button>
                <button type="reset" class="btn btn-secondary">Clear Form</button>
                <a href="dashboard" class="btn btn-back">Back to Dashboard</a>
            </div>
        </form>
    </div>

    <script>
        // Set minimum date to today
        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('scheduledDatetime');
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            
            const minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
            dateInput.min = minDateTime;
        });

        // Form validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const patientId = document.getElementById('patientId').value;
            const doctorId = document.getElementById('doctorId').value;
            const scheduledDateTime = document.getElementById('scheduledDatetime').value;
            const reason = document.getElementById('reason').value.trim();

            if (!patientId || !doctorId || !scheduledDateTime || !reason) {
                e.preventDefault();
                alert('Please fill in all required fields.');
                return false;
            }

            // Check if the scheduled time is in the future
            const selectedDate = new Date(scheduledDateTime);
            const now = new Date();
            if (selectedDate <= now) {
                e.preventDefault();
                alert('Please select a future date and time for your appointment.');
                return false;
            }

            return true;
        });
    </script>
</body>
</html>