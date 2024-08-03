<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="animated-header">
        <h1 class="rainbow-text">Login to Expense Tracker</h1>
    </div>
    <div class="container">
        <div class="styled-form">
            <h2>Login</h2>
            <% if(request.getAttribute("error") != null) { %>
                <p class="error-message"><%= request.getAttribute("error") %></p>
            <% } %>
            <form action="login" method="post">
                <input type="text" name="username" placeholder="Username" required>
                <input type="password" name="password" placeholder="Password" required>
                <input type="submit" value="Login" class="animated-button">
            </form>
            <p>Don't have an account? <a href="register.jsp" class="animated-button">Register</a></p>
        </div>
    </div>
</body>
</html>