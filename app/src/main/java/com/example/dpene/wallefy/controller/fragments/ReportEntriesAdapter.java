package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;

import java.util.ArrayList;

public class ReportEntriesAdapter extends RecyclerView.Adapter<ReportEntriesAdapter.CustomVH> {

    private Context context;
    private ArrayList<History> entries;
    private User currentUser;
    private int isLast;

    public ReportEntriesAdapter(Context context, ArrayList<History> entries,User user) {
        this.context = context;
        this.currentUser = user;
        if (entries == null)
            entries = new ArrayList<>();
        this.entries = entries;
    }

    @Override
    public CustomVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
        return new CustomVH(row);
    }

    /**
     * itemView.setOnClickListener starts intent to EditActivity
     * extras:
     *      String          key     IRequestCodes.EDIT_TRANSACTION
     *      Serializable    entry
     *      Serializable    user
     */
    @Override
    public void onBindViewHolder(CustomVH holder, final int position) {
        holder.img.setImageResource(entries.get(position).getCategoryIconResource());
        holder.category.setText(entries.get(position).getCategoryName());
        holder.date.setText(DateFormater.from_yyyyMMddHHmmss_To_dMMMyyyy(entries.get(position).getDateOfTransaction()));
        holder.note.setText(entries.get(position).getDescription());
        if (isLast == 1)
            holder.lineSepatator.setVisibility(View.GONE);
        else
            holder.lineSepatator.setVisibility(View.VISIBLE);
        if (entries.get(position).getAmount() < 0)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.color_red_dark));
        else
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.color_green_dark));
        holder.amount.setText( String.format("%.2f", entries.get(position).getAmount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editActivity = new Intent(context, EditActivity.class);
                editActivity.putExtra("key", IRequestCodes.EDIT_TRANSACTION);
                editActivity.putExtra("entry", entries.get(position));
//                editActivity.putExtra("user", currentUser);
                context.startActivity(editActivity);

            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

//    Check if the last file is reached
    @Override
    public int getItemViewType(int position) {
        if (position == entries.size()-1){
            return isLast=1;
        }
        return isLast=0;
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView category;
        TextView note;
        TextView date;
        TextView amount;
        View lineSepatator;

        public CustomVH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.icon_row_history);
            category = (TextView) itemView.findViewById(R.id.category_row_history);
            date = (TextView) itemView.findViewById(R.id.date_row_history);
            note = (TextView) itemView.findViewById(R.id.note_row_history);
            amount = (TextView) itemView.findViewById(R.id.amount_row_history);
            lineSepatator =  itemView.findViewById(R.id.line_separator);
        }
    }
}
