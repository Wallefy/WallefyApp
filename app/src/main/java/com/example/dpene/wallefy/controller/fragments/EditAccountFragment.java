package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountFragment extends Fragment {

    private TextView title;
    private TextView amount;
    private TextView date;

    public EditAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);

        IToolbar toolbar = (IToolbar) getActivity();
        toolbar.setTitle("Account");

        title = (TextView) v.findViewById(R.id.edit_account_name);
        amount = (TextView) v.findViewById(R.id.edit_account_init_balance);
        date = (TextView) v.findViewById(R.id.edit_account_init_balance_date);

        title.setText(getArguments().get("title").toString());
        amount.setText(getArguments().get("amount").toString());
        date.setText(getArguments().get("date").toString());

        return v;
    }

}
