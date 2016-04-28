package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.LoginActivity;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISendMail;
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

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button          btnLogin;
    private Button          btnRegister;
    private TextView        forgottenPass;
    private EditText        edtEmail;
    private EditText        edtPassword;
    private CheckBox        chbKeepLogged;

    private User            user;
    private IUserDao        userDataSource;
    private IHistoryDao     historyDataSource;
    private IAccountDao     accountDataSource;
    private ICategoryDao    categoryDataSource;

    private ISendMail       loginActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (ISendMail) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        userDataSource      = UserDataSource.getInstance(getContext());
        historyDataSource   = HistoryDataSource.getInstance(getContext());
        accountDataSource   = AccountDataSource.getInstance(getContext());
        categoryDataSource  = CategoryDataSource.getInstance(getContext());

        new Thread(new InstantLog()).start();

        final View view       = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin        = (Button) view.findViewById(R.id.login_button);
        edtEmail        = (EditText) view.findViewById(R.id.email_login);
        btnRegister     = (Button) view.findViewById(R.id.register_button);
        edtPassword     = (EditText) view.findViewById(R.id.password_login);
        chbKeepLogged   = (CheckBox) view.findViewById(R.id.always_logged);
        forgottenPass   = (TextView) view.findViewById(R.id.pass_recover);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        forgottenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginActivity.setSenderEmail(edtEmail.getText().toString());
                FragmentManager fm = getFragmentManager();
                PassRecoveryDialog prd = new PassRecoveryDialog();
                prd.show(fm,"pass_recovery_dialog");

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

                new Thread(new LogIn()).start();

                break;
            case R.id.register_button:
                RegisterFragment    register    = new RegisterFragment();
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
        SharedPreferences           isLogged    = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor    editor      = isLogged.edit();
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

    private void openConnections() {

        ((UserDataSource)       userDataSource).open();
        ((HistoryDataSource)    historyDataSource).open();
        ((AccountDataSource)    accountDataSource).open();
        ((CategoryDataSource)   categoryDataSource).open();
    }

    private void parseUserAndStartMainActivity() {

        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra("user", loadUserInfo(accountDataSource.showAllAccounts(user.getUserId()),
                categoryDataSource.showAllCategoriesForUser(user.getUserId()),
                historyDataSource.listAllHistory(user.getUserId())));
        ((UserDataSource) userDataSource).close();
        startActivity(i);
        getActivity().finish();
    }

    private class InstantLog implements Runnable {

        @Override
        public void run() {
            if (logInfo() > 0) {
                openConnections();
                user = userDataSource.selectUserById(logInfo());
                parseUserAndStartMainActivity();
            }
        }
    }

    private class LogIn implements Runnable {

        @Override
        public void run() {
            openConnections();
            user = userDataSource.loginUser(edtEmail.getText().toString(), RegisterHelper.md5(edtPassword.getText().toString()));
            if (user == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (chbKeepLogged.isChecked())
                    keepLogged(user.getUserId());

                parseUserAndStartMainActivity();
            }
        }
    }
}
