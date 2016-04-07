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
import com.example.dpene.wallefy.model.utils.RegisterHelper;

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

        userDataSource = UserDataSource.getInstance(getContext());

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
                ((UserDataSource) userDataSource).open();

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

                Log.e("tag", isCorrect + "");

                if (!RegisterHelper.validateUsername(userName)) {
                    edtName.setError("Name's length must be bigger than 3");
                    isCorrect = false;
                }

                Log.e("tag", isCorrect + "");

                if (RegisterHelper.strongPassword(userPassword)) {
                    if (!userPassword.equals(retypedPassword)) {
                        edtRetypedPassword.setError("The passwords don't match");
                        isCorrect = false;
                    }
                } else {
                    edtPassword.setError("Password must be between 5-10 characters and contain letters AND numbers");
                    isCorrect = false;
                }


                Log.e("tag", isCorrect + "");

                //TODO
                if (isCorrect) {
                    user = userDataSource.registerUser(userMail, userName, RegisterHelper.md5(userPassword));

                    Log.e("tag", user + "");
                    if (user != null) {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.putExtra("user", user);
                        ((UserDataSource) userDataSource).close();
                        startActivity(i);
                        getActivity().finish();
                        Toast.makeText(getContext(), "Successful registration", Toast.LENGTH_SHORT).show();
                    } else {
                        edtEmail.setError("This email is already used");
                    }
                }
                break;
            case R.id.btn_register_login:
                this.onDestroy();

        }
    }

}
