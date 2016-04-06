package com.example.dpene.wallefy.model.classes;


import java.io.Serializable;

public class History implements Serializable {

    private long historyId;
    private long userId;
    private long accountTypeId;
    private long categoryId;
    private String categoryName;
    private int categoryIconResource;
    private double amount;

    private String description;
    private String dateOfTransaction;
    private String imgPath;
    private String location;

    public History(){}

//    public History(long userId, long accountTypeId, long categoryId, double amount) {
//        this.userId = userId;
//        this.accountTypeId = accountTypeId;
//        this.categoryId = categoryId;
//        this.amount = amount;
//    }


    public History(long historyId, long userId, long accountTypeId, long categoryId,String categoryName,
                   int categoryIconResource, double amount,
                   String description, String dateOfTransaction, String imgPath, String location) {
        this.historyId = historyId;
        this.userId = userId;
        this.accountTypeId = accountTypeId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIconResource = categoryIconResource;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
        this.imgPath = imgPath;
        this.location = location;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryIconResource() {
        return categoryIconResource;
    }

    public long getUserId() {
        return userId;
    }

    public long getAccountTypeId() {
        return accountTypeId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}