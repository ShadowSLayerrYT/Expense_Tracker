<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.expensetracker.model.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Expense - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        function validateDate() {
            var selectedDate = new Date(document.getElementById('expenseDate').value);
            var currentDate = new Date();
            
            // Set both dates to midnight for accurate comparison
            selectedDate.setHours(0, 0, 0, 0);
            currentDate.setHours(0, 0, 0, 0);
            
            if (selectedDate > currentDate) {
                alert("Cannot add expense for a future date!");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <% User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    } %>
    <div class="animated-header">
        <h1 class="rainbow-text">Add Expense</h1>
    </div>
    <div class="container">
        <div class="styled-form">
            <h2>Add Expense</h2>
            <% if(request.getAttribute("error") != null) { %>
                <p class="error-message"><%= request.getAttribute("error") %></p>
            <% } %>
            <% if(request.getAttribute("warning") != null) { %>
                <p class="warning-message"><%= request.getAttribute("warning") %></p>
            <% } %>
            <form action="addExpense" method="post" onsubmit="return validateDate();">
                <input type="number" name="amount" step="0.01" placeholder="Amount" required>
                <input type="text" name="description" placeholder="Description" required>
                <select name="category" required>
                    <option value="">Select Category</option>
                    <option value="food">Food</option>
                    <option value="transportation">Transportation</option>
                    <option value="utilities">Utilities</option>
                    <option value="entertainment">Entertainment</option>
                    <option value="other">Other</option>
                </select>
                <input type="date" name="date" id="expenseDate" required>
                <input type="submit" value="Add Expense" class="animated-button">
            </form>
            <a href="dashboard.jsp" class="animated-button">Back to Dashboard</a>
        </div>
    </div>
</body>
</html>