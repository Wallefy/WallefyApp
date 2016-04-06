package com.example.dpene.wallefy.controller.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {

    private List<String> categories;
    private List<String> accounts;
    private List<String> expenseIncome;

    private EditText edtDate;
    private Spinner spnCategories;
    private Spinner spnAccounts;
    private Spinner spnExpenseIncome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categories = new ArrayList<>();
        accounts = new ArrayList<>();
        expenseIncome = new ArrayList<>();

        categories.add("Choose category");
        categories.add("Food");
        categories.add("Clothes");
        categories.add("Foots");
        categories.add("Boots");

        accounts.add("Choose account");
        accounts.add("Cash");
        accounts.add("Card");

        expenseIncome.add("Choose expense/income");
        expenseIncome.add("Expense");
        expenseIncome.add("Income");


        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        edtDate = (EditText) v.findViewById(R.id.reports_date);
        edtDate.setCursorVisible(false);
        edtDate.setKeyListener(null);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDate((EditText) v);
            }
        });

        spnCategories = (Spinner) v.findViewById(R.id.reports_categories);
        spnAccounts = (Spinner) v.findViewById(R.id.reports_accounts);
        spnExpenseIncome = (Spinner) v.findViewById(R.id.reports_expense_income);

        spnCategories.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categories));
        spnAccounts.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, accounts));
        spnExpenseIncome.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, expenseIncome));

        return v;
    }

    private void picDate(final EditText edt) {
        class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                String date = year + "-" + ((monthOfYear < 10) ? "0" : "") + monthOfYear + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                edt.setText(DateFormater.from_yyyyMMdd_To_dMMMyyyy(date));
            }
        }
            DialogFragment dateFragment = new FragmentDatePicker();
            dateFragment.show(getFragmentManager(), "datePicker");
    }

}
