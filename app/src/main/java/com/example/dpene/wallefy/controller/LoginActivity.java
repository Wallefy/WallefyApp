package com.example.dpene.wallefy.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.LoginFragment;
import com.example.dpene.wallefy.model.DbHelper;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Fragment login = new LoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.root_login, login, "login");
        trans.commit();

    }
}
