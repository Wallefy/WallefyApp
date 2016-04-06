package com.example.dpene.wallefy.model.dao;

import com.example.dpene.wallefy.model.classes.Account;

import java.util.ArrayList;

public interface IAccountDao {

    Account showAccount(String accountName);

    Account createAccount(long userId,String accountName);

    ArrayList<Account> showAllAccounts(long userId);

}
