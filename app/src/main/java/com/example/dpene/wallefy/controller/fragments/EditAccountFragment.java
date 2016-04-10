package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvAmount;
    private EditText tvDate;
    private ImageButton btnCalendar;

    private String existingDate;
    private String title;
    private String amount;

    public EditAccountFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);

        IToolbar toolbar = (IToolbar) getActivity();
        toolbar.setTitle("Account");

        this.existingDate = getArguments().getString("date");
        this.title = getArguments().getString("title");
        this.amount = getArguments().getString("amount");

        if(existingDate.length() == 0){
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("d MMM,yyyy");
            this.existingDate = df.format(currentDate);
        }

        tvTitle = (TextView) v.findViewById(R.id.edit_account_name);
        tvAmount = (TextView) v.findViewById(R.id.edit_account_init_balance);
        tvDate = (EditText) v.findViewById(R.id.edit_account_init_balance_date);
        btnCalendar = (ImageButton) v.findViewById(R.id.edit_account_calendar_btn);

        tvTitle.setText(title);
        tvAmount.setText(amount);
        tvDate.setText(existingDate);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(tvDate, getFragmentManager());
            }
        });


        return v;
    }

}
