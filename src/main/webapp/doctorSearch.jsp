<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Doctors</title>
</head>
<body>
    <h1>Search Doctors</h1>
    <form action="doctor?action=search" method="get">
        <input type="text" name="query" placeholder="Enter name or specialty">
        <input type="submit" value="Search">
    </form>
    <a href="doctor?action=list">Back to List</a>
</body>
</html>