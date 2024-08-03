<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.expensetracker.model.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set Budget - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    <div class="animated-header">
        <h1 class="rainbow-text">Set Your Budget</h1>
    </div>
    <div class="container">
        <div class="styled-form">
            <h2>Set Budget</h2>
            <% if(request.getAttribute("error") != null) { %>
                <p class="error-message"><%= request.getAttribute("error") %></p>
            <% } %>
            <form action="setBudget" method="post">
                <input type="number" name="monthlyIncome" step="100" placeholder="Monthly Income" required>
                <input type="number" name="monthlyBudget" step="100" placeholder="Monthly Budget" required>
                <input type="submit" value="Set Budget" class="animated-button">
            </form>
            <a href="dashboard.jsp" class="animated-button">Back to Dashboard</a>
        </div>
    </div>
</body>
</html>