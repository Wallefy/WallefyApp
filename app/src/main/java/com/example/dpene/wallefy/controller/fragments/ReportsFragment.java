package com.example.dpene.wallefy.controller.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.controllerutils.DateFormater;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;
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

    ArrayAdapter categoryAdapter;
    ArrayAdapter accountAdapter;

    RadioButton radioChooseTypeOfEntry;
    RadioButton radioChooseCategory;

    RecyclerView reportEntries;
    ArrayList<History> entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        categories = new ArrayList<>();
        accounts = new ArrayList<>();
        expenseIncome = new ArrayList<>();

        expenseIncome.add("Expense");
        expenseIncome.add("Income");

        userDataSource = UserDataSource.getInstance(getContext());
        categoryDataSource = CategoryDataSource.getInstance(getContext());
        accountDataSource = AccountDataSource.getInstance(getContext());

        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        edtDate = (EditText) v.findViewById(R.id.reports_date);
        edtDate.setCursorVisible(false);
        edtDate.setKeyListener(null);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDate((EditText) v);
            }
        });

        spnAccounts = (Spinner) v.findViewById(R.id.reports_accounts);
        spnCategories = (Spinner) v.findViewById(R.id.reports_categories);
        spnCategories.setVisibility(View.GONE);
        spnExpenseIncome = (Spinner) v.findViewById(R.id.reports_expense_income);
        spnExpenseIncome.setVisibility(View.GONE);

        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        accountAdapter  = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts);

        categoryAdapter.setNotifyOnChange(true);
        accountAdapter.setNotifyOnChange(true);

        spnCategories.setAdapter(categoryAdapter);
        spnAccounts.setAdapter(accountAdapter);
        new TaskFillSpinners().execute(1);

        spnExpenseIncome.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, expenseIncome));

        radioChooseCategory = (RadioButton) v.findViewById(R.id.reports_radio_category);
        radioChooseTypeOfEntry = (RadioButton) v.findViewById(R.id.reports_radio_expense);

        radioChooseCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    spnCategories.setVisibility(View.VISIBLE);
                    spnExpenseIncome.setVisibility(View.GONE);
                }
            }
        });

        radioChooseTypeOfEntry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    spnCategories.setVisibility(View.GONE);
                    spnExpenseIncome.setVisibility(View.VISIBLE);
                }
            }
        });
        entries = new ArrayList<>();
        reportEntries = (RecyclerView) v.findViewById(R.id.report_recycler);
        ReportEntriesAdapter rea = new ReportEntriesAdapter(getContext(),entries);
        reportEntries.setLayoutManager(new LinearLayoutManager(getContext()));
        reportEntries.setAdapter(rea);

        return v;
    }

    private void picDate(final EditText edt) {
        class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                String date = year + "-" + ((monthOfYear < 10) ? "0" : "") + monthOfYear + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                edt.setText(DateFormater.from_yyyyMMdd_To_dMMMyyyy(date));
            }
        }
            DialogFragment dateFragment = new FragmentDatePicker();
            dateFragment.show(getFragmentManager(), "datePicker");
    }

    class TaskFillSpinners extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            ((UserDataSource)userDataSource).open();
            ((CategoryDataSource)categoryDataSource).open();
            ((AccountDataSource)accountDataSource).open();
            ArrayList<Category> cats = categoryDataSource.showAllCategoriesForUser(params[0]);
            ArrayList<Account> accs = accountDataSource.showAllAccounts(params[0]);
            for (Category c: cats) {
                categories.add(c.getCategoryName());
            }
            for (Account a: accs) {
                accounts.add(a.getAccountName());
            }
            ((UserDataSource)userDataSource).close();
            ((CategoryDataSource)categoryDataSource).close();
            ((AccountDataSource)accountDataSource).close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            accountAdapter.notifyDataSetChanged();
            categoryAdapter.notifyDataSetChanged();
        }
    }

    class TaskFillFilteredEntries extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }

}
