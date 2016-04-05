package com.example.dpene.wallefy.model.classes;

public class Category {

    private long categoryId;
    private String categoryName;
    private boolean isExpense;
    private long iconResource;
    private String color;

    public Category(long categoryId, String categoryName, boolean isExpense, long iconResource, String color) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isExpense = isExpense;
        this.iconResource = iconResource;
        this.color = color;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setIsExpense(boolean isExpense) {
        this.isExpense = isExpense;
    }

    public long getIconResource() {
        return iconResource;
    }

    public void setIconResource(long iconResource) {
        this.iconResource = iconResource;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
