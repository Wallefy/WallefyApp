package com.example.dpene.wallefy.controller.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Calculator;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.exceptions.NegativeNumberException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements View.OnClickListener {


    private TextView tvCategoryType;
    private TextView tvAccountType;

    private TextView currency;

    private Spinner spnAccountType;
    private Spinner spnCategoryType;

    private List<String> listCategoriest;
    private List<String> listAccounts;

    // calculator's vars
    private TextView amount;
    TextView sign;

    Button btn_one;
    Button btn_two;
    Button btn_three;
    Button btn_four;
    Button btn_five;
    Button btn_six;
    Button btn_seven;
    Button btn_eight;
    Button btn_nine;
    Button btn_plus;

    Button btn_minus;
    Button btn_multiply;
    Button btn_divide;
    Button btn_equals;
    Button btn_zero;
    Button btn_del;
    Button btn_delimiter;

    String btn_sign;
    String prevNum;
    BigDecimal result;
    BigDecimal mem;

    User user;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listCategoriest = new ArrayList<>();
        listAccounts = new ArrayList<>();
//
//        listCategoriest.add("Food");
//        listCategoriest.add("Drink");
//        listCategoriest.add("Clothes");
//        listCategoriest.add("Things");

//        listAccounts.add("Cash");
//        listAccounts.add("Card");

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");

        for (Account ac : user.getAccounts()) {
            listAccounts.add(ac.getAccountName());

        }

        for (Category cat: user.getCategories()) {
            listCategoriest.add(cat.getCategoryName());

        }

        this.initializeCalculatorVariables(v);

        tvCategoryType = (TextView) v.findViewById(R.id.transaction_type_category);
        tvAccountType = (TextView) v.findViewById(R.id.transaction_type_account);
        amount = (TextView) v.findViewById(R.id.transaction_amount);
        currency = (TextView) v.findViewById(R.id.transaction_amount_currency);

        spnAccountType = (Spinner) v.findViewById(R.id.transaction_account);
        spnCategoryType = (Spinner) v.findViewById(R.id.transaction_category);

        spnAccountType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listAccounts));
        spnCategoryType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listCategoriest));

        return v;
    }

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
            if (!amount.getText().toString().contains(".") && input.matches("[0-9]")) {
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
        mem = new BigDecimal(0);

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
}
