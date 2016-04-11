package com.example.dpene.wallefy.model.dao;

import com.example.dpene.wallefy.model.classes.Account;

import java.util.ArrayList;

public interface IAccountDao {

    Account showAccount(long userId,String accountName);

    Account createAccount(long userId,String accountName);

    Account updateAccount(long userId,String newAccountName, String oldAccoundName);

    ArrayList<Account> showAllAccounts(long userId);

}
