<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/common.css'/>">
</head>
<body>
    <div class="container">
        <div class="form-card">
            <h2>User Registration</h2>
            <form action="register" method="post">
                <div class="form-group">
                    <label for="firstName">First Name:</label>
                    <input type="text" id="firstName" name="firstName" required>
                </div>
                <div class="form-group">
                    <label for="lastName">Last Name:</label>
                    <input type="text" id="lastName" name="lastName" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" autocomplete="email" required>

                </div>
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" autocomplete="username" required>

                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required minlength="4">
                </div>
                <input type="submit" value="Register">
            </form>
            <p>Already have an account? <a href="login">Login here</a></p>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
