package com.example.dpene.wallefy.controller.fragments;

import android.content.DialogInterface;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.controller.gesturelistener.OnSwipeGestureListener;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Calculator;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;
import com.example.dpene.wallefy.model.exceptions.NegativeNumberException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements View.OnClickListener {

    private ITransactionCommunicator parent;

    private TextView tvCategoryType;
    private TextView tvAccountType;
    private TextView currency;
    private TextView amount;

    private Spinner spnAccountType;
    private Spinner spnCategoryType;
    private LinearLayout transactionView;

    private User user;

    // vars from detailsFragment
    private String note;
    private String date;

    private List<String> listCategoriest;
    private List<String> listAccounts;
    private Map<Long, String> mapUsersAccounts;

    // calculator's vars
    private TextView sign;

    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;
    private Button btn_five;
    private Button btn_six;
    private Button btn_seven;
    private Button btn_eight;
    private Button btn_nine;
    private Button btn_plus;

    private Button btn_minus;
    private Button btn_multiply;
    private Button btn_divide;
    private Button btn_equals;
    private Button btn_zero;
    private Button btn_del;
    private Button btn_delimiter;

    private String btn_sign;
    private String prevNum;
    private BigDecimal result;

    public TransactionFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = (ITransactionCommunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listCategoriest = new ArrayList<>();
        listAccounts = new ArrayList<>();
        mapUsersAccounts = new HashMap<>();

//        setting custom heading for every fragment
        IToolbar toolbar = (IToolbar) getActivity();
        toolbar.setTitle("Transaction");


        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

//        enable options menu
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        this.user = (User) bundle.getSerializable("user");

        // arguments from detailsFragment
        this.note = getArguments().getString("note");
        this.date = getArguments().getString("date");

        for (Account acc : user.getAccounts()) {
            mapUsersAccounts.put(acc.getAccountTypeId(), acc.getAccountName());
        }

        for (Account ac : user.getAccounts()) {
            listAccounts.add(ac.getAccountName());
        }

        for (Category cat : user.getCategories()) {
            listCategoriest.add(cat.getCategoryName());
        }

        this.initializeCalculatorVariables(v);

        tvCategoryType = (TextView) v.findViewById(R.id.transaction_type_category);
        tvAccountType = (TextView) v.findViewById(R.id.transaction_type_account);
        amount = (TextView) v.findViewById(R.id.transaction_amount);
        currency = (TextView) v.findViewById(R.id.transaction_amount_currency);
        transactionView = (LinearLayout) v.findViewById(R.id.transaction_layout);

        spnAccountType = (Spinner) v.findViewById(R.id.transaction_account);
        spnCategoryType = (Spinner) v.findViewById(R.id.transaction_category);

        final ArrayAdapter categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listCategoriest);
        final ArrayAdapter accountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listAccounts);

        spnAccountType.setAdapter(accountAdapter);
        spnCategoryType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listCategoriest));


        if (getArguments().get("entry") != null) {
            History entry = (History) getArguments().get("entry");
            spnCategoryType.setSelection(categoryAdapter.getPosition(entry.getCategoryName()));
            spnAccountType.setSelection(accountAdapter.getPosition(mapUsersAccounts.get(entry.getAccountTypeId())));

            amount.setText(String.valueOf(String.format("%.2f", entry.getAmount())));
            if(entry.getAmount() != 0) {
                amount.setText(String.valueOf(entry.getAmount()));
            } else {
                amount.setText(String.valueOf((int) entry.getAmount()));
            }
        } else {
            if (getArguments().get("category") != null) {
                spnCategoryType.setSelection(categoryAdapter.getPosition(getArguments().get("category")));
            }

            if (getArguments().get("account") != null) {
                spnAccountType.setSelection(accountAdapter.getPosition(getArguments().get("account")));
            }

            if (getArguments().get("amount") != null) {
                if (!getArguments().get("amount").equals(0)) {
                    amount.setText(String.valueOf((int) getArguments().getDouble("amount")));
                }
            }
        }



        transactionView.setOnTouchListener(new OnSwipeGestureListener(getContext()) {
            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
                // send bundle to detailsFragment
                Bundle bundle = new Bundle();

                bundle.putDouble("amount", Double.parseDouble(amount.getText().toString()));
                bundle.putString("category", spnCategoryType.getSelectedItem().toString());
                bundle.putString("account", spnAccountType.getSelectedItem().toString());
                bundle.putString("note", getArguments().getString("note") != null ? getArguments().getString("note") : "");
                bundle.putString("date", getArguments().getString("date") != null ? getArguments().getString("date") : "");
                bundle.putSerializable("user", user);


                parent.notifyFragment(new DetailsTransactionFragment(), bundle);
            }
        });

        return v;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.save_entry:
                String selectedAccountType = spnAccountType.getSelectedItem().toString();
                String selectedCategory = spnCategoryType.getSelectedItem().toString();
                String calculatedAmount = amount.getText().toString();
                if (Double.parseDouble(calculatedAmount) > 0) {
                    new TaskSaveEntry(user.getUserId()).execute(selectedAccountType, selectedCategory, calculatedAmount);
                    getActivity().finish();
                }

                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Amount must be positive");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            case R.id.clear_values:
                ((TextView) getActivity().findViewById(R.id.transaction_amount)).setText("0");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // on click for calculator's buttons
    @Override
    public void onClick(View v) {
        String btn_text = ((Button) v).getText().toString();
        String input = amount.getText().toString();
        String signField = sign.getText().toString();
        amount.setTextColor(Color.DKGRAY);

        if (amount.getText().toString().equals("\u221E")) {
            amount.setText("0");
        }

        if (btn_text.equals(btn_delimiter.getText().toString())) {
            if (!amount.getText().toString().contains(".") && input.matches("[0-9]*")) {
                amount.append(".");
            }
        }

        if (btn_text.equals(btn_del.getText().toString())) {
            if (input.length() == 1 || !input.matches("[0-9]*")) {
                amount.setText("0");
                return;
            }
            amount.setText(input.substring(0, input.length() - 1));
        }

        if (btn_text.matches("[*|+|/|-]") && !btn_text.equals(btn_equals.getText().toString())) {
            sign.setText(btn_text);
            btn_sign = btn_text;
            prevNum = input;
            if (prevNum.equals("\u221E"))
                prevNum = "0";
        }
        if (btn_text.matches("[0-9]")) {
            if (amount.getText().toString().matches("0") ||
                    !btn_sign.equals("") ||
                    result == null ||
                    result.equals(0) ||
                    input.equals("\u221E") ||
                    !input.matches("[0-9.]*")
                    ) {
                result = new BigDecimal(0);
                prevNum = input;
                if (prevNum.equals("\u221E"))
                    prevNum = "0";
                input = "";
                btn_sign = "";
            }
            amount.setText(input + btn_text);
        }

        if (btn_text.equals(btn_equals.getText().toString())) {
            this.calcNums(input, signField);
        }

    }



    void calcNums(String input, String signField) {
        try {

            result = Calculator.calc(prevNum, input, signField);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativeNumberException("The amount cannot be a negative number");

            }

            DecimalFormat f = new DecimalFormat();
            f.setDecimalSeparatorAlwaysShown(false);
            f.setGroupingUsed(false);
            f.setMaximumFractionDigits(10);

            sign.setText("");
            amount.setText(f.format(result));
            result = null;
        } catch (NegativeNumberException | ArithmeticException e) {
            amount.setTextColor(Color.RED);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            sign.setText("");
            if (prevNum.equals("0") && input.equals("0")) {
                return;
            }
            amount.setText("\u221E"); //∞ infinity
        }
    }

    private void initializeCalculatorVariables(View v) {
        btn_sign = "";
        result = new BigDecimal(0);

        sign = (TextView) v.findViewById(R.id.transaction_sign);

        btn_one = (Button) v.findViewById(R.id.one_calculator);
        btn_two = (Button) v.findViewById(R.id.two_calculator);
        btn_three = (Button) v.findViewById(R.id.three_calculator);
        btn_four = (Button) v.findViewById(R.id.four_calculator);
        btn_five = (Button) v.findViewById(R.id.five_calculator);
        btn_six = (Button) v.findViewById(R.id.six_calculator);
        btn_seven = (Button) v.findViewById(R.id.seven_calculator);
        btn_eight = (Button) v.findViewById(R.id.eight_calculator);
        btn_nine = (Button) v.findViewById(R.id.nine_calculator);
        btn_zero = (Button) v.findViewById(R.id.zero_calculator);

        btn_plus = (Button) v.findViewById(R.id.plus_calculator);
        btn_minus = (Button) v.findViewById(R.id.minus_calculator);
        btn_multiply = (Button) v.findViewById(R.id.multiply_calculator);
        btn_divide = (Button) v.findViewById(R.id.divide_calculator);
        btn_equals = (Button) v.findViewById(R.id.equal_calculator);
        btn_del = (Button) v.findViewById(R.id.delete_calculator);
        btn_delimiter = (Button) v.findViewById(R.id.coma_calculator);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);

        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_equals.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_delimiter.setOnClickListener(this);
    }

    private class TaskSaveEntry extends AsyncTask<String, Void, Boolean> {
        private long userId;

        public TaskSaveEntry(long userId) {
            this.userId = userId;

        }

        @Override
        protected Boolean doInBackground(String... params) {

            IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
            ((AccountDataSource) accountDataSource).open();
            Account acc = accountDataSource.showAccount(userId, params[0]);

            ICategoryDao categoryDataSource = CategoryDataSource.getInstance(getContext());
            ((CategoryDataSource) categoryDataSource).open();

            Category cat = categoryDataSource.showCategory(userId,params[1]);
            IHistoryDao historyDataSource;
            historyDataSource = HistoryDataSource.getInstance(getContext());
            ((HistoryDataSource) historyDataSource).open();
            History h = historyDataSource.createHistory(userId, acc.getAccountTypeId(),
                    cat.getCategoryId(), Double.parseDouble(params[2]), null, null, null, null);

            if (h != null) {
                user.addHistory(h);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(getContext(), "Save success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "SAVE FAILED", Toast.LENGTH_SHORT).show();
        }
    }
}

