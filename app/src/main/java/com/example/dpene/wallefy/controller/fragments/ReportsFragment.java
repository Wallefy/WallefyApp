package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dpene.wallefy.R;

import java.util.ArrayList;
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


    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categories = new ArrayList<>();
        accounts = new ArrayList<>();
        expenseIncome = new ArrayList<>();

        categories.add("Choose categories");
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
        spnCategories = (Spinner) v.findViewById(R.id.reports_categories);
        spnAccounts = (Spinner) v.findViewById(R.id.reports_accounts);
        spnExpenseIncome = (Spinner) v.findViewById(R.id.reports_expense_income);

        spnCategories.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categories));
        spnAccounts.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, accounts));
        spnExpenseIncome.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, expenseIncome));

        return v;
    }

}
