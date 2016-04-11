package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.EditActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListCategoryFragment extends Fragment implements View.OnClickListener {

    private RecyclerView listIncomeCategories;
    private RecyclerView listExpenseCategories;
    private ImageButton btnAddIncomeCateg;
    private ImageButton btnAddExpenseCateg;
    private RelativeLayout incomeLayout;
    private RelativeLayout expenseLayout;

    private ArrayList<Category> incomeCategs;
    private ArrayList<Category> expenseCategs;

    User user;
    CategoriesAdapter incomeAdapter;
    CategoriesAdapter expenseAdapter;

    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        incomeCategs = new ArrayList<>();
        expenseCategs = new ArrayList<>();

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

//        incomeCategs.add(new Category(1, "Food", false, R.drawable.ghost, 1));
//        incomeCategs.add(new Category(2, "NeFood", false, R.drawable.ghost, 1));
//        incomeCategs.add(new Category(3, "NeneFood", false, R.drawable.ghost, 1));
//
//        expenseCategs.add(new Category(4, "Food", true, R.drawable.calendar, 1));
//        expenseCategs.add(new Category(5, "NeFood", true, R.drawable.calendar, 1));
//        expenseCategs.add(new Category(6, "NeneFood", true, R.drawable.calendar, 1));


        View v = inflater.inflate(R.layout.fragment_list_category, container, false);

        listIncomeCategories = (RecyclerView) v.findViewById(R.id.category_income_recview);
        listExpenseCategories = (RecyclerView) v.findViewById(R.id.category_expense_recview);

        btnAddIncomeCateg = (ImageButton) v.findViewById(R.id.category_add_income_button);
        btnAddIncomeCateg.setOnClickListener(this);
        btnAddExpenseCateg = (ImageButton) v.findViewById(R.id.category_add_expense_button);
        btnAddExpenseCateg.setOnClickListener(this);

        incomeLayout = (RelativeLayout) v.findViewById(R.id.list_category_income_layout);
        expenseLayout = (RelativeLayout) v.findViewById(R.id.list_category_expense_layout);
        incomeLayout.setOnClickListener(this);
        expenseLayout.setOnClickListener(this);

        new FillCategoriesTask().execute(user.getUserId());



        return v;
    }

    private class FillCategoriesTask extends AsyncTask<Long,Void,Void>{

        @Override
        protected Void doInBackground(Long... params) {
            incomeCategs.clear();
            expenseCategs.clear();
            ICategoryDao categoryDataSource = CategoryDataSource.getInstance(getContext());
            ((CategoryDataSource)categoryDataSource).open();
            incomeCategs = categoryDataSource.showCategoriesByType(params[0],false);
            expenseCategs = categoryDataSource.showCategoriesByType(params[0],true);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            incomeAdapter = new CategoriesAdapter(getContext(), incomeCategs);
//            listIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));

            LinearLayoutManager linLayoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//            listIncomeCategories.setLayoutManager(new GridLayoutManager(getContext(),2));
            listIncomeCategories.setLayoutManager(linLayoutManager);
            listIncomeCategories.setAdapter(incomeAdapter);

            expenseAdapter = new CategoriesAdapter(getContext(), expenseCategs);
//            listExpenseCategories.setLayoutManager(new LinearLayoutManager(getContext()));
            listExpenseCategories.setLayoutManager(new GridLayoutManager(getContext(),2));
            listExpenseCategories.setAdapter(expenseAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (incomeAdapter != null) {
            new FillCategoriesTask().execute(user.getUserId());
            Log.e("TAG", "onResume: " );
        }
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
            case R.id.list_category_income_layout:
                isExpense = "false";
                break;
            case R.id.category_add_expense_button:
            case R.id.list_category_expense_layout:
                isExpense = "true";
                break;
        }

        Intent editActivity = new Intent(getContext(), EditActivity.class);
        editActivity.putExtra("key", IRequestCodes.EDIT_CATEGORY);
        editActivity.putExtra("editCategisExpense", isExpense);
        editActivity.putExtra("user", user);
        startActivity(editActivity);

    }


    /**
     * itemView.onClickListener sends intent to EditActivity
     * extras:
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
        public void onBindViewHolder(final CategoriesVH holder, final int position) {
//            TODO Change resources from long to int

            holder.img.setImageResource((int) categs.get(position).getIconResource());
            holder.title.setText(categs.get(position).getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "onClick: " + ((View) v.getParent()).getTag());
                    Intent editActivity = new Intent(getContext(), EditActivity.class);
                    editActivity.putExtra("key", IRequestCodes.EDIT_CATEGORY);
                    editActivity.putExtra("title", holder.title.getText().toString());
                    editActivity.putExtra("categoryIcon",categs.get(position).getIconResource());
                    editActivity.putExtra("categoryType",((View)v.getParent()).getTag().toString());
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
