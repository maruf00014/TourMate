package com.maruf.toutmate.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    private String key;
    private String eventName;
    private String startingLocation;
    private String destinationLocation;
    private long createdDate;
    private long departureDate;
    private double budget;
    private List<com.maruf.toutmate.Model.Expense> expenseList = new ArrayList<>();
    private List<com.maruf.toutmate.Model.Moment> momentList = new ArrayList<>();

    public Event() {
        //required for firebase
    }

    public Event(String key, String eventName, String startingLocation, String destinationLocation, long createdDate, long departureDate, double budget) {
        this.key = key;
        this.eventName = eventName;
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
        this.createdDate = createdDate;
        this.departureDate = departureDate;
        this.budget = budget;
    }

    public Event(String key, String eventName, String startingLocation, String destinationLocation, long createdDate, long departureDate, double budget, List<com.maruf.toutmate.Model.Expense> expenseList, List<com.maruf.toutmate.Model.Moment> momentList) {
        this.key = key;
        this.eventName = eventName;
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
        this.createdDate = createdDate;
        this.departureDate = departureDate;
        this.budget = budget;
        this.expenseList = expenseList;
        this.momentList = momentList;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public double getBudget() {
        return budget;
    }

    public String getKey() {
        return key;
    }

    public List<com.maruf.toutmate.Model.Expense> getExpenseList() {
        return expenseList;
    }

    public List<com.maruf.toutmate.Model.Moment> getMomentList() {
        return momentList;
    }


}
