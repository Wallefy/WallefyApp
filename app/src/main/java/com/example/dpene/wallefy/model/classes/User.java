package com.example.dpene.wallefy.model.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private long userId;
    private String username;
    private String password;
    private String email;
    private ArrayList<Category> categories;
    private ArrayList<Account> accounts;
    private ArrayList<History> historyLog;

    public User(long userId,String email, String name, String password) {

        this.userId = userId;
        this.username = name;
        this.password = password;
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<History> getHistoryLog() {
        return historyLog;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCategory(Category newCategory){
        this.categories.add(newCategory);
    }

    public void addAccount(Account newAccount){
        this.accounts.add(newAccount);
    }

    public void adHistory(History newHistory){
        this.historyLog.add(newHistory);
    }
}
