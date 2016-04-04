package com.example.dpene.wallefy.model.classes;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private String title;
    private float amount;
    private List<Transaction> transactions;

    Account(String title, float amount) {
        this.title = title;
        this.amount = amount;
        this.transactions = new ArrayList<>();
    }

    void renameAccount(String newTitle) {
        this.title = newTitle;
    }

    void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    void deleteTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
    }

    String getTitle() {
        return title;
    }

    float getAmount() {
        return amount;
    }

    List<Transaction> getTransactions() {
        return transactions;
    }
}
