<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.expensetracker.model.User"%>
<%@ page import="com.expensetracker.model.Savings"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Savings - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        List<Savings> savingsList = (List<Savings>) request.getAttribute("savingsList");
        Double totalSavings = (Double) request.getAttribute("totalSavings");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new java.util.Date());
    %>
    <div class="container">
        <h1>Savings</h1>
        <nav class="nav-menu">
            <a href="dashboard.jsp" class="animated-button">Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout" class="animated-button">Logout</a>
        </nav>
        
        <div class="styled-form">
            <h3>Add New Savings</h3>
            <form action="${pageContext.request.contextPath}/savings" method="post">
                <input type="hidden" name="action" value="add">
                <label for="amount">Amount:</label>
                <input type="number" step="100" name="amount" required>
                <label for="date">Date:</label>
                <input type="date" name="date" required max="<%= today %>">
                <label for="description">Description:</label>
                <input type="text" name="description" required>
                <input type="submit" value="Add Savings" class="animated-button">
            </form>
        </div>
        
        <h3>Your Savings</h3>
        <p>Total Savings: $<%= totalSavings != null ? String.format("%.2f", totalSavings) : "0.00" %></p>
        
        <% if (savingsList != null && !savingsList.isEmpty()) { %>
            <table class="styled-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Savings savings : savingsList) { %>
                        <tr>
                            <td><%= dateFormat.format(savings.getDate()) %></td>
                            <td><%= savings.getDescription() %></td>
                            <td>$<%= String.format("%.2f", savings.getAmount()) %></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/savings" method="post" onsubmit="return confirm('Are you sure you want to delete this savings entry?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="savingsId" value="<%= savings.getId() %>">
                                    <input type="submit" value="Delete" class="animated-button">
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <p>You haven't added any savings yet.</p>
        <% } %>
    </div>
    
    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>
</body>
</html>