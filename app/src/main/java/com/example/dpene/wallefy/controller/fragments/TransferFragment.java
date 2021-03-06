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
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.User;

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


    private User user;


    public TransferFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        IToolbar toolbar = (IToolbar) getActivity();
        toolbar.setTitle("Transfer");

//        this.user = (User) getArguments().get("user");

        accounts = new ArrayList<>();
        if (MainActivity.user.getAccounts() != null) {
            for (Account acc : MainActivity.user.getAccounts()) {
                accounts.add(acc.getAccountName());
            }
        }

        View v = inflater.inflate(R.layout.fragment_transfer, container, false);

        spnFromAcc = (Spinner) v.findViewById(R.id.transfer_from_account_spinner);
        spnToAcc = (Spinner) v.findViewById(R.id.transfer_to_account_spinner);
        btnTransfer = (Button) v.findViewById(R.id.transfer_button);
        edtAmount = (EditText) v.findViewById(R.id.transfer_amount);

        spnFromAcc.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts));
        spnToAcc.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts));

        if (accounts.isEmpty()) {
            btnTransfer.setText("There are no accounts");
            btnTransfer.setEnabled(false);
        }

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spnFromAcc.getSelectedItem().equals(spnToAcc.getSelectedItem())) {
                    Toast.makeText(getContext(), "Please, choose two different accounts", Toast.LENGTH_SHORT).show();
                    return;
                } else if ((edtAmount.getText().toString().equals(""))) {
                    edtAmount.setError("Please, add your sum");
                    return;
                }
                else if (Double.valueOf(edtAmount.getText().toString()) < 0) {
                    edtAmount.setError("You cannot transfer negative sum");
                    return;
                } else {
                    // logic
                    Toast.makeText(getContext(), "You transfer " + edtAmount.getText().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        return v;
    }

//    TODO remove trash and add save implementation

}
