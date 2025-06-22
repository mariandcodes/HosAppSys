<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Doctor Profile</title>
</head>
<body>
    <h1>Doctor Profile</h1>
    <p>Full Name: ${doctor.fullName}</p>
    <p>Specialty: ${doctor.specialty}</p>
    <p>Qualifications: ${doctor.qualifications}</p>
    <p>Contact Info: ${doctor.contactInfo}</p>
    <p>Working Hours: ${doctor.workingHours}</p>
    <p>Consultation Fee: ${doctor.consultationFee}</p>
    <p>Status: ${doctor.status}</p>
    <a href="doctor?action=edit&id=${doctor.doctorId}">Edit</a>
    <a href="doctor?action=delete&id=${doctor.doctorId}">Delete</a>
    <a href="doctor?action=list">Back to List</a>
</body>
</html>