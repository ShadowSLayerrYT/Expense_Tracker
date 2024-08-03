package com.expensetracker.servlet;

import com.expensetracker.dao.SavingsDAO;
import com.expensetracker.model.Savings;
import com.expensetracker.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

@WebServlet("/savings")
public class SavingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SavingsDAO savingsDAO = new SavingsDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        loadSavingsData(request, user);
        request.getRequestDispatcher("savings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                addSavings(request, user);
            } else if ("delete".equals(action)) {
                deleteSavings(request, user);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to process savings. Please try again.");
        }
        loadSavingsData(request, user);
        request.getRequestDispatcher("savings.jsp").forward(request, response);
    }

    private void loadSavingsData(HttpServletRequest request, User user) {
        try {
            List<Savings> savingsList = savingsDAO.getSavingsByUser(user.getId());
            double totalSavings = savingsDAO.getTotalSavingsByUser(user.getId());
            request.setAttribute("savingsList", savingsList);
            request.setAttribute("totalSavings", totalSavings);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve savings data.");
        }
    }

    private void addSavings(HttpServletRequest request, User user) throws SQLException, ParseException {
        double amount = Double.parseDouble(request.getParameter("amount"));
        String description = request.getParameter("description");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(request.getParameter("date"));
        
        // Validate the date
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();
        
        if (date.after(today) && !date.before(tomorrow)) {
            request.setAttribute("error", "Savings date cannot be in the future.");
            return;
        }
        
        Savings savings = new Savings(user.getId(), amount, date, description);
        savingsDAO.addSavings(savings);
    }

    private void deleteSavings(HttpServletRequest request, User user) throws SQLException {
        int savingsId = Integer.parseInt(request.getParameter("savingsId"));
        savingsDAO.deleteSavings(savingsId, user.getId());
    }
}