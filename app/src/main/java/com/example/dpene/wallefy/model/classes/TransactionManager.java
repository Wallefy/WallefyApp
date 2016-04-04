package com.example.dpene.wallefy.model.classes;

import java.util.Calendar;

public class TransactionManager {

    private static Transaction transaction;
    private static TransactionManager instance;

    private TransactionManager(){

    }

    public TransactionManager getInstance(){
        if(instance == null){
            instance = new TransactionManager();
        }
        return instance;
    }

    //TODO initializing transaction's methods, DAO

    public String getTitle(){
        return transaction.getTitle();
    }

    public Category getCategory(){
        return transaction.getCategory();
    }

    public float getAmount(){
        return transaction.getAmount();
    }

    public Calendar getDate(){
        return transaction.getDate();
    }

    public String getLocation(){
        return transaction.getLocation();
    }

    public String getPicture(){
        return transaction.getPicture();
    }
}
