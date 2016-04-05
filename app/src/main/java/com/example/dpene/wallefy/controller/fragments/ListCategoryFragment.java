package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dpene.wallefy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListCategoryFragment extends Fragment {


    public ListCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_category, container, false);
    }

}
