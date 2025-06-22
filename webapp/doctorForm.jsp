<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${doctor.doctorId != null ? 'Edit Doctor' : 'Add Doctor'}</title>
</head>
<body>
    <h1>${doctor.doctorId != null ? 'Edit Doctor' : 'Add Doctor'}</h1>
    <form action="doctor?action=${doctor.doctorId != null ? 'update' : 'create'}" method="post">
        <input type="hidden" name="id" value="${doctor.doctorId}">
        <label>Full Name:</label>
        <input type="text" name="fullName" value="${doctor.fullName}" required><br>
        <label>Specialty:</label>
        <input type="text" name="specialty" value="${doctor.specialty}" required><br>
        <label>Qualifications:</label>
        <input type="text" name="qualifications" value="${doctor.qualifications}"><br>
        <label>Contact Info:</label>
        <input type="text" name="contactInfo" value="${doctor.contactInfo}"><br>
        <label>Working Hours:</label>
        <input type="text" name="workingHours" value="${doctor.workingHours}"><br>
        <label>Consultation Fee:</label>
        <input type="number" name="consultationFee" value="${doctor.consultationFee}" required><br>
        <label>Status:</label>
        <input type="text" name="status" value="${doctor.status}"><br>
        <input type="submit" value="${doctor.doctorId != null ? 'Update' : 'Create'}">
    </form>
    <a href="doctor?action=list">Cancel</a>
</body>
</html>