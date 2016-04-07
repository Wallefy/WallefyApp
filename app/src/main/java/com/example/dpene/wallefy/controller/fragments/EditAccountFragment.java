package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dpene.wallefy.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountFragment extends Fragment {

    private TextView title;
    private TextView amount;

    public EditAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);

        title = (TextView) v.findViewById(R.id.edit_account_name);
        amount = (TextView) v.findViewById(R.id.edit_account_init_balance);

        title.setText(getArguments().get("title").toString());
        amount.setText(getArguments().get("amount").toString());
        amount.setText(getArguments().get("date").toString());

        return v;
    }

}
