package com.example.dpene.wallefy.controller.fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;

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

    private User user;
    Account pojoAccount;

    public EditAccountFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);

        user = (User) getArguments().getSerializable("user");

        IToolbar toolbar = (IToolbar) getActivity();

        this.title = getArguments().getString("title");
        this.existingDate = getArguments().getString("date");
        this.amount = getArguments().getString("amount");


        tvTitle = (TextView) v.findViewById(R.id.edit_account_name);
        tvAmount = (TextView) v.findViewById(R.id.edit_account_init_balance);
        tvDate = (EditText) v.findViewById(R.id.edit_account_init_balance_date);
        btnCalendar = (ImageButton) v.findViewById(R.id.edit_account_calendar_btn);
        tvDate.setFocusable(false);
        tvDate.setInputType(InputType.TYPE_NULL);

        toolbar.setSubtitle("Create account");
        if (existingDate.length() != 0) {
            toolbar.setSubtitle("Edit account");
            tvAmount.setEnabled(false);
            tvAmount.setFocusable(false);
            tvAmount.setInputType(InputType.TYPE_NULL);
            tvDate.setEnabled(false);

            btnCalendar.setEnabled(false);
            btnCalendar.setFocusable(false);
        }

        tvTitle.setText(title);
        tvAmount.setText(amount);
        tvDate.setText(existingDate);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(tvDate, getFragmentManager());
            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(tvDate, getFragmentManager());
            }
        });
//      To modify toolbar btns override oncreateoptionsmenu
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (amount.length() <= 0)
            menu.removeItem(R.id.clear_values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.save_entry:
//                TODO if it is a new account - if there is initial balance there must be init date
                new SaveAccountTask(amount.length() <= 0).execute(tvTitle.getText().toString());
//                String selectedAccountType = spnAccountType.getSelectedItem().toString();
//                String selectedCategory = spnCategoryType.getSelectedItem().toString();
//                String calculatedAmount = amount.getText().toString();
//                if (Double.parseDouble(calculatedAmount) > 0) {
//                    new TaskSaveEntry(user.getUserId()).execute(selectedAccountType, selectedCategory,
//                            calculatedAmount,parent.setNote(), DateFormater.from_dMMMyyyy_To_yyyyMMddHHmmss(parent.setDate()));
//                    getActivity().finish();
//                }
//
//                else{
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage("Amount must be positive");
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
                return true;
            case R.id.clear_values:
                Toast.makeText(getContext(), "DELETE FROM DB", Toast.LENGTH_SHORT).show();
//                ((TextView) getActivity().findViewById(R.id.transaction_amount)).setText("0");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SaveAccountTask extends AsyncTask<String, Void, Boolean> {

        private boolean isNewAccount;

        public SaveAccountTask(boolean isNewAccount) {
            this.isNewAccount = isNewAccount;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
            ((AccountDataSource) accountDataSource).open();
//            TODO update existing account for current user
            if (this.isNewAccount)
                pojoAccount = accountDataSource.createAccount(user.getUserId(), params[0]);
            ((AccountDataSource) accountDataSource).close();
            if (pojoAccount != null) {
                user.addAccount(pojoAccount);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else
                Toast.makeText(getContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
        }
    }
}
