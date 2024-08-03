package com.expensetracker.dao;

import com.expensetracker.model.Savings;
import com.expensetracker.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavingsDAO {
    private static final Logger LOGGER = Logger.getLogger(SavingsDAO.class.getName());

    public void addSavings(Savings savings) throws SQLException {
        String sql = "INSERT INTO savings (user_id, amount, date, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, savings.getUserId());
            pstmt.setDouble(2, savings.getAmount());
            pstmt.setDate(3, new java.sql.Date(savings.getDate().getTime()));
            pstmt.setString(4, savings.getDescription());
            
            pstmt.executeUpdate();
        }
    }

    public List<Savings> getSavingsByUserAndDate(int userId, Date date) throws SQLException {
        List<Savings> savingsList = new ArrayList<>();
        String sql = "SELECT * FROM savings WHERE user_id = ? AND DATE(date) = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, date);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Savings savings = new Savings();
                    savings.setId(rs.getInt("id"));
                    savings.setUserId(rs.getInt("user_id"));
                    savings.setAmount(rs.getDouble("amount"));
                    savings.setDate(rs.getDate("date"));
                    savings.setDescription(rs.getString("description"));
                    savingsList.add(savings);
                }
            }
        }
        return savingsList;
    }

    public double getTotalSavingsByUser(int userId) throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM savings WHERE user_id = ?";
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
    public List<Savings> getSavingsByUser(int userId) throws SQLException {
        List<Savings> savingsList = new ArrayList<>();
        String sql = "SELECT * FROM savings WHERE user_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Savings savings = new Savings();
                    savings.setId(rs.getInt("id"));
                    savings.setUserId(rs.getInt("user_id"));
                    savings.setAmount(rs.getDouble("amount"));
                    savings.setDate(rs.getDate("date"));
                    savings.setDescription(rs.getString("description"));
                    savingsList.add(savings);
                }
            }
        }
        return savingsList;
    }
    public void deleteSavings(int savingsId, int userId) throws SQLException {
        String sql = "DELETE FROM savings WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, savingsId);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
        }
    }
}