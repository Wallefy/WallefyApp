package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;
    CheckBox chbKeepLogged;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        switch(v.getId()){
            case R.id.login_button:
                // logic
                startActivity(new Intent(getContext(), MainActivity.class));
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
}
