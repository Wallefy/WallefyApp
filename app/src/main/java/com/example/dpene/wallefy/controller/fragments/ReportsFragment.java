package com.example.dpene.wallefy.controller.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dd.ShadowLayout;
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
    private ImageButton btnPickDate;
    private ImageButton btnclearDate;

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

    String selectedAccount;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

        categories = new ArrayList<>();
        accounts = new ArrayList<>();
        expenseIncome = new ArrayList<>();

        expenseIncome.add("All");
        expenseIncome.add("Expense");
        expenseIncome.add("Income");

        userDataSource = UserDataSource.getInstance(getContext());
        categoryDataSource = CategoryDataSource.getInstance(getContext());
        accountDataSource = AccountDataSource.getInstance(getContext());
        historyDataSource = HistoryDataSource.getInstance(getContext());

        final View v = inflater.inflate(R.layout.fragment_reports, container, false);

        edtDate = (EditText) v.findViewById(R.id.reports_date);
        edtDate.setCursorVisible(false);
        edtDate.setKeyListener(null);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(v, getFragmentManager());
            }
        });
        btnPickDate = (ImageButton) v.findViewById(R.id.reports_calendar_btn);
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate.pick(edtDate, getFragmentManager());
            }
        });
        btnclearDate = (ImageButton) v.findViewById(R.id.reports_clear_calendar_btn);
        btnclearDate.setVisibility(View.GONE);
        btnclearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDate.setText(null);
                v.setVisibility(View.GONE);
            }
        });
        edtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnclearDate.setVisibility(View.VISIBLE);
            }
        });

        spnAccounts = (Spinner) v.findViewById(R.id.reports_accounts);
        spnCategories = (Spinner) v.findViewById(R.id.reports_categories);
        spnCategories.setVisibility(View.GONE);
        spnExpenseIncome = (Spinner) v.findViewById(R.id.reports_expense_income);
        spnExpenseIncome.setVisibility(View.VISIBLE);

        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        accountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts);

        accountAdapter.setNotifyOnChange(true);
        categoryAdapter.setNotifyOnChange(true);

        spnAccounts.setAdapter(accountAdapter);
        selectedAccount = "cash";
        reportEntries = (RecyclerView) v.findViewById(R.id.report_recycler);
        final com.dd.ShadowLayout filtersLabel = (ShadowLayout) v.findViewById(R.id.filters_label_shadow);
        final com.dd.ShadowLayout mainFilterWindow = (ShadowLayout) v.findViewById(R.id.reports_shadow_top);
        filtersLabel.setVisibility(View.GONE);
        final Animation moveUp = AnimationUtils.loadAnimation(getContext(), R.anim.move_up);
        moveUp.setDuration(220);
        final Animation moveDown = AnimationUtils.loadAnimation(getContext(), R.anim.move_down);
        moveDown.setDuration(220);
        filtersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFilterWindow.setVisibility(View.VISIBLE);
                mainFilterWindow.startAnimation(moveDown);
                filtersLabel.startAnimation(moveUp);
                filtersLabel.setVisibility(View.GONE);
            }
        });
        reportEntries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.e("SCROLLLOSTENER", "onScrolled: dx: "  + dx );
                Log.e("SCROLLLOSTENER", "onScrolled: VSP: "  + recyclerView.getScrollY() );
                Log.e("SCROLLLOSTENER", "onScrolled: chldCount: "  + recyclerView.getChildCount() );
                if (dy>30 && mainFilterWindow.getVisibility() == View.VISIBLE) {

                    mainFilterWindow.startAnimation(moveUp);
                    mainFilterWindow.setVisibility(View.GONE);
                    filtersLabel.setVisibility(View.VISIBLE);
                    filtersLabel.startAnimation(moveDown);
                }

//                if ( dy < - 100 && mainFilterWindow.getVisibility() == View.GONE) {
//                    mainFilterWindow.setVisibility(View.VISIBLE);
//                    mainFilterWindow.startAnimation(moveDown);
//                    filtersLabel.startAnimation(moveUp);
//                    filtersLabel.setVisibility(View.GONE);
//                }
            }
        });
        entries = new ArrayList<>();

        spnCategories.setAdapter(categoryAdapter);
        new TaskFillSpinners().execute(1);

        spnExpenseIncome.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, expenseIncome));

        radioChooseTypeOfEntry = (RadioButton) v.findViewById(R.id.reports_radio_expense);
        radioChooseTypeOfEntry.setChecked(true);
        radioChooseCategory = (RadioButton) v.findViewById(R.id.reports_radio_category);
        radioGroup = (RadioGroup) v.findViewById(R.id.reports_radio_group);

        radioChooseCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnCategories.setVisibility(View.VISIBLE);
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
                }
            }
        });
        Button btnFilter = (Button) v.findViewById(R.id.reports_btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String accName = spnAccounts.getSelectedItem().toString();
                String typeOfEntry = null;
                if (spnExpenseIncome.getSelectedItem() != null)
                    typeOfEntry = spnExpenseIncome.getSelectedItem().toString();
                String catName = null;
                if (spnCategories.getSelectedItem() != null)
                    catName = spnCategories.getSelectedItem().toString();

                if (spnExpenseIncome.getVisibility() == View.GONE)
                    typeOfEntry = null;
                if (spnCategories.getVisibility()==View.GONE)
                    catName = null;

                new TaskFillFilteredEntries().execute(
                        String.valueOf(user.getUserId()),
                        accName,
                        typeOfEntry,
                        catName,
                        DateFormater.from_dMMMyyyy_To_yyyyMMddHHmmss(edtDate.getText().toString()
                        )
                );
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

//                    String.valueOf(user.getUserId()),
//                    spnAccounts.getSelectedItem().toString(),
//                    spnExpenseIncome.getSelectedItem().toString(),
//                    spnCategories.getSelectedItem().toString(),
//                    DateFormater.from_dMMMyyyy_To_yyyyMMddHHmmss(edtDate.getText().toString()

            String userId = params[0];
            String accName = params[1];
            String typeOfEntry = null;
            if (params[2] != null) {
                switch (params[2]) {
                    case "Expense":
                        typeOfEntry = "1";
                        break;
                    case "Income":
                        typeOfEntry = "0";
                        break;
                    default:
                        typeOfEntry = "all";
                        break;
                }
            }
            String catName = params[3];
            String dateAfter = params[4];

            Log.e("FILTRING", "doInBackground: " + userId + accName + typeOfEntry + catName + dateAfter);

            ((HistoryDataSource) historyDataSource).open();
            entries.clear();
            entries = historyDataSource.filterEntries(userId,accName,typeOfEntry,catName,dateAfter);
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
