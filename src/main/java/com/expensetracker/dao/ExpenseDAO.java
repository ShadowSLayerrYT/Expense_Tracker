package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseDAO {
    public void addExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (user_id, amount, description, category, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expense.getUserId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDescription());
            pstmt.setString(4, expense.getCategory());
            pstmt.setDate(5, new java.sql.Date(expense.getDate().getTime()));
            
            pstmt.executeUpdate();
        }
    }

    public double getTotalExpensesByUser(int userId) throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM expenses WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }
    public List<Expense> getExpensesByUser(int userId) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setId(rs.getInt("id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setDescription(rs.getString("description"));
                    expense.setCategory(rs.getString("category"));
                    expense.setDate(rs.getDate("date"));
                    expenses.add(expense);
                }
            }
        }
        return expenses;
    }

    public void deleteExpense(int expenseId, int userId) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
        }
    }

    public Map<Date, Double> getDailyTotals(int userId, Date startDate, Date endDate) throws SQLException {
        Map<Date, Double> dailyTotals = new TreeMap<>();
        String sql = "SELECT DATE(date) as expense_date, SUM(amount) as daily_total " +
                     "FROM expenses WHERE user_id = ? AND date BETWEEN ? AND ? " +
                     "GROUP BY DATE(date) ORDER BY DATE(date)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date date = rs.getDate("expense_date");
                    double total = rs.getDouble("daily_total");
                    dailyTotals.put(date, total);
                }
            }
        }
        return dailyTotals;
    }

    public List<Expense> getExpensesByUserAndDate(int userId, Date date) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? AND DATE(date) = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, new java.sql.Date(date.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setId(rs.getInt("id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setDescription(rs.getString("description"));
                    expense.setCategory(rs.getString("category"));
                    expense.setDate(rs.getDate("date"));
                    expenses.add(expense);
                }
            }
        }
        return expenses;
    }
}