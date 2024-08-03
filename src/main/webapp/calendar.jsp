<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.expensetracker.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calendar - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
</head>
<body>
    <div class="container">
        <h1>Expense and Savings Calendar</h1>
        <nav class="nav-menu">
            <a href="dashboard.jsp" class="animated-button">Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout" class="animated-button">Logout</a>
        </nav>
        <div class="calendar-container">
            <input type="text" id="datepicker" placeholder="Select a date">
        </div>
        <% if (request.getAttribute("selectedDate") != null) { %>
            <h2>Details for <%= request.getAttribute("selectedDate") %></h2>
            
            <h3>Expenses</h3>
            <% List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");
            if (expenses != null && !expenses.isEmpty()) { %>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Category</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Expense expense : expenses) { %>
                            <tr>
                                <td><%= expense.getDescription() %></td>
                                <td><%= expense.getCategory() %></td>
                                <td>$<%= String.format("%.2f", expense.getAmount()) %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p>No expenses for this date.</p>
            <% } %>

            <h3>Savings</h3>
            <% List<Savings> savingsList = (List<Savings>) request.getAttribute("savings");
            if (savingsList != null && !savingsList.isEmpty()) { %>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Savings savings : savingsList) { %>
                            <tr>
                                <td><%= savings.getDescription() %></td>
                                <td>$<%= String.format("%.2f", savings.getAmount()) %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p>No savings for this date.</p>
            <% } %>
        <% } %>
    </div>
    <script>
        flatpickr("#datepicker", {
            dateFormat: "Y-m-d",
            onChange: function(selectedDates, dateStr, instance) {
                window.location.href = '${pageContext.request.contextPath}/calendar?date=' + dateStr;
            }
        });
    </script>
</body>
</html>