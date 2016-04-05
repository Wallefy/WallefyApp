package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.UserDataSource;

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

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (logInfo()>0)
        {
//            extract in method
            getActivity().finish();
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = (EditText) view.findViewById(R.id.email_login);
        edtPassword = (EditText) view.findViewById(R.id.password_login);
        btnLogin = (Button) view.findViewById(R.id.login_button);
        btnRegister = (Button) view.findViewById(R.id.register_button);
        chbKeepLogged = (CheckBox) view.findViewById(R.id.always_logged);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        userDataSource = new  UserDataSource(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                ((UserDataSource) userDataSource).open();
//                TODO CLOSE CONNECTION
                long userId = userDataSource.loginUser(edtEmail.getText().toString(),edtPassword.getText().toString());
                if (userId<1)
                    Toast.makeText(getContext(), "ERROR LOGGING", Toast.LENGTH_SHORT).show();
                else // save in shared prefs userId
                {
                    if (chbKeepLogged.isChecked())
                        keepLogged(userId);

                    getActivity().finish();
                    startActivity(new Intent(getContext(), MainActivity.class));
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

    private void keepLogged(long savedUserId){
        SharedPreferences isLogged = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = isLogged.edit();
        editor.putLong("userId", savedUserId);
        editor.commit();
    }

    private long logInfo() {
        SharedPreferences log = PreferenceManager.getDefaultSharedPreferences(getContext());
        return log.getLong("userId", 0);
    }
}
