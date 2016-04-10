package com.example.dpene.wallefy.model.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private long  accountTypeId;
    private long  accountUserId;
    private String accountName;
    private double accountTempSum;

    public Account(long accountTypeId,long userFk, String accountName) {
        this.accountTypeId = accountTypeId;
        this.accountUserId = userFk;
        this.accountName = accountName;
    }

    public long getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(long accountUserId) {
        this.accountUserId = accountUserId;
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

    public double getAccountTempSum() {
        return accountTempSum;
    }

    public void setAccountTempSum(double accountTempSum) {
        this.accountTempSum = accountTempSum;
    }
}
