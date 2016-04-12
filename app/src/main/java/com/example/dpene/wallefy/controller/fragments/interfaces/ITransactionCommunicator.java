package com.example.dpene.wallefy.controller.fragments.interfaces;

/**
 * Created by dpene on 4/8/2016.
 */
public interface ITransactionCommunicator {

    void getAmount(String amount);
    String setAmount();
    void getNote(String note);
    String setNote();
    void getDate(String date);
    String setDate();


}
