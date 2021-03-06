package com.example.dpene.wallefy.model.classes;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private long userId;
    private String username;
    private String password;
    private String email;
    private ArrayList<Category> categories;
    private ArrayList<Account> accounts;
    private ArrayList<History> historyLog;

    public User() {
        this.accounts = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.historyLog = new ArrayList<>();
    }

    public User(long userId, String username, String password, String email,
                ArrayList<Category> categories, ArrayList<Account> accounts, ArrayList<History> historyLog) {

        this.accounts = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.historyLog = new ArrayList<>();

        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        if (accounts != null)
            this.accounts = accounts;
        if (categories!= null)
            this.categories = categories;
        if (historyLog != null)
            this.historyLog = historyLog;
    }

    public User(long userId, String email, String name, String password) {

        this.userId = userId;
        this.username = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.historyLog = new ArrayList<>();
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public void addCategory(Category newCategory) {
        this.categories.add(newCategory);
    }

    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    public void addHistory(History newHistory) {
        if (historyLog != null)
            this.historyLog.add(newHistory);
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void setHistoryLog(ArrayList<History> historyLog) {
        this.historyLog = historyLog;
    }

    public void deleteAccount(String accName){
        for (Account acc :
                this.accounts) {
            if (acc.getAccountName().equalsIgnoreCase(accName)){
                this.accounts.remove(acc);
            }
        }
    }

}
