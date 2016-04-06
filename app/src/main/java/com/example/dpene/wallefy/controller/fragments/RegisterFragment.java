package com.example.dpene.wallefy.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.UserDataSource;

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

    IUserDao userDataSource;
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

        userDataSource = new UserDataSource(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_register_reg:
                String userMail = edtEmail.getText().toString();
                String userName = edtName.getText().toString();
                String userPassword = edtPassword.getText().toString();
                ((UserDataSource) userDataSource).open();
                if (validateEmail(userMail) && validateUsername(userName) && validatePassword(userPassword)) {
                    user = userDataSource.registerUser(userMail, userName, userPassword);
                    if (user.getUserId() > 0) {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.putExtra("user",user);
                        ((UserDataSource) userDataSource).close();
                        startActivity(i);
                        getActivity().finish();
                    }
//                    else if (user.getUserId() ==0){
//                        Toast.makeText(getContext(), "Email already registered", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                        Toast.makeText(getContext(), "Could not register", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Could not register", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register_login:
                this.onDestroy();
        }
    }

    public boolean validateEmail(String email) {
        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email != null && email.matches(pattern)){
            return true;
        }
        edtEmail.setError("invalid email");
        return false;
    }
    public boolean validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z]).{5,10}";
        if (password != null && password.matches(pattern)){
            return true;
        }
        edtPassword.setError("password should be more than 5 characters and include a number");
        return false;
    }
    public boolean validateUsername(String username) {
        String pattern = "(?=.*[a-z]).{5,10}";
        if (username != null && username.matches(pattern)){
            return true;
        }
        edtName.setError("username should me more than 5 chars");
        return false;
    }
}
