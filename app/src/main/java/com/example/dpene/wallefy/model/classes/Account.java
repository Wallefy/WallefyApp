package com.example.dpene.wallefy.model.classes;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private long  accountTypeId;
    private String accountName;

    public Account(long accountTypeId, String accountName) {
        this.accountTypeId = accountTypeId;
        this.accountName = accountName;
    }

    public long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
