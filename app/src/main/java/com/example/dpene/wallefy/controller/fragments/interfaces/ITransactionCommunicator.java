package com.example.dpene.wallefy.controller.fragments.interfaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by dpene on 4/8/2016.
 */
public interface ITransactionCommunicator {

    void notifyFragment(Fragment fragment, Bundle bundle);
    void getAmount(String amount);
    String setAmount();
    void getNote(String note);
    String setNote();
    void getDate(String date);
    String setDate();


}
