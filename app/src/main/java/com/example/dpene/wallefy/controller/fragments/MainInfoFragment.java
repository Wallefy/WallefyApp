package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainInfoFragment extends Fragment implements View.OnClickListener {

    private int[] categoryImgs;
    GridView gridCategoryImgs;
    RelativeLayout balance;
    RecyclerView listHistory;
    RecyclerView listCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        Log.e("ASDASD", "asd");
        categoryImgs = new int[9];
        for (int i = 0; i < categoryImgs.length; i++) {
            categoryImgs[i] = R.drawable.ghost;
        }

        balance = (RelativeLayout) view.findViewById(R.id.main_info_balance);
        listHistory = (RecyclerView) view.findViewById(R.id.main_info_history);
        listCategories = (RecyclerView) view.findViewById(R.id.main_info_categories);

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext());
        LinearLayoutManager linLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        listCategories.setLayoutManager(linLayoutManager);
        listCategories.setAdapter(categoriesAdapter);

        HistoryAdapter historyAdapter = new HistoryAdapter(getContext());
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
        private String[] plannedArray = {"neshto1, neshto2, neshto3, neshto4"};

        HistoryAdapter(Context context) {
            this.context = context;

        }


        @Override
        public HistoryVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
            return new HistoryAdapter.HistoryVH(row);

        }

        @Override
        public void onBindViewHolder(HistoryVH holder, int position) {
            holder.img.setImageResource(R.drawable.ghost);
            holder.category.setText("Test");
            holder.date.setText("00-00-00");
            holder.note.setText("Note..");
            holder.amount.setText("00.00");
        }

        @Override
        public int getItemCount() {
            return plannedArray.length;
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
        private String[] plannedArray = {"neshto1, neshto2, neshto3, neshto4"};

        CategoriesAdapter(Context context) {
            this.context = context;

        }


        @Override
        public CategoriesAdapter.CategoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
            return new CategoriesAdapter.CategoriesVH(row);

        }

        @Override
        public void onBindViewHolder(CategoriesVH holder, int position) {
            holder.img.setImageResource(R.drawable.ghost_48);
        }

        @Override
        public int getItemCount() {
            return 10;
        }


        protected class CategoriesVH extends RecyclerView.ViewHolder {

            ImageView img;


            public CategoriesVH(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.row_category_icon);

            }
        }

    }
}
