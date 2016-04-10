package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAccountsFragment extends Fragment implements View.OnClickListener{

    private ListView lvAccounts;
    private RelativeLayout addLayout;
    private ImageButton btnAdd;

    private ArrayList<Account> arrayListAccounts;

    public ListAccountsFragment() {
    }


    /**
     * OnItemClickListener sends intent to EditActivity
     * extras:
     *      String key = IRequestCode
     *      String title
     *         ?   amount
     *      ?String date
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arrayListAccounts = new ArrayList<>();
        arrayListAccounts.add(new Account(1, 1, "Cash"));
        arrayListAccounts.add(new Account(2, 1, "Savings"));
        arrayListAccounts.add(new Account(3, 1, "Card"));

        View v = inflater.inflate(R.layout.fragment_list_accounts, container, false);

        lvAccounts = (ListView) v.findViewById(R.id.list_accounts_listview);
        addLayout = (RelativeLayout) v.findViewById(R.id.list_accounts_add);
        btnAdd = (ImageButton) v.findViewById(R.id.list_accounts_add_btn);

        lvAccounts.setAdapter(new AccountsAdapter(getContext(), arrayListAccounts));

        lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editActivity = new Intent(getContext(), EditActivity.class);
                editActivity.putExtra("key", IRequestCodes.EDIT_ACCOUNT);
                editActivity.putExtra("title", arrayListAccounts.get(position).getAccountName());
                // TODO: getAmount()
                editActivity.putExtra("amount", ((TextView) ((ViewGroup) view).getChildAt(1)).getText().toString());
                // TODO: getInitialDate()
                editActivity.putExtra("date", "00-00-00");
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
            //TODO:
            holder.amount.setText("0.00");
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

}
