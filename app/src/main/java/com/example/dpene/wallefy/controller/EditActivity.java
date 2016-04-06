package com.example.dpene.wallefy.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.EditCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.EditProfileFragment;
import com.example.dpene.wallefy.controller.fragments.comunicators.IRequestCodes;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Fragment editFragment;
        Bundle bundle = new Bundle();

        switch (getIntent().getStringExtra("key")) {
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
                editFragment = new EditCategoryFragment();
                // inflate bundle
                break;
        }

        editFragment.setArguments(bundle);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.root_edit, editFragment, "category");
        trans.commit();

    }
}
