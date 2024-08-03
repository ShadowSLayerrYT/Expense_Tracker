package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get current date as end date
        Calendar cal = Calendar.getInstance();
        Date endDate = new Date(cal.getTimeInMillis());

        // Calculate start date (30 days ago)
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = new Date(cal.getTimeInMillis());

        try {
            ExpenseDAO expenseDAO = new ExpenseDAO();
            Map<Date, Double> dailyTotals = expenseDAO.getDailyTotals(user.getId(), startDate, endDate);
            request.setAttribute("dailyTotals", dailyTotals);
            request.getRequestDispatcher("report.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to generate report. Please try again.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
}