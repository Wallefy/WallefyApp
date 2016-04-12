package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.model.classes.User;

public class DetailsTransactionFragment extends Fragment {

    private TextView note;
    private TextView date;
    private ImageButton chooseDate;

    private String existingDate;

    private User user;

    private ITransactionCommunicator parent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = (ITransactionCommunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_details_transaction, container, false);

        // initialize vars from transactionFragment
        this.user = (User) getArguments().getSerializable("user");
        this.existingDate = getArguments().getString("date");

        note = (TextView) v.findViewById(R.id.details_transaction_note);
        date = (TextView) v.findViewById(R.id.details_transaction_date);
        chooseDate = (ImageButton) v.findViewById(R.id.description_calendar_button);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(date, getFragmentManager());
            }
        });

        setHasOptionsMenu(true);

        note.setText(getArguments().getString("note"));
        date.setText(existingDate);
        note.setText(parent.setNote());
        date.setText(parent.setDate());

        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                parent.getNote(note.getText().toString());
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                parent.getDate(date.getText().toString());
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}
