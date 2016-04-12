package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;

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

    User user;

    private String oldCategoryName;
    private long oldIconRes;

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
        if (getArguments().getString("categoryType") != null){

            if (getArguments().getString("categoryType").equals("true"))
                toolbar.setSubtitle("Expense");
            else
                toolbar.setSubtitle("Income");
        }

        setHasOptionsMenu(true);

        user = (User) getArguments().getSerializable("user");
        oldIconRes = getArguments().getLong("categoryIcon");

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


        if (getArguments().get("title") != null) {
            categoryName.setText(getArguments().get("title").toString());
        }



        categoryIconsList = (GridView) v.findViewById(R.id.edit_category_icon_gridview);
        categoryIconsList.setNumColumns(4);
        categoryIconsList.setAdapter(new IconAdapter(getContext(), icons));

        if (getArguments().get("categoryIcon")!= null) {
            long iconResource = (long) getArguments().get("categoryIcon");
            Log.e("Category icon", "onCreateView: "+ iconResource );
            if (iconResource != 0) {
                imgSelectedIcon.setImageResource((int) (iconResource));
                Log.e("Category icon if", "onCreateView: " + iconResource);
            }

        }else
            imgSelectedIcon.setImageResource(icons.get(0));

        categoryIconsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                imgSelectedIcon.setImageResource( icons.get(position));
                selectedIcon = icons.get(position);
            }
        });
//      To modify toolbar btns override oncreateoptionsmenu
        setHasOptionsMenu(true);
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (categoryName.length() <= 0)
            menu.removeItem(R.id.clear_values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.save_entry:
//                TODO if it is a new account - if there is initial balance there must be init date
                String isExpenceForDb = null;
                if (getArguments().getString("categoryType") != null)
                     isExpenceForDb = getArguments().getString("categoryType").equals("true")? "1" :"0";
                new SaveCategoryTask(getArguments().get("title") == null).execute(categoryName.getText().toString(),
                        isExpenceForDb,String.valueOf(selectedIcon),oldCategoryName,String.valueOf(oldIconRes));
                return true;
            case R.id.clear_values:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("DELETE Category: " + getArguments().get("title").toString());
                builder.setMessage("All entries for this category would be deleted!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteCategoryTask(user.getUserId()).execute(categoryName.getText().toString());
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private class SaveCategoryTask extends AsyncTask<String,Void,Boolean>{
        private boolean isNewCategory;
        public SaveCategoryTask(boolean b) {
            this.isNewCategory = b;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ICategoryDao categoryDataSource = CategoryDataSource.getInstance(getContext());
            ((CategoryDataSource)categoryDataSource).open();
            //            TODO check existing Category for current user and update userAccountPojo
            if (isNewCategory) {
                Category cat = categoryDataSource.createCategory(params[0], params[1].equals("1"),
                        Long.valueOf(params[2]), user.getUserId());
                if (cat != null) {
                    user.addCategory(cat);
                    return true;
                }
            }
            else {
//                execute(categoryName.getText().toString(),
//                isExpenceForDb,String.valueOf(selectedIcon),oldCategoryName,String.valueOf(oldIconRes));
                Log.e("Category in async", "doInBackground: categoryName  " + params[0] );
                Log.e("Category in async", "doInBackground: selectedIcon  " + params[2] );
                Log.e("Category in async", "doInBackground: selectedIcon  old" +  params[4] );
                Log.e("Category in async", "doInBackground: userId  " + user.getUserId() );
                Log.e("Category in async", "doInBackground: oldCategoryName  " + params[3] );
                String imgRes = params[2];
//                if (imgRes.length() <2)
                Category cat = categoryDataSource.updateCategory(params[0],Long.valueOf(params[2]),user.getUserId(),params[3]);
                if (cat != null) {
                    user.addCategory(cat);
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else
                Toast.makeText(getContext(), "Failed to create category", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteCategoryTask extends AsyncTask<String,Void,Boolean>{
        private long userId;
        public DeleteCategoryTask(long userId) {
            this.userId = userId;

        }

        @Override
        protected Boolean doInBackground(String... params) {
            ICategoryDao categoryDataSource = CategoryDataSource.getInstance(getContext());
            ((CategoryDataSource)categoryDataSource).open();
            if (categoryDataSource.deleteCategory(userId,params[0]))
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(getContext(), "DELETE SUCCESS", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "DELETE FAILED", Toast.LENGTH_SHORT).show();
        }
    }
}
