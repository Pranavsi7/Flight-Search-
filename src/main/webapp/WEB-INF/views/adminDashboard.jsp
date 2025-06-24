<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/dashboard.css">

</head>
<body>
    <header>
    <div class="logo">SearchFlights</div>
    <a href="${pageContext.request.contextPath}/admin/logout" class="logout-btn" onclick="return confirm('Are you sure you want to logout?')">
        🚪 Logout
    </a>
</header>



   <div class="container">
    <h2>Welcome, Admin</h2>
    <div class="dashboard-cards">
        <div class="card">${totalUsers}<span>Users</span></div>
        <div class="card">${totalFlights}<span>Flights</span></div>
    </div>

        <div class="buttons">
           <button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/upload'">Upload Flights</button>
<button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/addFlight'">Add Flight</button>
<button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/editFlight'">Edit Flights</button>
<button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/deleteFlight'">Delete Flights</button>

<button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/configuration'">Configuration</button>
<button class="dashboard-btn" onclick="location.href='${pageContext.request.contextPath}/admin/history'">History</button>

        </div>
    </div>

    <footer>
        © 2025 SearchFlights. All rights reserved. Contact: <a href="mailto:pranav.singh071204@gmail.com">pranav.singh071204@gmail.com</a>
    </footer>
</body>
</html>
