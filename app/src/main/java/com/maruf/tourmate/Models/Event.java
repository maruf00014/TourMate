package com.maruf.tourmate.Models;


import java.io.Serializable;

public class Event implements Serializable {

    private String id;
    private String eventTitle;
    private String fromTo;
    private String startDate;
    private String budget;
    private String balance;

    public Event(String id, String eventTitle, String fromTo, String startDate, String budget, String balance) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.fromTo = fromTo;
        this.startDate = startDate;
        this.budget = budget;
        this.balance = balance;
    }

    public Event(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
