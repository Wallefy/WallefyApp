package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.model.classes.Category;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCategoryFragment extends Fragment {

    private EditText categoryName;
    private GridView categoryIconsList;
    private ImageView imgSelectedIcon;

    // category's characteristics
    private int selectedIcon;
    private boolean isExpend;

    private ArrayList icons;

    public EditCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        icons = new ArrayList();
        icons.add(R.drawable.eating_56);
        icons.add(R.drawable.bills_56);
        icons.add(R.drawable.clothes_56);
        icons.add(R.drawable.car_56);
        icons.add(R.drawable.fitnes_56);
        icons.add(R.drawable.medic_56);
        icons.add(R.drawable.phone_56);
        icons.add(R.drawable.house_56);
        icons.add(R.drawable.gift_56);
        icons.add(R.drawable.animals_56);
        icons.add(R.drawable.film_56);
        icons.add(R.drawable.taxi_56);
        icons.add(R.drawable.sport_56);
        icons.add(R.drawable.vacations_56);
        icons.add(R.drawable.ghost_48);
        icons.add(R.drawable.support_56);

        View v = inflater.inflate(R.layout.fragment_edit_category, container, false);

        categoryName = (EditText) v.findViewById(R.id.edit_category_edt_name);
        imgSelectedIcon = (ImageView) v.findViewById(R.id.edit_category_selected_icon);

        if (getArguments().get("isExpense") != null && getArguments().get("isExpense").equals("")) {
            isExpend = getArguments().get("isExpense").equals("true");
        }

        categoryName.setText(getArguments().get("title").toString());

        categoryIconsList = (GridView) v.findViewById(R.id.edit_category_icon_gridview);
        categoryIconsList.setNumColumns(4);
        categoryIconsList.setAdapter(new IconAdapter(getContext(), icons));

        imgSelectedIcon.setImageResource((Integer) icons.get(0));

        categoryIconsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                imgSelectedIcon.setImageResource((Integer) icons.get(position));
                selectedIcon = (Integer) icons.get(position);
            }
        });

        return v;
    }

    private class IconAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Integer> icons;

        IconAdapter(Context context, ArrayList<Integer> icons) {
            this.context = context;
            this.icons = icons;
        }

        @Override
        public int getCount() {
            return this.icons.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            imageView = new ImageView(context);
            imageView.setPadding(8, 8, 8, 8);

            imageView.setImageResource(icons.get(position));
            return imageView;
        }
    }

}
