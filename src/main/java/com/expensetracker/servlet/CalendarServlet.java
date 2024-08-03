package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.SavingsDAO;
import com.expensetracker.model.Expense;
import com.expensetracker.model.Savings;
import com.expensetracker.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.isEmpty()) {
            request.getRequestDispatcher("calendar.jsp").forward(request, response);
            return;
        }

        Date selectedDate = Date.valueOf(dateStr);

        try {
            ExpenseDAO expenseDAO = new ExpenseDAO();
            SavingsDAO savingsDAO = new SavingsDAO();

            List<Expense> expenses = expenseDAO.getExpensesByUserAndDate(user.getId(), selectedDate);
            List<Savings> savings = savingsDAO.getSavingsByUserAndDate(user.getId(), selectedDate);

            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("expenses", expenses);
            request.setAttribute("savings", savings);

            request.getRequestDispatcher("calendar.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to fetch data for the selected date.");
            request.getRequestDispatcher("calendar.jsp").forward(request, response);
        }
    }
}