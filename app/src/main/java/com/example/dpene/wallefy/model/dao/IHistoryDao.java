package com.example.dpene.wallefy.model.dao;

import com.example.dpene.wallefy.model.classes.History;

import java.util.ArrayList;

public interface IHistoryDao {

    ArrayList<History> listAllHistory(long userID);

    ArrayList<History> listHistoryByCategory(long userID,long accountType);

    void createHistory(long userId, long accountTypeId, long categoryId, double amount,
                       String description,String dateOfTransaction,String imgPath,String locationLat,String locationLong);

    ArrayList<History> listHistoryAfterDate(long userID,String afterDate);
    ArrayList<History> listHistoryBeforeDate(long userID,String beforeDate);
    ArrayList<History> listHistoryBetweenDate(long userID,String afterDate,String beforeDate);


}
