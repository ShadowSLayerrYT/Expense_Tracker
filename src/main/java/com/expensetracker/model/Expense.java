package com.expensetracker.model;

import java.util.Date;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String description;
    private String category;
    private Date date;

    public Expense() {}

    public Expense(int userId, double amount, String description, String category, Date date) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}