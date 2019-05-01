package com.maruf.tourmate.Models;

public class Expense {

    private String id;
    private String title;
    private String date;
    private String amount;

    public Expense(String id, String title, String date, String amount) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
    }

    public Expense() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
