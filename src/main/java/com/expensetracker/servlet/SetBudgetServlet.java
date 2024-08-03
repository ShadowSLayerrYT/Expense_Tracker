package com.expensetracker.servlet;

import com.expensetracker.dao.BudgetDAO;
import com.expensetracker.model.Budget;
import com.expensetracker.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/setBudget")
public class SetBudgetServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            double monthlyIncome = Double.parseDouble(request.getParameter("monthlyIncome"));
            double monthlyBudget = Double.parseDouble(request.getParameter("monthlyBudget"));
            double dailyLimit = monthlyBudget / 30; // Simplified calculation

            BudgetDAO budgetDAO = new BudgetDAO();
            Budget existingBudget = budgetDAO.getBudgetByUser(user.getId());

            if (existingBudget != null) {
                // Update existing budget
                existingBudget.setMonthlyIncome(monthlyIncome);
                existingBudget.setMonthlyBudget(monthlyBudget);
                existingBudget.setDailyLimit(dailyLimit);
                budgetDAO.updateBudget(existingBudget);
            } else {
                // Create new budget
                Budget newBudget = new Budget(user.getId(), monthlyIncome, monthlyBudget, dailyLimit);
                budgetDAO.setBudget(newBudget);
            }

            response.sendRedirect("dashboard.jsp");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input. Please enter valid numbers.");
            request.getRequestDispatcher("setBudget.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again.");
            request.getRequestDispatcher("setBudget.jsp").forward(request, response);
        }
    }
}