package com.example.dpene.wallefy.model.classes;

public class Category {

    private String title;
    private boolean isIncome;
    private int icon;
    private String color;

    Category(String title, boolean isIncome, int icon, String color) {
        this.title = title;
        this.isIncome = isIncome;
        this.icon = icon;
        this.color = color;
    }

    String getTitle() {
        return title;
    }

    boolean isIncome() {
        return isIncome;
    }

    int getIcon() {
        return icon;
    }

    String getColor() {
        return color;
    }
}
