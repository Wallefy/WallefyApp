package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;

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

    public RegisterFragment() {
        // Required empty public constructor
    }

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

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_register_reg:
                // if successful
                startActivity(new Intent(getContext(), MainActivity.class));
                //else error
                break;
            case R.id.btn_register_login:
                this.onDestroy();
        }
    }
}
