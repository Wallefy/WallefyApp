package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;
import com.example.dpene.wallefy.model.datasources.UserDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAccountsFragment extends Fragment implements View.OnClickListener {

    private ListView lvAccounts;
    private RelativeLayout addLayout;
    private ImageButton btnAdd;

    private ArrayList<Account> arrayListAccounts;

    User user;
    AccountsAdapter accountAdapter;

    public ListAccountsFragment() {
    }


    /**
     * OnItemClickListener sends intent to EditActivity
     * extras:
     * String key = IRequestCode
     * String title
     * ?   amount
     * ?String date
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = (User) getArguments().getSerializable("user");

        arrayListAccounts = new ArrayList<>();

        View v = inflater.inflate(R.layout.fragment_list_accounts, container, false);

        lvAccounts = (ListView) v.findViewById(R.id.list_accounts_listview);
        addLayout = (RelativeLayout) v.findViewById(R.id.list_accounts_add);
        btnAdd = (ImageButton) v.findViewById(R.id.list_accounts_add_btn);

//        new FillAccountsList().execute(user.getUserId());

        accountAdapter = new AccountsAdapter(getContext(), arrayListAccounts);

        lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editActivity = new Intent(getContext(), EditActivity.class);
                editActivity.putExtra("key", IRequestCodes.EDIT_ACCOUNT);
                editActivity.putExtra("title", arrayListAccounts.get(position).getAccountName());
                editActivity.putExtra("amount", ((TextView) ((ViewGroup) view).getChildAt(1)).getText().toString());
                editActivity.putExtra("date", "Not available");
                editActivity.putExtra("user", user);
                startActivity(editActivity);
            }
        });

        btnAdd.setOnClickListener(this);
        addLayout.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent editActivity = new Intent(getContext(), EditActivity.class);
        editActivity.putExtra("key", IRequestCodes.EDIT_ACCOUNT);
        editActivity.putExtra("user",user);
        startActivity(editActivity);
    }

    private class AccountsAdapter extends ArrayAdapter<Account> {

        private ArrayList<Account> accounts;


        public AccountsAdapter(Context context, List<Account> accs) {
            super(context, R.layout.row_account, accs);
            this.accounts = (ArrayList<Account>) accs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row;
            AccountsVH holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_account, parent, false);

                holder = new AccountsVH(row);
                row.setTag(holder);
            } else {
                row = convertView;
                holder = (AccountsVH) row.getTag();
            }

            Account acc = accounts.get(position);

            holder.name.setText(acc.getAccountName());
            if (acc.getAccountTempSum() < 0)
                holder.amount.setTextColor(Color.RED);
            else
                holder.amount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            holder.amount.setText(String.valueOf(acc.getAccountTempSum()));
            return row;
        }

        private class AccountsVH {

            private TextView name;
            private TextView amount;

            AccountsVH(View v) {
                name = (TextView) v.findViewById(R.id.row_account_name);
                amount = (TextView) v.findViewById(R.id.row_account_amount);

            }
        }
    }

    private class FillAccountsList extends AsyncTask<Long,Void,Void >{
        @Override
        protected Void doInBackground(Long... params) {
            arrayListAccounts.clear();
            IHistoryDao historyDataSource = HistoryDataSource.getInstance(getContext());
            ((HistoryDataSource)historyDataSource).open();
            IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
            ((AccountDataSource)accountDataSource).open();
            for (Account acc :
                    accountDataSource.showAllAccounts(user.getUserId())) {
                double amount = historyDataSource.calcAmountForAccount(params[0],acc.getAccountName());
                acc.setAccountTempSum(amount);
                arrayListAccounts.add(acc);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvAccounts.setAdapter(accountAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (accountAdapter != null) {
            new FillAccountsList().execute(user.getUserId());
            Log.e("TAG", "onResume: " );
        }
    }
}
