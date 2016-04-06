package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
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
public class MainInfoFragment extends Fragment implements View.OnClickListener {

    RelativeLayout balance;
    RecyclerView listHistory;
    RecyclerView listCategories;
    Spinner spnAccounts;
    TextView txtAccountBalanceTotal;
    ArrayList<History> historyByAccount;

    User user;

//    TODO Create initial categories and accounts
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        txtAccountBalanceTotal = (TextView) view.findViewById(R.id.main_info_balance_total);

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

        ArrayList<String> userAccountNames = new ArrayList<>();

        for (Account ac :
                user.getAccounts()) {
            userAccountNames.add(ac.getAccountName());
        }


        balance = (RelativeLayout) view.findViewById(R.id.main_info_balance);
        listHistory = (RecyclerView) view.findViewById(R.id.main_info_history);
        listCategories = (RecyclerView) view.findViewById(R.id.main_info_categories);

        spnAccounts = (Spinner) view.findViewById(R.id.spinner_main_info_accounts);
        spnAccounts.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userAccountNames));

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(),user.getCategories());

        final IHistoryDao historyDao = new HistoryDataSource(getContext());

        ((HistoryDataSource)historyDao).open();

       historyByAccount = new ArrayList<>();
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), historyByAccount);


        spnAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double totalBalanceForAccount = historyDao.calcAmountForAccount(user.getUserId(), spnAccounts.getSelectedItem().toString());

                txtAccountBalanceTotal.setText(String.valueOf(totalBalanceForAccount));
                historyByAccount = historyDao.listHistoryByAccountName(user.getUserId(), spnAccounts.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        LinearLayoutManager linLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        listCategories.setLayoutManager(linLayoutManager);
        listCategories.setAdapter(categoriesAdapter);


        listHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        listHistory.setAdapter(historyAdapter);

        balance.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {

        private Context context;
        private ArrayList<History> historyArrayList;

        HistoryAdapter(Context context,ArrayList<History> historyLog) {
            this.context = context;
            this.historyArrayList = historyLog;
        }


        @Override
        public HistoryVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
            return new HistoryAdapter.HistoryVH(row);

        }

        @Override
        public void onBindViewHolder(HistoryVH holder, final int position) {
            holder.img.setImageResource(historyArrayList.get(position).getCategoryIconResource());
            holder.category.setText(historyArrayList.get(position).getCategoryName());
            holder.date.setText(historyArrayList.get(position).getDateOfTransaction());
            holder.note.setText(historyArrayList.get(position).getDescription());
            holder.amount.setText(String.valueOf(historyArrayList.get(position).getAmount()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment entry = new TransactionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entryInfo", historyArrayList.get(position));
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.root_main, entry, "entry");
                    trans.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return historyArrayList.size();
        }


        protected class HistoryVH extends RecyclerView.ViewHolder {

            ImageView img;
            TextView category;
            TextView note;
            TextView date;
            TextView amount;

            public HistoryVH(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.icon_row_history);
                category = (TextView) itemView.findViewById(R.id.category_row_history);
                date = (TextView) itemView.findViewById(R.id.date_row_history);
                note = (TextView) itemView.findViewById(R.id.note_row_history);
                amount = (TextView) itemView.findViewById(R.id.amount_row_history);
            }
        }
    }

    class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesVH> {

        private Context context;

        private ArrayList<Category> categs;

        CategoriesAdapter(Context context,ArrayList<Category> categories) {
            this.context = context;
            this.categs = categories;
        }


        @Override
        public CategoriesAdapter.CategoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_category_icon_only, parent, false);
            return new CategoriesAdapter.CategoriesVH(row);

        }

        @Override
        public void onBindViewHolder(CategoriesVH holder, int position) {
//            TODO Change resources from long to int
            holder.img.setImageResource((int) categs.get(position).getIconResource());
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
}
