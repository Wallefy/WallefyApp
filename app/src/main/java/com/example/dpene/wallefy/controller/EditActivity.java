package com.example.dpene.wallefy.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.EditAccountFragment;
import com.example.dpene.wallefy.controller.fragments.EditCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.EditProfileFragment;
import com.example.dpene.wallefy.controller.fragments.TransactionFragment;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.User;

public class EditActivity extends AppCompatActivity implements IToolbar {

    String fragmentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
//            Hide initial title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        User user = (User) getIntent().getSerializableExtra("user");

        Fragment editFragment;
        Bundle bundle = new Bundle();

        fragmentCode = getIntent().getStringExtra("key");

        switch (fragmentCode) {
            default:
            case IRequestCodes.EDIT_CATEGORY:
                editFragment = new EditCategoryFragment();
                if (getIntent().getStringExtra("isExpense") != null){
                    bundle.putString("isExpense", getIntent().getStringExtra("isExpense"));
                }
                if(getIntent().getStringExtra("title") != null ){
                    bundle.putString("title", getIntent().getStringExtra("title"));
                } else {
                    bundle.putString("title", "");
                }
                break;

            case IRequestCodes.EDIT_PROFILE:
                editFragment = new EditProfileFragment();
                // inflate bundle
                break;

            case IRequestCodes.EDIT_TRANSACTION:
                editFragment = new TransactionFragment();

                // inflate bundle
                if (getIntent().getStringExtra("category") != null){
                    bundle.putString("category", getIntent().getStringExtra("category"));
                }

                if (getIntent().getStringExtra("account") != null){
                    bundle.putString("account", getIntent().getStringExtra("account"));
                }

                if (getIntent().getSerializableExtra("user") != null){
                    bundle.putSerializable("user", getIntent().getSerializableExtra("user"));
                }

                break;

            case IRequestCodes.EDIT_ACCOUNT:
                editFragment = new EditAccountFragment();
                // inflate bundle
                if(getIntent().getStringExtra("title") != null ){
                    bundle.putString("title", getIntent().getStringExtra("title"));
                } else {
                    bundle.putString("title", "");
                }

                if(getIntent().getStringExtra("amount") != null ){
                    bundle.putString("amount", getIntent().getStringExtra("amount"));
                } else {
                    bundle.putString("amount", "");
                }

                if(getIntent().getStringExtra("date") != null ){
                    bundle.putString("date", getIntent().getStringExtra("date"));
                } else {
                    bundle.putString("date", "");
                }

                break;
        }
//         Put it in all fragments not only to transactions?
//        bundle.putSerializable("user",user);
        editFragment.setArguments(bundle);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.root_edit, editFragment, "category");
        trans.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public void setTitle(String title) {
        ((TextView)findViewById(R.id.activity_edit_title)).setText(title);
    }
}
