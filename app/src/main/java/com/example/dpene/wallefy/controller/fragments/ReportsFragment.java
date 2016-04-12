package com.example.dpene.wallefy.controller.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;
import com.example.dpene.wallefy.controller.controllerutils.PickDate;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;
import com.example.dpene.wallefy.model.datasources.UserDataSource;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportsFragment extends Fragment {

    private ArrayList<String> categories;
    private ArrayList<String> accounts;
    private ArrayList<String> expenseIncome;

    private EditText edtDate;

    private Spinner spnCategories;
    private Spinner spnAccounts;
    private Spinner spnExpenseIncome;

    IUserDao userDataSource;
    ICategoryDao categoryDataSource;
    IAccountDao accountDataSource;
    IHistoryDao historyDataSource;

    ArrayAdapter categoryAdapter;
    ArrayAdapter accountAdapter;
    ReportEntriesAdapter rea;

    RadioButton radioChooseTypeOfEntry;
    RadioButton radioChooseCategory;
    RadioGroup radioGroup;

    RecyclerView reportEntries;
    ArrayList<History> entries;

    String selectedCategory;
    String selectedAccount;
    String selectedExpense;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

        categories = new ArrayList<>();
        accounts = new ArrayList<>();
        expenseIncome = new ArrayList<>();

        expenseIncome.add("Expense");
        expenseIncome.add("Income");

        userDataSource = UserDataSource.getInstance(getContext());
        categoryDataSource = CategoryDataSource.getInstance(getContext());
        accountDataSource = AccountDataSource.getInstance(getContext());
        historyDataSource = HistoryDataSource.getInstance(getContext());

        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        edtDate = (EditText) v.findViewById(R.id.reports_date);
        edtDate.setCursorVisible(false);
        edtDate.setKeyListener(null);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick((EditText) v, getFragmentManager());
            }
        });

        spnAccounts = (Spinner) v.findViewById(R.id.reports_accounts);
        spnCategories = (Spinner) v.findViewById(R.id.reports_categories);
        spnCategories.setVisibility(View.GONE);
        spnExpenseIncome = (Spinner) v.findViewById(R.id.reports_expense_income);
        spnExpenseIncome.setVisibility(View.GONE);

        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        accountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts);

        accountAdapter.setNotifyOnChange(true);
        categoryAdapter.setNotifyOnChange(true);

        spnAccounts.setAdapter(accountAdapter);
        selectedAccount = "cash";
        reportEntries = (RecyclerView) v.findViewById(R.id.report_recycler);
        spnAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccount = ((TextView) view).getText().toString();

                new TaskFillFilteredEntries().execute(String.valueOf(user.getUserId()), selectedAccount);
                Log.e("SELECTED", selectedAccount);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        entries = new ArrayList<>();


        spnCategories.setAdapter(categoryAdapter);
        new TaskFillSpinners().execute(1);

        spnExpenseIncome.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, expenseIncome));

        radioChooseCategory = (RadioButton) v.findViewById(R.id.reports_radio_category);
        radioChooseTypeOfEntry = (RadioButton) v.findViewById(R.id.reports_radio_expense);
        radioGroup = (RadioGroup) v.findViewById(R.id.reports_radio_group);

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.reports_radio_category){
//                    spnCategories.setVisibility(View.VISIBLE);
//
//                    spnCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            selectedCategory = ((TextView) view).getText().toString();
//                            Log.e("CATEG", selectedCategory + " ...");
//                            Log.e("Exp", selectedExpense + " ...");
//                        }
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//                        }
//                    });
//                    Log.e("ASD", spnCategories.getChildCount() + " id");
////                    selectedCategory = ((TextView) spnCategories.getSelectedItem()).getText().toString();
//                    selectedExpense = null;
//                }
//            }
//        });

        radioChooseCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnCategories.setVisibility(View.VISIBLE);

                    spnCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = ((TextView) view).getText().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    spnExpenseIncome.setVisibility(View.GONE);
                }
            }
        });

        radioChooseTypeOfEntry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnCategories.setVisibility(View.GONE);
                    spnExpenseIncome.setVisibility(View.VISIBLE);

                    spnExpenseIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedExpense = ((TextView) view).getText().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });

        return v;
    }

    class TaskFillSpinners extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Integer... params) {
            ((UserDataSource) userDataSource).open();
            ((CategoryDataSource) categoryDataSource).open();
            ((AccountDataSource) accountDataSource).open();
            ArrayList<Category> cats = categoryDataSource.showAllCategoriesForUser(params[0]);
            ArrayList<Account> accs = accountDataSource.showAllAccounts(params[0]);
            if (cats == null)
                cats = new ArrayList<>();
            for (Category c : cats) {
                categories.add(c.getCategoryName());
            }
            if (accs == null)
                accs = new ArrayList<>();
            for (Account a : accs) {
                accounts.add(a.getAccountName());
            }
            ((UserDataSource) userDataSource).close();
            ((CategoryDataSource) categoryDataSource).close();
            ((AccountDataSource) accountDataSource).close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            accountAdapter.notifyDataSetChanged();
            categoryAdapter.notifyDataSetChanged();
        }
    }

    class TaskFillFilteredEntries extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            ((HistoryDataSource) historyDataSource).open();
            entries = historyDataSource.listHistoryByAccountName(Long.parseLong(params[0]), params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rea = new ReportEntriesAdapter(getContext(), entries, user);
            rea.notifyDataSetChanged();
            reportEntries.setLayoutManager(new LinearLayoutManager(getContext()));
            reportEntries.setAdapter(rea);
        }
    }

}
