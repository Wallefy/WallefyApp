package com.example.dpene.wallefy.model.classes;

import java.util.Calendar;

public class Transaction {

    private String title;
    private float amount;
    private Calendar date;
    private Category category;
    //TODO
    private String location;
    private String picture;

    Transaction(float amount, Category category, Calendar date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.title = "";
        this.location = "";
        this.picture = "";
    }

    Transaction(float amount, Category category, Calendar date, String title, String location, String picture) {
        this(amount, category, date);
        this.title = title;
        this.location = location;
        this.picture = picture;
    }


    void setTitle(String title) {
        this.title = title;
    }

    void changeDate(Calendar date) {
        this.date = date;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setPicture(String picture) {
        this.picture = picture;
    }

    String getTitle() {
        return title;
    }

    float getAmount() {
        return amount;
    }

    Calendar getDate() {
        return date;
    }

    Category getCategory() {
        return category;
    }

    String getLocation() {
        return location;
    }

    String getPicture() {
        return picture;
    }
}
