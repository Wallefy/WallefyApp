package com.example.dpene.wallefy.model.classes;

import java.io.Serializable;

public class Category implements Serializable {

    private long categoryId;
    private String categoryName;
    private boolean isExpense;
    private long iconResource;
    private long accountUserId;


    public Category(long categoryId, String categoryName, boolean isExpense, long iconResource, long accountUserId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isExpense = isExpense;
        this.iconResource = iconResource;
        this.accountUserId = accountUserId;
    }

    public long getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(long accountUserId) {
        this.accountUserId = accountUserId;
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

}
