package com.example.dpene.wallefy.model.dao;

import com.example.dpene.wallefy.model.classes.History;

import java.util.ArrayList;

public interface IHistoryDao {

    ArrayList<History> listAllHistory(long userID);

//    ArrayList<History> listHistoryByCategory(long userID,long accountType);

    ArrayList<History> listHistoryByAccount(long userID,long accountType);

    ArrayList<History> listHistoryByAccountName(long userID,String accountType);

    ArrayList<History> listHistoryByCategoryName(long userID,String category);

    ArrayList<History> listHistoryByCategoryNameAndAccount(long userID,String accountName,String category);

    History createHistory(long userId, long accountTypeId, long categoryId, double amount,
                          String description,String dateOfTransaction,String imgPath,String location);

    double calcAmountForAccount(long userId,String accountTypeName);

    ArrayList<History> listHistoryAfterDate(long userID,String afterDate);

    ArrayList<History> listHistoryBeforeDate(long userID,String beforeDate);

    ArrayList<History> listHistoryBetweenDate(long userID,String afterDate,String beforeDate);

    ArrayList<History> filterEntries(String userId, String accName, String typeOfEntry, String catName, String dateAfter);
}
