package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISaveSpinnerPosition;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainInfoFragment extends Fragment {

    RelativeLayout balance;
    RecyclerView listHistory;
    RecyclerView listCategories;
    Spinner spnAccounts;
    TextView txtAccountBalanceTotal;

    User user;

    ArrayList<String> accounts;
    ArrayList<History> entries;
    ReportEntriesAdapter rea;
    IHistoryDao historyDataSource;
    ArrayAdapter accountAdapter;

    String selectedAccount;

    int position;
    ISaveSpinnerPosition mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (ISaveSpinnerPosition) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        historyDataSource = HistoryDataSource.getInstance(getContext());
        txtAccountBalanceTotal = (TextView) view.findViewById(R.id.main_info_balance_total);

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

        ArrayList<String> userAccountNames = new ArrayList<>();

        for (Account ac :
                user.getAccounts()) {
            userAccountNames.add(ac.getAccountName());
        }

        balance = (RelativeLayout) view.findViewById(R.id.main_info_balance);
        listCategories = (RecyclerView) view.findViewById(R.id.main_info_categories);

        accountAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, accounts);
        listHistory = (RecyclerView) view.findViewById(R.id.main_info_history);

        spnAccounts = (Spinner) view.findViewById(R.id.spinner_main_info_accounts);
        spnAccounts.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userAccountNames));

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(), user.getCategories());

        spnAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new TaskFillFilteredEntries().execute(String.valueOf(user.getUserId()), spnAccounts.getSelectedItem().toString());
                selectedAccount = spnAccounts.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayoutManager linLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        listCategories.setLayoutManager(linLayoutManager);
        listCategories.setAdapter(categoriesAdapter);

        return view;


    }

    class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesVH> {

        private Context context;

        private ArrayList<Category> categs;

        CategoriesAdapter(Context context, ArrayList<Category> categories) {
            this.context = context;
            this.categs = categories;
        }


        @Override
        public CategoriesAdapter.CategoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_category_icon_only, parent, false);
            return new CategoriesAdapter.CategoriesVH(row);

        }

        /**
         * itemView.OnClickListener sends intent to EditActivity
         * extras:
         * String        key               IRequestCode.EDIT_TRANSACTION
         * String        category
         * String        selectedAccount
         * Serializable  user
         */
        @Override
        public void onBindViewHolder(CategoriesVH holder, final int position) {
//            TODO Change resources from long to int
            holder.img.setImageResource((int) categs.get(position).getIconResource());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editActivity = new Intent(getContext(), EditActivity.class);
                    editActivity.putExtra("key", IRequestCodes.EDIT_TRANSACTION);
                    editActivity.putExtra("category", categs.get(position).getCategoryName());
                    editActivity.putExtra("account", selectedAccount);
                    editActivity.putExtra("user", user);
                    startActivity(editActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categs.size();
        }

        protected class CategoriesVH extends RecyclerView.ViewHolder {

            ImageView img;

            public CategoriesVH(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.row_category_icon_only);
            }
        }
    }

    class TaskFillFilteredEntries extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            ((HistoryDataSource) historyDataSource).open();
            entries = historyDataSource.listHistoryByAccountName(Long.parseLong(params[0]), params[1]);
            return historyDataSource.calcAmountForAccount(Long.parseLong(params[0]), params[1]);
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            rea = new ReportEntriesAdapter(getContext(), entries,user);
            rea.notifyDataSetChanged();
            listHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            listHistory.setAdapter(rea);
            txtAccountBalanceTotal.setText(String.format("%.2f", aDouble));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        position = mainActivity.getPosition();
        spnAccounts.setSelection(position);
        Log.e("POSITION", position + " onresume");
    }

    @Override
    public void onPause() {
        super.onPause();
        position = spnAccounts.getSelectedItemPosition();
        mainActivity.setPosition(position);
    }
}
