package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;

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
    private boolean isExpense;

    private IToolbar toolbar;

    private ArrayList<Integer> icons;

    public EditCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = (IToolbar) getActivity();

        if (getArguments().get("isExpense") != null ) {
            isExpense = getArguments().get("isExpense").equals("true");
            toolbar.setSubtitle(isExpense ? "Expense" : "Income");
        }
        if (getArguments().getString("categoryType") != null)
            toolbar.setSubtitle(getArguments().getString("categoryType"));

        setHasOptionsMenu(true);


        icons = new ArrayList<>();
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


        categoryName.setText(getArguments().get("title").toString());

        categoryIconsList = (GridView) v.findViewById(R.id.edit_category_icon_gridview);
        categoryIconsList.setNumColumns(4);
        categoryIconsList.setAdapter(new IconAdapter(getContext(), icons));

        if (getArguments().get("categoryIcon")!=null) {
            long iconResource = (long) getArguments().get("categoryIcon");

            if (iconResource != 0)
                imgSelectedIcon.setImageResource((int) (iconResource));
            else
                imgSelectedIcon.setImageResource(icons.get(0));
        }

        categoryIconsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                imgSelectedIcon.setImageResource( icons.get(position));
                selectedIcon = icons.get(position);
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
