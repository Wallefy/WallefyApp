package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.controller.gesturelistener.OnSwipeGestureListener;
import com.example.dpene.wallefy.model.classes.User;

public class DetailsTransactionFragment extends Fragment {

    private TextView note;
    private TextView date;
    private ImageButton chooseDate;

    // vars from transactionFragment
    private double amount;
    private String category;
    private String account;
    private String existingDate;

    private LinearLayout detailsTransactionView;

    private User user;

    private ITransactionCommunicator parent;

    IToolbar toolbar;

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
        this.amount = getArguments().getDouble("amount");
        this.category = getArguments().getString("category");
        this.account = getArguments().getString("account");
        this.existingDate = getArguments().getString("date");

//        toolbar = (IToolbar) getActivity();
//        toolbar.setTitle(String.valueOf(amount));

        note = (TextView) v.findViewById(R.id.details_transaction_note);
        date = (TextView) v.findViewById(R.id.details_transaction_date);
        chooseDate = (ImageButton) v.findViewById(R.id.description_calendar_button);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick( date, getFragmentManager());
            }
        });
        detailsTransactionView = (LinearLayout) v.findViewById(R.id.fragment_details_layout);

        note.setText(getArguments().getString("note"));
        date.setText(existingDate);
        note.setText(parent.setNote());
        date.setText(parent.setDate());

        detailsTransactionView.setOnTouchListener(new OnSwipeGestureListener(getContext()) {
            public void onSwipeRight() {
                Bundle bundle = new Bundle();

                bundle.putDouble("amount", getArguments().getDouble("amount"));
                bundle.putString("category", getArguments().getString("category"));
                bundle.putString("account", getArguments().getString("account"));
                bundle.putString("note", note.getText().toString());
                bundle.putString("date", date.getText().toString());
                bundle.putSerializable("user", user);
                // TODO put picture and location

                parent.notifyFragment(new TransactionFragment(), bundle);
            }

            public void onSwipeLeft() {
            }

        });


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
    public void onResume() {
        super.onResume();
        Log.e("Custom", "resume details: ");
    }

}
