package com.example.dpene.wallefy.model.classes;

import java.util.List;

public class AccountManager {

    private static Account account;
    private static AccountManager instance;

    private AccountManager(){

    }

    public static AccountManager getInstance(){
        if(instance == null){
            instance = new AccountManager();
        }
        return instance;
    }

    //TODO initializing account's methods, DAO


    public String getAccountTitle(){
        return account.getTitle();
    }

    public float getAmount(){
        return account.getAmount();
    }

    public List<Transaction> getTransacions(){
        return account.getTransactions();
    }

}
