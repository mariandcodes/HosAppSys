<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Doctor List</title>
</head>
<body>
    <h1>Doctor List</h1>
    <form action="doctor?action=search" method="get">
        <input type="text" name="query" placeholder="Search by name or specialty">
        <input type="submit" value="Search">
    </form>
    <table>
        <tr>
            <th>Full Name</th>
            <th>Specialty</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="doctor" items="${doctors}">
            <tr>
                <td>${doctor.fullName}</td>
                <td>${doctor.specialty}</td>
                <td>
                    <a href="doctor?action=view&id=${doctor.doctorId}">View</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="doctor?action=create">Add New Doctor</a>
</body>
</html>