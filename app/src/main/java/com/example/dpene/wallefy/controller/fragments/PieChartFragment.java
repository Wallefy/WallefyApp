package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IPieChartCommunicator;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISaveSpinnerPosition;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {

    private IPieChartCommunicator parent;

    private ArrayList<Category> categories;
    private ArrayList<String> categoriyName;
    private ArrayList<Double> categoriySum;
    private Map<Category, Double> categoriyStatistic;


    private LinearLayout root;

    private PieChart pieChart;

    private Typeface tf;

    private User user;
    int selectedAccountPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = (IPieChartCommunicator) context;
        Log.e("PIECHART", "Atach:account " + selectedAccountPosition );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");
        selectedAccountPosition = parent.getPosition();


        categories = new ArrayList<>();
        categoriyName = new ArrayList<>();
        categoriySum = new ArrayList<>();
        categoriyStatistic = new LinkedHashMap<>();

        categories.add(new Category(1, "Food1", true, R.drawable.ghost, 1 ));
        categories.add(new Category(2, "Food2", true, R.drawable.ghost, 1 ));
        categories.add(new Category(3, "Food3", true, R.drawable.ghost, 1 ));
        categories.add(new Category(4, "Food4", true, R.drawable.ghost, 1 ));

        categoriySum.add((double) 5);
        categoriySum.add((double) 8);
        categoriySum.add((double) 1);
        categoriySum.add((double) 10);

        for(int i = 0; i < 4; i++) {
            categoriyStatistic.put(categories.get(i), categoriySum.get(i));
        }

        View v = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        root = (LinearLayout) v.findViewById(R.id.pie_chart_layout);

        pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        setData(categoriySum.size(), 100);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        return v;
    }

    @Override
    public void onValueSelected(Entry entry, int dataSetIndex, Highlight highlight) {
        if (entry == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + entry.getVal() + ", xIndex: " + entry.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float)( categoriySum.get(i) * mult) + mult / 5, i));

            Log.e("tag", yVals1.get(i) + "  " + categoriySum.get(i) + " i = " + i);
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for(Map.Entry<Category, Double> entry : categoriyStatistic.entrySet()){
           categoriyName.add(entry.getKey().getCategoryName());
        }

        for (int i = 0; i < count; i++) {
            xVals.add(categoriyName.get(i % categoriyName.size()));

            Log.e("tag", xVals.get(i) + "  " + categoriyName.get(i));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Categories");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("PIECHART", "resum:account " + selectedAccountPosition);
    }
}
