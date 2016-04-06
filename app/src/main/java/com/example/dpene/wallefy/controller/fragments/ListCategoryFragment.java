package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.fragments.comunicators.IRequestCodes;
import com.example.dpene.wallefy.model.classes.Category;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListCategoryFragment extends Fragment implements View.OnClickListener {

    private RecyclerView listIncomeCategories;
    private RecyclerView listExpenseCategories;
    private Button btnAddIncomeCateg;
    private Button btnAddExpenseCateg;
    private RelativeLayout incomeLayout;
    private RelativeLayout expenseLayout;

    private ArrayList<Category> incomeCategs;
    private ArrayList<Category> expenseCategs;


    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        incomeCategs = new ArrayList<>();
        expenseCategs = new ArrayList<>();

        incomeCategs.add(new Category(1, "Food", false, R.drawable.ghost, 1));
        incomeCategs.add(new Category(2, "NeFood", false, R.drawable.ghost, 1));
        incomeCategs.add(new Category(3, "NeneFood", false, R.drawable.ghost, 1));

        expenseCategs.add(new Category(4, "Food", true, R.drawable.calendar, 1));
        expenseCategs.add(new Category(5, "NeFood", true, R.drawable.calendar, 1));
        expenseCategs.add(new Category(6, "NeneFood", true, R.drawable.calendar, 1));


        View v = inflater.inflate(R.layout.fragment_list_category, container, false);

        listIncomeCategories = (RecyclerView) v.findViewById(R.id.category_income_recview);
        listExpenseCategories = (RecyclerView) v.findViewById(R.id.category_expense_recview);

        btnAddIncomeCateg = (Button) v.findViewById(R.id.category_add_income_button);
        btnAddIncomeCateg.setOnClickListener(this);
        btnAddExpenseCateg = (Button) v.findViewById(R.id.category_add_expense_button);
        btnAddExpenseCateg.setOnClickListener(this);

        incomeLayout = (RelativeLayout) v.findViewById(R.id.list_category_income_layout);
        expenseLayout = (RelativeLayout) v.findViewById(R.id.list_category_expense_layout);
        clickToShow(incomeLayout);
        clickToShow(expenseLayout);

        CategoriesAdapter incomeAdapter = new CategoriesAdapter(getContext(), incomeCategs);
        listIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        listIncomeCategories.setAdapter(incomeAdapter);

        CategoriesAdapter expenseAdapter = new CategoriesAdapter(getContext(), expenseCategs);
        listExpenseCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        listExpenseCategories.setAdapter(expenseAdapter);

        return v;
    }

    public void clickToShow(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.list_category_income_layout) {
                    if (listIncomeCategories.getVisibility() == View.GONE) {
                        listIncomeCategories.setVisibility(View.VISIBLE);
                    } else {
                        listIncomeCategories.setVisibility(View.GONE);
                    }
                } else {
                    if (listExpenseCategories.getVisibility() == View.GONE) {
                        listExpenseCategories.setVisibility(View.VISIBLE);
                    } else {
                        listExpenseCategories.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * Sends intent to EditActivity
     * bundle params:
     *      String key = IRequestCode
     *      String isExpense = true / false
     */
    @Override
    public void onClick(View v) {

        String isExpense;
        switch (v.getId()) {
            default:
            case R.id.category_add_income_button:
                isExpense = "false";
                break;
            case R.id.category_add_expense_button:
                isExpense = "true";
                break;
        }

        Intent editActivity = new Intent(getContext(), EditActivity.class);
        editActivity.putExtra("key", IRequestCodes.EDIT_CATEGORY);
        editActivity.putExtra("isExpense", isExpense);
        startActivity(editActivity);

    }


    /**
     * itemView.onClickListener sends intent to EditActivity
     * bundle params:
     *      String key = IRequestCode
     *      String title
     */
    //TODO: outer class
    class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesVH> {

        private Context context;

        private ArrayList<Category> categs;

        CategoriesAdapter(Context context, ArrayList<Category> categories) {
            this.context = context;
            this.categs = categories;
        }


        @Override
        public CategoriesAdapter.CategoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
            return new CategoriesAdapter.CategoriesVH(row);

        }

        @Override
        public void onBindViewHolder(final CategoriesVH holder, int position) {
//            TODO Change resources from long to int

            holder.img.setImageResource((int) categs.get(position).getIconResource());
            holder.title.setText(categs.get(position).getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editActivity = new Intent(getContext(), EditActivity.class);
                    editActivity.putExtra("key", IRequestCodes.EDIT_CATEGORY);
                    editActivity.putExtra("title", holder.title.getText().toString());
                    startActivity(editActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categs.size();
        }


        protected class CategoriesVH extends RecyclerView.ViewHolder {

            ImageView img;
            TextView title;


            public CategoriesVH(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.row_category_icon);
                title = (TextView) itemView.findViewById(R.id.row_category_title);


            }
        }

    }

}
