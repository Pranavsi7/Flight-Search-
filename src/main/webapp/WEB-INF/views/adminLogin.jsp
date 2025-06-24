<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Login</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/common.css'/>">
</head>
<body>
    <div class="container">
        <div class="form-card">
            <h2>Admin Login</h2>
            <form action="${pageContext.request.contextPath}/adminLogin" method="post">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                     <sec:csrfInput />
                </div>
                <input type="submit" value="Login">
            </form>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
