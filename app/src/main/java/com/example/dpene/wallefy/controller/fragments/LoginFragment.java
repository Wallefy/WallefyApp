package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
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
import com.example.dpene.wallefy.model.utils.RegisterHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;
    CheckBox chbKeepLogged;

    IUserDao userDataSource;
    User user;

    IAccountDao accountDataSource;
    ICategoryDao categoryDataSource;
    IHistoryDao historyDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userDataSource = UserDataSource.getInstance(getContext());
        ((UserDataSource) userDataSource).open();


        accountDataSource = AccountDataSource.getInstance(getContext());
        ((AccountDataSource) accountDataSource).open();
        categoryDataSource = CategoryDataSource.getInstance(getContext());
        ((CategoryDataSource) categoryDataSource).open();
        historyDataSource = HistoryDataSource.getInstance(getContext());
        ((HistoryDataSource) historyDataSource).open();

        if (logInfo() > 0) {

            Intent i = new Intent(getContext(), MainActivity.class);

            user = userDataSource.selectUserById(logInfo());

            i.putExtra("user", loadUserInfo(accountDataSource.showAllAccounts(user.getUserId()),
                    categoryDataSource.showAllCategoriesForUser(user.getUserId()),
                    historyDataSource.listAllHistory(user.getUserId())));
            ((UserDataSource) userDataSource).close();
            startActivity(i);
            getActivity().finish();
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = (EditText) view.findViewById(R.id.email_login);
        edtPassword = (EditText) view.findViewById(R.id.password_login);
        btnLogin = (Button) view.findViewById(R.id.login_button);
        btnRegister = (Button) view.findViewById(R.id.register_button);
        chbKeepLogged = (CheckBox) view.findViewById(R.id.always_logged);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

//                TODO CLOSE CONNECTION
                user = userDataSource.loginUser(edtEmail.getText().toString(), RegisterHelper.md5(edtPassword.getText().toString()));
                if (user == null)
                    Toast.makeText(getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                else // save in shared prefs userId
                {
                    if (chbKeepLogged.isChecked())
                        keepLogged(user.getUserId());
                    // create user
                    //parse user to next activity

//                    accountDataSource.createAccount(user.getUserId(), "cash");
//                    accountDataSource.createAccount(user.getUserId(), "card");
//                    accountDataSource.createAccount(user.getUserId(), "deposit");
//
//                    categoryDataSource.createCategory("Food", true, R.drawable.ghost_48, 1);
//                    categoryDataSource.createCategory("Transport", true, R.drawable.ghost_48, 1);
//                    categoryDataSource.createCategory("Photo", true, android.R.drawable.ic_menu_camera, 1);
//
//                    historyDataSource.createHistory(user.getUserId(), 2, 1, 30, null, null, null, null);
//                    historyDataSource.createHistory(user.getUserId(), 2, 1, 12, null, null, null, null);
//                    historyDataSource.createHistory(user.getUserId(), 2, 2, 50, null, null, null, null);
//                    historyDataSource.createHistory(user.getUserId(), 2, 3, 220, "Mnogo snimki", null, null, null);

                    Intent i = new Intent(getContext(), MainActivity.class);
                    i.putExtra("user", loadUserInfo(accountDataSource.showAllAccounts(user.getUserId()),
                            categoryDataSource.showAllCategoriesForUser(user.getUserId()),
                            historyDataSource.listAllHistory(user.getUserId())));
                    ((UserDataSource) userDataSource).close();
                    startActivity(i);
                    getActivity().finish();
                }
                break;
            case R.id.register_button:
                RegisterFragment register = new RegisterFragment();
                Bundle bundle = new Bundle();
                // bundle with username
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_login, register, "register");

                transaction.addToBackStack(null);
                transaction.commit();

                break;
            default:
                break;
        }
    }

    private void keepLogged(long savedUserId) {
        SharedPreferences isLogged = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = isLogged.edit();
        editor.putLong("userId", savedUserId);
        editor.commit();
    }

    private long logInfo() {
        SharedPreferences log = PreferenceManager.getDefaultSharedPreferences(getContext());
        return log.getLong("userId", 0);
    }

    private User loadUserInfo(ArrayList<Account> acc, ArrayList<Category> cat, ArrayList<History> h) {

        user.setAccounts(acc);
        user.setCategories(cat);
        user.setHistoryLog(h);
        return user;
    }
}
