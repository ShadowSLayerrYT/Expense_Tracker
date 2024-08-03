<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.expensetracker.model.User"%>
<%@ page import="com.expensetracker.dao.ExpenseDAO"%>
<%@ page import="com.expensetracker.model.Expense"%>
<%@ page import="com.expensetracker.dao.BudgetDAO"%>
<%@ page import="com.expensetracker.model.Budget"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.sql.SQLException"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        ExpenseDAO expenseDAO = new ExpenseDAO();
        BudgetDAO budgetDAO = new BudgetDAO();
        List<Expense> expenses = null;
        Budget budget = null;
        double totalExpenses = 0.0;
        
        try {
            expenses = expenseDAO.getExpensesByUser(user.getId());
            budget = budgetDAO.getBudgetByUser(user.getId());
            totalExpenses = expenseDAO.getTotalExpensesByUser(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while fetching your data.");
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String warning = (String) request.getAttribute("warning");
        if (warning != null) {
            session.setAttribute("warning", warning);
        }
    %>
    <div class="animated-header">
        <h1 class="rainbow-text">Welcome, <%= user.getUsername() %>!</h1>
    </div>
    <div class="container">
        <nav class="nav-menu">
            <a href="addExpense.jsp" class="animated-button">Add Expense</a>
            <a href="setBudget.jsp" class="animated-button">Set Budget</a>
            <a href="savings.jsp" class="animated-button">Savings</a>
            <a href="${pageContext.request.contextPath}/report" class="animated-button">View Report</a> 
            <a href="${pageContext.request.contextPath}/calendar" class="animated-button">Calendar</a>           
            <a href="${pageContext.request.contextPath}/logout" class="animated-button">Logout</a>
        </nav>
        
        <% if (session.getAttribute("warning") != null) { %>
            <div class="warning-message">
                <%= session.getAttribute("warning") %>
            </div>
            <% session.removeAttribute("warning"); %>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (budget != null) { %>
            <div class="styled-form pulse-animation">
                <h3>Your Budget</h3>
                <p>Monthly Income: $<%= String.format("%.2f", budget.getMonthlyIncome()) %></p>
                <p>Monthly Budget: $<%= String.format("%.2f", budget.getMonthlyBudget()) %></p>
                <p>Daily Limit: $<%= String.format("%.2f", budget.getDailyLimit()) %></p>
                <p>Total Expenses: $<%= String.format("%.2f", totalExpenses) %></p>
                <p>Remaining Budget: $<%= String.format("%.2f", budget.getMonthlyBudget() - totalExpenses) %></p>
                <form action="${pageContext.request.contextPath}/deleteBudget" method="post" onsubmit="return confirm('Are you sure you want to delete this budget?');">
                    <input type="submit" value="Delete Budget" class="animated-button">
                </form>
            </div>
        <% } else { %>
            <p>You haven't set a budget yet. <a href="setBudget.jsp" class="animated-button">Set your budget now</a></p>
        <% } %>
        
        <h3 class="float-animation">Recent Expenses</h3>
        <% if (expenses != null && !expenses.isEmpty()) { %>
            <table class="styled-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Category</th>
                        <th>Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Expense expense : expenses) { %>
                        <tr>
                            <td><%= dateFormat.format(expense.getDate()) %></td>
                            <td><%= expense.getDescription() %></td>
                            <td><%= expense.getCategory() %></td>
                            <td>$<%= String.format("%.2f", expense.getAmount()) %></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/deleteExpense" method="post" onsubmit="return confirm('Are you sure you want to delete this expense?');">
                                    <input type="hidden" name="expenseId" value="<%= expense.getId() %>">
                                    <input type="submit" value="Delete" class="animated-button">
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <p>You haven't added any expenses yet.</p>
        <% } %>
    </div>
</body>
</html>