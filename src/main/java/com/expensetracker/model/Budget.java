package com.expensetracker.model;

public class Budget {
    private int id;
    private int userId;
    private double monthlyIncome;
    private double monthlyBudget;
    private double dailyLimit;

    public Budget() {}

    public Budget(int userId, double monthlyIncome, double monthlyBudget, double dailyLimit) {
        this.userId = userId;
        this.monthlyIncome = monthlyIncome;
        this.monthlyBudget = monthlyBudget;
        this.dailyLimit = dailyLimit;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(double monthlyBudget) { this.monthlyBudget = monthlyBudget; }
    public double getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(double dailyLimit) { this.dailyLimit = dailyLimit; }
}