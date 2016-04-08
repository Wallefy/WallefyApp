package com.example.dpene.wallefy.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.model.classes.History;

import java.util.ArrayList;

public class ReportEntriesAdapter extends RecyclerView.Adapter<ReportEntriesAdapter.CustomVH> {

    private Context context;
    private ArrayList<History> entries;

    public ReportEntriesAdapter(Context context, ArrayList<History> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public CustomVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
        return new CustomVH(row);
    }

    @Override
    public void onBindViewHolder(CustomVH holder, final int position) {
        holder.img.setImageResource(entries.get(position).getCategoryIconResource());
        holder.category.setText(entries.get(position).getCategoryName());
        holder.date.setText(entries.get(position).getDateOfTransaction());
        holder.note.setText(entries.get(position).getDescription());
        holder.amount.setText( String.format("%.2f",entries.get(position).getAmount()));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Fragment entry = new TransactionFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("entryInfo", entries.get(position));
//                FragmentTransaction ft = ((Activity)context) .....
//                FragmentTransaction trans = cogetFragmentManager().beginTransaction();
//                trans.replace(R.id.root_main, entry, "entry");
//                trans.commit();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView category;
        TextView note;
        TextView date;
        TextView amount;

        public CustomVH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.icon_row_history);
            category = (TextView) itemView.findViewById(R.id.category_row_history);
            date = (TextView) itemView.findViewById(R.id.date_row_history);
            note = (TextView) itemView.findViewById(R.id.note_row_history);
            amount = (TextView) itemView.findViewById(R.id.amount_row_history);
        }
    }
}
