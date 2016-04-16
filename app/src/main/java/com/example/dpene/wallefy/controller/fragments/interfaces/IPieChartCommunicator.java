package com.example.dpene.wallefy.controller.fragments.interfaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by dpene on 4/9/2016.
 */
public interface IPieChartCommunicator {

    void notifyFragment(Fragment fragment, Bundle bundle);
    void setPosition(int position);
    int getPosition();

}
