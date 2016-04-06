package com.example.dpene.wallefy.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dpene.wallefy.R;
import com.github.mikephil.charting.charts.PieChart;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {

    private LinearLayout root;
    private PieChart pieChart;

    public PieChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        root = (LinearLayout) v.findViewById(R.id.pie_chart_layout);

        return v;
    }

}
