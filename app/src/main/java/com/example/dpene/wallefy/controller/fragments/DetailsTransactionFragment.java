package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.controller.gesturelistener.OnSwipeGestureListener;

public class DetailsTransactionFragment extends Fragment {

    private TextView note;
    private TextView date;

    // vars from transactionFragment
    private double amount;
    private String category;
    private String account;

    private LinearLayout detailsTransactionView;

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

        note = (TextView) v.findViewById(R.id.details_transaction_note);
        date = (TextView) v.findViewById(R.id.details_transaction_date);
        detailsTransactionView = (LinearLayout) v.findViewById(R.id.fragment_details_layout);

        // initialize vars from transactionFragment
        this.amount = getArguments().getDouble("amount");
        this.category = getArguments().getString("category");
        this.account = getArguments().getString("account");

        detailsTransactionView.setOnTouchListener(new OnSwipeGestureListener(getContext()) {
            public void onSwipeRight() {
                Bundle bundle = new Bundle();

                bundle.putString("note", note.getText().toString());
                bundle.putString("date", date.getText().toString());
                // put picture and location

                parent.notifyFragment(new DetailsTransactionFragment(), bundle);
            }

            public void onSwipeLeft() {
            }

        });


        return v;
    }


}
