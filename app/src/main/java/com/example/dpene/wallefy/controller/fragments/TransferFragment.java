package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dpene.wallefy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferFragment extends Fragment {

    private EditText edtAmount;
    private Spinner spnFromAcc;
    private Spinner spnToAcc;
    private Button btnTransfer;

    private List<String> accounts;


    public TransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        accounts = new ArrayList<>();
        accounts.add("Cash");
        accounts.add("Card");

        View v = inflater.inflate(R.layout.fragment_transfer, container, false);

        spnFromAcc = (Spinner) v.findViewById(R.id.transfer_from_account_spinner);
        spnToAcc = (Spinner) v.findViewById(R.id.transfer_to_account_spinner);
        btnTransfer = (Button) v.findViewById(R.id.transfer_button);
        edtAmount = (EditText) v.findViewById(R.id.transfer_amount);

        spnFromAcc.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts));
        spnToAcc.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts));

        return v;
    }

}
