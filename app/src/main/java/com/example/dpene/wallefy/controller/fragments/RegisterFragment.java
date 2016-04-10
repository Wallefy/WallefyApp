package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;
import com.example.dpene.wallefy.model.datasources.UserDataSource;
import com.example.dpene.wallefy.model.utils.RegisterHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {


    EditText edtEmail;
    EditText edtName;
    EditText edtPassword;
    EditText edtRetypedPassword;
    Button btnRegister;
    Button btnBackToLogin;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        edtEmail = (EditText) view.findViewById(R.id.edt_register_email);
        edtName = (EditText) view.findViewById(R.id.edt_register_name);
        edtPassword = (EditText) view.findViewById(R.id.edt_register_pass);
        edtRetypedPassword = (EditText) view.findViewById(R.id.edt_register_re_pass);
        btnRegister = (Button) view.findViewById(R.id.btn_register_reg);
        btnBackToLogin = (Button) view.findViewById(R.id.btn_register_login);

        btnRegister.setOnClickListener(this);
        btnBackToLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_reg:
                String userMail = edtEmail.getText().toString();
                String userName = edtName.getText().toString();
                String userPassword = edtPassword.getText().toString();
                String retypedPassword = edtRetypedPassword.getText().toString();

                boolean isCorrect = true;

                if (RegisterHelper.validateEmail(userMail)) {
                    // check if email already exist

//                    else if (user.getUserId() ==0){
//                        Toast.makeText(getContext(), "Email already registered", Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    edtEmail.setError("Invalid email");
                    isCorrect = false;
                }

                if (!RegisterHelper.validateUsername(userName)) {
                    edtName.setError("Name's length must be bigger than 3");
                    isCorrect = false;
                }

                if (RegisterHelper.strongPassword(userPassword)) {
                    if (!userPassword.equals(retypedPassword)) {
                        edtRetypedPassword.setError("The passwords don't match");
                        isCorrect = false;
                    }
                } else {
                    edtPassword.setError("Password must be between 5-10 characters and contain letters AND numbers");
                    isCorrect = false;
                }
                if (isCorrect) {
                    new TaskCreateUserInitialAccountsCategories().execute(userMail,userName, userPassword);
                }
                break;
            case R.id.btn_register_login:
                getFragmentManager().popBackStack();
        }
    }


    private class TaskCreateUserInitialAccountsCategories extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... params) {

            ArrayList<Account> pojoAccounts = new ArrayList<>();

            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource) userDataSource).open();
            user = userDataSource.registerUser(params[0], params[1], RegisterHelper.md5(params[2]));
            ((UserDataSource) userDataSource).open();

            if (user != null) {
                IAccountDao accountDataSource = AccountDataSource.getInstance(getContext());
                ((AccountDataSource) accountDataSource).open();
                pojoAccounts.add(accountDataSource.createAccount(user.getUserId(), "cash"));
                pojoAccounts.add(accountDataSource.createAccount(user.getUserId(), "card"));
                ((AccountDataSource) accountDataSource).close();

                user.setAccounts(pojoAccounts);

                ICategoryDao categoryDataSource = CategoryDataSource.getInstance(getContext());
                ((CategoryDataSource) categoryDataSource).open();
                ArrayList<Category> categories = new ArrayList<>();
                categories.add(categoryDataSource.createCategory("Home", true, R.drawable.house_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Food", true, R.drawable.eating_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Transport", true, R.drawable.car_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Bills", true, R.drawable.bills_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Gifts", true, R.drawable.gift_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Sport", true, R.drawable.fitnes_56, user.getUserId()));

                categories.add(categoryDataSource.createCategory("Deposits", false, R.drawable.deposit_56, user.getUserId()));
                categories.add(categoryDataSource.createCategory("Salary", false, R.drawable.salary_56, user.getUserId()));
                ((CategoryDataSource) categoryDataSource).close();

                user.setCategories(categories);
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("user", user);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getContext(), "Successful registration", Toast.LENGTH_SHORT).show();
            } else {
                edtEmail.setError("This email is already used");
            }
        }
    }
}
