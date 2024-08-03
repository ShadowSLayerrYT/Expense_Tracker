package com.expensetracker.dao;

import com.expensetracker.model.Budget;
import com.expensetracker.util.DatabaseUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BudgetDAO {

    private static final Logger LOGGER = Logger.getLogger(BudgetDAO.class.getName());

    public void setBudget(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, monthly_income, monthly_budget, daily_limit) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE monthly_income = ?, monthly_budget = ?, daily_limit = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, budget.getUserId());
            pstmt.setDouble(2, budget.getMonthlyIncome());
            pstmt.setDouble(3, budget.getMonthlyBudget());
            pstmt.setDouble(4, budget.getDailyLimit());
            pstmt.setDouble(5, budget.getMonthlyIncome());
            pstmt.setDouble(6, budget.getMonthlyBudget());
            pstmt.setDouble(7, budget.getDailyLimit());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error setting budget for user id: " + budget.getUserId(), e);
        }
    }
    public void deleteBudget(int userId) throws SQLException {
        String sql = "DELETE FROM budgets WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.executeUpdate();
        }
    }
    public void updateBudget(Budget budget) throws SQLException {
        String sql = "UPDATE budgets SET monthly_income = ?, monthly_budget = ?, daily_limit = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, budget.getMonthlyIncome());
            pstmt.setDouble(2, budget.getMonthlyBudget());
            pstmt.setDouble(3, budget.getDailyLimit());
            pstmt.setInt(4, budget.getUserId());
            
            pstmt.executeUpdate();
        }
    }

    public Budget getBudgetByUser(int userId) {
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Budget budget = new Budget();
                    budget.setId(rs.getInt("id"));
                    budget.setUserId(rs.getInt("user_id"));
                    budget.setMonthlyIncome(rs.getDouble("monthly_income"));
                    budget.setMonthlyBudget(rs.getDouble("monthly_budget"));
                    budget.setDailyLimit(rs.getDouble("daily_limit"));
                    return budget;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting budget for user id: " + userId, e);
        }
        return null;
    }
}
