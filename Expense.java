package com.expensetracker.expense;

public class Expense {
	
	private int id;
    private String category;
    private double amount;
    private String date;
    private String description;

    public Expense(String category, double amount, String date, String description) {
    	this.id=id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
    
    public int getId() {

        return id;

    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
