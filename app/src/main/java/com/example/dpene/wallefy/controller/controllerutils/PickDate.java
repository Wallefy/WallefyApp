package com.example.dpene.wallefy.controller.controllerutils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PickDate {

    public static void pick(final View edtField,FragmentManager fm) {
        class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

            boolean isCanceled;
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                isCanceled =false;
                
                final DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
//                Not working with android 5+
//                dpd.setButton(DialogInterface.BUTTON_POSITIVE, "Select", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
                dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCanceled = true;
                    }
                });
                return dpd;
            }

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isCanceled)
                    return;
                monthOfYear++;
                String date = year + "-" + ((monthOfYear < 10) ? "0" : "") + monthOfYear + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                ((TextView)edtField).setText(DateFormater.from_yyyyMMdd_To_dMMMyyyy(date));
            }
        }
        DialogFragment dateFragment = new FragmentDatePicker();
        dateFragment.show(fm, "datePicker");
    }
}
