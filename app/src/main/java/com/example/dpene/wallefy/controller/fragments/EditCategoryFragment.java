package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        Log.e("USER", "onCreateView: " + String.valueOf(getArguments().get("categoryIcon")) );
        if (getArguments().get("categoryIcon")!= null) {
            long iconResource = (long) getArguments().get("categoryIcon");

            if (iconResource != 0)
                imgSelectedIcon.setImageResource((int) (iconResource));

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
                String isExpenceForDb = getArguments().getString("categoryType").equals("true")? "1" :"0";
                new SaveCategoryTask(getArguments().get("title") == null).execute(categoryName.getText().toString(),
                        isExpenceForDb,String.valueOf(selectedIcon));

//                String selectedAccountType = spnAccountType.getSelectedItem().toString();
//                String selectedCategory = spnCategoryType.getSelectedItem().toString();
//                String calculatedAmount = amount.getText().toString();
//                if (Double.parseDouble(calculatedAmount) > 0) {
//                    new TaskSaveEntry(user.getUserId()).execute(selectedAccountType, selectedCategory,
//                            calculatedAmount,parent.setNote(), DateFormater.from_dMMMyyyy_To_yyyyMMddHHmmss(parent.setDate()));
//                    getActivity().finish();
//                }
//
//                else{
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage("Amount must be positive");
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
                return true;
            case R.id.clear_values:
                Toast.makeText(getContext(), "DELETE FROM DB", Toast.LENGTH_SHORT).show();
//                ((TextView) getActivity().findViewById(R.id.transaction_amount)).setText("0");
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
            //            TODO update existing Category for current user
            if (isNewCategory) {
                Category cat = categoryDataSource.createCategory(params[0], params[1].equals("1"),
                        Long.valueOf(params[2]), user.getUserId());
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
                Toast.makeText(getContext(), "Category created", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else
                Toast.makeText(getContext(), "Failed to create category", Toast.LENGTH_SHORT).show();
        }
    }
}
