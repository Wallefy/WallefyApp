package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.controller.gesturelistener.OnSwipeGestureListener;
import com.example.dpene.wallefy.model.classes.User;

public class DetailsTransactionFragment extends Fragment {

    private TextView note;
    private TextView date;

    // vars from transactionFragment
    private double amount;
    private String category;
    private String account;

    private LinearLayout detailsTransactionView;

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

        note = (TextView) v.findViewById(R.id.details_transaction_note);
        date = (TextView) v.findViewById(R.id.details_transaction_date);
        detailsTransactionView = (LinearLayout) v.findViewById(R.id.fragment_details_layout);

        // initialize vars from transactionFragment
        this.user = (User) getArguments().getSerializable("user");
        this.amount = getArguments().getDouble("amount");
        this.category = getArguments().getString("category");
        this.account = getArguments().getString("account");

        note.setText(getArguments().getString("note"));
        date.setText(getArguments().getString("date"));

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


        return v;
    }


}
