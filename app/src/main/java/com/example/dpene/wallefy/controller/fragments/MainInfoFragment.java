package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainInfoFragment extends Fragment implements View.OnClickListener {

    RelativeLayout balance;
    RecyclerView listHistory;
    RecyclerView listCategories;
    User user;

//    TODO Create initial categories and accounts
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");


        balance = (RelativeLayout) view.findViewById(R.id.main_info_balance);
        listHistory = (RecyclerView) view.findViewById(R.id.main_info_history);
        listCategories = (RecyclerView) view.findViewById(R.id.main_info_categories);

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(),user.getCategories());
        LinearLayoutManager linLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        listCategories.setLayoutManager(linLayoutManager);
        listCategories.setAdapter(categoriesAdapter);

        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(),user.getHistoryLog());
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
            this.historyArrayList =historyLog;
        }


        @Override
        public HistoryVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
            return new HistoryAdapter.HistoryVH(row);

        }

        @Override
        public void onBindViewHolder(HistoryVH holder, final int position) {
//            TODO
//            holder.img.setImageResource(historyArrayList.get(position).get);
            holder.category.setText("categori ID " + historyArrayList.get(position).getCategoryId());
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
