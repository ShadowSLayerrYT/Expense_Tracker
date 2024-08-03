package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.BudgetDAO;
import com.expensetracker.model.Expense;
import com.expensetracker.model.Budget;
import com.expensetracker.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/addExpense")
public class AddExpenseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            double amount = Double.parseDouble(request.getParameter("amount"));
            String description = request.getParameter("description");
            String category = request.getParameter("category");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(request.getParameter("date"));

            // Check if the selected date is greater than the current date
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date currentDate = cal.getTime();

            if (date.after(currentDate)) {
                request.setAttribute("error", "Cannot add expense for a future date!");
                request.getRequestDispatcher("addExpense.jsp").forward(request, response);
                return;
            }

            ExpenseDAO expenseDAO = new ExpenseDAO();
            BudgetDAO budgetDAO = new BudgetDAO();
            Budget budget = budgetDAO.getBudgetByUser(user.getId());
            double totalExpenses = expenseDAO.getTotalExpensesByUser(user.getId());

            if (budget != null && (totalExpenses + amount) > budget.getMonthlyBudget()) {
                request.setAttribute("error", "Adding this expense would exceed your monthly budget!");
                request.getRequestDispatcher("addExpense.jsp").forward(request, response);
                return;
            }

            Expense expense = new Expense(user.getId(), amount, description, category, date);
            expenseDAO.addExpense(expense);

            if (budget != null && (totalExpenses + amount) > (0.9 * budget.getMonthlyBudget())) {
                request.setAttribute("warning", "You have used 90% or more of your monthly budget!");
            }

            response.sendRedirect("dashboard.jsp");
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to add expense. Please try again.");
            request.getRequestDispatcher("addExpense.jsp").forward(request, response);
        }
    }
}