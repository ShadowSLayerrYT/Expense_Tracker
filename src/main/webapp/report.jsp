<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense Report - Expense Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="container">
        <h1>Expense Report</h1>
        <nav class="nav-menu">
            <a href="dashboard.jsp" class="animated-button">Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout" class="animated-button">Logout</a>
        </nav>

        <canvas id="expenseChart"></canvas>

        <script>
            var ctx = document.getElementById('expenseChart').getContext('2d');
            var chart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [
                        <% 
                        Map<Date, Double> dailyTotals = (Map<Date, Double>) request.getAttribute("dailyTotals");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        for (Date date : dailyTotals.keySet()) {
                            out.print("'" + sdf.format(date) + "',");
                        }
                        %>
                    ],
                    datasets: [{
                        label: 'Daily Expenses',
                        data: [
                            <% 
                            for (Double total : dailyTotals.values()) {
                                out.print(total + ",");
                            }
                            %>
                        ],
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        </script>
    </div>
</body>
</html>