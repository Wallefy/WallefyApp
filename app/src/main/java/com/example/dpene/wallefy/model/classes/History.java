package com.example.dpene.wallefy.model.classes;


public class History {

    private long userId;
    private long accountTypeId;
    private long categoryId;
    private double amount;

    private String description;
    private String dateOfTransaction;
    private String imgPath;
    private String locationLat;
    private String locationLong;

    public History(long userId, long accountTypeId, long categoryId, double amount) {
        this.userId = userId;
        this.accountTypeId = accountTypeId;
        this.categoryId = categoryId;
        this.amount = amount;
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

    public String getLocationLat() {
        return locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }
}