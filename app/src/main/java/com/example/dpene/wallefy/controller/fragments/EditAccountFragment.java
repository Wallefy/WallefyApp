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
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.controller.controllerutils.ControllerConstants;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;
import com.example.dpene.wallefy.model.datasources.UserDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountFragment extends Fragment {

    private TextView tvEditAccountName;
    private TextView tvAmount;
    private EditText tvDate;
    private ImageButton btnCalendar;

    private String existingDate;
    private String title;
    private String amount;

    private Account pojoAccount;

    public EditAccountFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);

//        user = (User) getArguments().getSerializable("user");

        IToolbar toolbar = (IToolbar) getActivity();

        this.title = getArguments().getString("title");
        this.existingDate = getArguments().getString("date");
        this.amount = getArguments().getString("amount");

        tvEditAccountName = (TextView) v.findViewById(R.id.edit_account_name);
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

        tvEditAccountName.setText(title);
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
//                TODO check for words in name  - if either balance or date are not empty must fill both
//                ^\s*$  - if all whitespaces
                if (tvEditAccountName.getText().toString().matches("^\\s*$") || tvEditAccountName.getText().toString().length() <= 0)
                    Toast.makeText(getContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
                else {
                    new SaveAccountTask(amount.length() <= 0).execute(tvEditAccountName.getText().toString(), title,
                            tvDate.getText().toString(), tvAmount.getText().toString());
                }
                return true;
            case R.id.clear_values:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("DELETE Account: " + tvEditAccountName.getText().toString());
                builder.setMessage("All entries for this account would be deleted!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteAccountTask(MainActivity.user.getUserId()).execute(tvEditAccountName.getText().toString());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
//            tvEditAccountName.getText().toString(), title,
//                    tvDate.getText().toString(), tvAmount.getText().toString()

            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource) userDataSource).open();

            String accName = params[0];
            String oldName = params[1];
            String date = DateFormater.from_dMMMyyyy_To_yyyyMMddHHmmss(params[2]);

            String amount = params[3];
            if (params[3].length() <= 0)
                amount = "0";

            IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
            ((AccountDataSource) accountDataSource).open();
            if (this.isNewAccount) {
                pojoAccount = accountDataSource.createAccount(MainActivity.user.getUserId(), accName);
                if (pojoAccount == null)
                    return false;
                if (date.length() > 0) {
                    IHistoryDao historyDataSource = HistoryDataSource.getInstance(getContext());
                    ((HistoryDataSource) historyDataSource).open();
                    History hist = historyDataSource.createHistory(MainActivity.user.getUserId(), pojoAccount.getAccountTypeId(),
                            ControllerConstants.CATEGORY_INITIAL_BALANCE, Double.parseDouble(amount), null, date, null, null);
                    if (hist == null)
                        return false;
                }
            } else
                pojoAccount = accountDataSource.updateAccount(MainActivity.user.getUserId(), accName, oldName);
            if (pojoAccount != null) {
                MainActivity.user.setAccounts(accountDataSource.showAllAccounts(MainActivity.user.getUserId()));
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
                Toast.makeText(getContext(), "Failed to create account. Possible duplicate name!", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteAccountTask extends AsyncTask<String, Void, Boolean> {

        private long userId;

        public DeleteAccountTask(long userId) {
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String newAccountName = params[0];

            IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
            ((AccountDataSource) accountDataSource).open();
            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource) userDataSource).open();
            if (accountDataSource.deleteAccount(userId, newAccountName)) {
                MainActivity.user.setAccounts(accountDataSource.showAllAccounts(userId));
                MainActivity.user.setHistoryLog(HistoryDataSource.getInstance(getContext()).listAllHistory(userId));
//                MainActivity.user.deleteAccount(newAccountName);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "DELETE SUCCESS", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else
                Toast.makeText(getContext(), "DELETE FAILED", Toast.LENGTH_SHORT).show();
        }
    }
}
