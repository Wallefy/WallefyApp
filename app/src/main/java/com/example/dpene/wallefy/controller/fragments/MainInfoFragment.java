package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.model.classes.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainInfoFragment extends Fragment {

    private int[] categoryImgs;
    GridView gridCategoryImgs;
    Button btn ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        Log.e("ASDASD","asd");
        categoryImgs = new int[9];
        for (int i = 0; i < categoryImgs.length; i++) {
            categoryImgs[i] = R.drawable.categroryimg;
        }

        btn = (Button) view.findViewById(R.id.button);

        gridCategoryImgs = (GridView) view.findViewById(R.id.grid_categories);
        gridCategoryImgs.setNumColumns(3);
        gridCategoryImgs.setAdapter(new CategoryImgAdapter(getContext(), categoryImgs));

        return view;
    }

    private class CategoryImgAdapter extends BaseAdapter {

        private Context context;
        private int[] elements;

        CategoryImgAdapter( Context c, int[] elements){
            this.context = c;
            this.elements = elements;
        }

        @Override
        public int getCount() {
            return this.elements.length;
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
            ImageView img;
            if (convertView == null){
                img = new ImageView(context);

            }
            else
            {
                img = (ImageView) convertView;
            }
            img.setImageResource(elements[position]);
            return img;
        }
    }
}
