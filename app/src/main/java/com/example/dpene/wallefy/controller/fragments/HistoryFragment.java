package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.model.classes.History;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private ArrayList<History> historyList;
    private RecyclerView history;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        historyList = new ArrayList<>();

        View v = inflater.inflate(R.layout.fragment_history, container, false);

        history = (RecyclerView) v.findViewById(R.id.history_recview);
        HistoryAdapter adapter = new HistoryAdapter(getContext(), historyList);
        history.setLayoutManager(new LinearLayoutManager(getContext()));
        history.setAdapter(adapter);

        return v;
    }


    //TODO in outer class
    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {

        private Context context;
        private ArrayList<History> historyArrayList;

        HistoryAdapter(Context context, ArrayList<History> historyLog) {
            this.context = context;
            this.historyArrayList = historyLog;
        }


        @Override
        public HistoryVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
            return new HistoryAdapter.HistoryVH(row);

        }

        @Override
        public void onBindViewHolder(HistoryVH holder, int position) {
//            TODO
//            holder.img.setImageResource(historyArrayList.get(position).get);
            holder.category.setText("categori ID " + historyArrayList.get(position).getCategoryId());
            holder.date.setText(historyArrayList.get(position).getDateOfTransaction());
            holder.note.setText(historyArrayList.get(position).getDescription());
            holder.amount.setText(String.valueOf(historyArrayList.get(position).getAmount()));
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

}
