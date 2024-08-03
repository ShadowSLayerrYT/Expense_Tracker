package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteExpense")
public class DeleteExpenseServlet extends HttpServlet {
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

        int expenseId = Integer.parseInt(request.getParameter("expenseId"));
        
        ExpenseDAO expenseDAO = new ExpenseDAO();
        try {
            expenseDAO.deleteExpense(expenseId, user.getId());
            response.sendRedirect("dashboard.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to delete expense. Please try again.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
}