package com.example.dpene.wallefy.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dpene.wallefy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCategoryFragment extends Fragment {

    private EditText categoryName;
    private RecyclerView categoryIconsList;

    public EditCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_edit_category, container, false);

        categoryName = (EditText) v.findViewById(R.id.edit_category_edt_name);
        categoryIconsList = (RecyclerView) v.findViewById(R.id.edit_category_icon_recview);

        if (getArguments().get("isExpense") != null && getArguments().get("isExpense").equals("")) {
            boolean isExpend = getArguments().get("isExpense").equals("true");
        }

        categoryName.setText(getArguments().get("title").toString());


        return v;
    }


}
