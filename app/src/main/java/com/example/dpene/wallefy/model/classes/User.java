package com.example.dpene.wallefy.model.classes;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private String password;
    private String email;
    private Map<String, Account> accounts;

    User(String name, String password, String email) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.accounts = new HashMap<String, Account>();
    }


    void addAccount(Account account) {
        this.accounts.put(account.getTitle(), account);
    }

    void deleteAccount(Account account) {
        this.accounts.remove(account.getTitle());
    }

    void changeUsername(String username) {
        this.username = username;
    }

    void changePassword(String newPassword) {
        this.password = newPassword;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getEmail() {
        return email;
    }

    Map<String, Account> getAccounts() {
        return accounts;
    }
}
