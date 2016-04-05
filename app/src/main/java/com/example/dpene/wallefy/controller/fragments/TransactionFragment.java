package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dpene.wallefy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {


    private TextView tvCategoryType;
    private TextView tvAccountType;

    private TextView amount;
    private TextView currency;

    private Spinner spnAccountType;
    private Spinner spnCategoryType;

    private List<String> listCategoriest;
    private List<String> listAccounts;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listCategoriest = new ArrayList<>();
        listAccounts = new ArrayList<>();

        listCategoriest.add("Food");
        listCategoriest.add("Drink");
        listCategoriest.add("Clothes");
        listCategoriest.add("Things");

        listAccounts.add("Cash");
        listAccounts.add("Card");

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        tvCategoryType = (TextView) v.findViewById(R.id.transaction_type_category);
        tvAccountType = (TextView) v.findViewById(R.id.transaction_type_account);
        amount = (TextView) v.findViewById(R.id.transaction_amount);
        currency = (TextView) v.findViewById(R.id.transaction_amount_currency);

        spnAccountType = (Spinner) v.findViewById(R.id.transaction_amount);
        spnCategoryType = (Spinner) v.findViewById(R.id.transaction_category);

        spnAccountType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listAccounts));
        spnCategoryType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listCategoriest));

        return v;
    }

}
