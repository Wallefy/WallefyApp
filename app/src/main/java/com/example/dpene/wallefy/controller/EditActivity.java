package com.example.dpene.wallefy.controller;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.DetailsTransactionFragment;
import com.example.dpene.wallefy.controller.fragments.EditAccountFragment;
import com.example.dpene.wallefy.controller.fragments.EditCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.EditProfileFragment;
import com.example.dpene.wallefy.controller.fragments.TransactionFragment;
import com.example.dpene.wallefy.controller.fragments.TransferFragment;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.controller.fragments.interfaces.ITransactionCommunicator;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;

public class EditActivity extends AppCompatActivity implements IToolbar, ITransactionCommunicator {

    RelativeLayout editAc;
    String fragmentCode;
    User user;

//    Communicator variables
    String passedAmount;
    String passedNote;
    String passedDate;

    //     Start   Tabbed test ---- >
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    //        End Tabbed  <----
    Fragment editFragment;
    Bundle bundle;
    Toolbar toolbar;
    History entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Transaction");


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

        user = (User) getIntent().getSerializableExtra("user");
        entry = (History) getIntent().getSerializableExtra("entry");

        bundle = new Bundle();
        fragmentCode = getIntent().getStringExtra("key");


        //     Start   Tabbed test ---- >

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(mViewPager);

//        End Tabbed  <----

        switch (fragmentCode) {
            default:

                /**
                 * required extras:
                 *      String    key         EDIT_CATEGORY
                 *      boolean   isExpense
                 *      String    title
                 */
            case IRequestCodes.EDIT_CATEGORY:
                toolbar.setTitle("Category");
                editFragment = new EditCategoryFragment();
                if (getIntent().getStringExtra("isExpense") != null) {
                    bundle.putString("isExpense", getIntent().getStringExtra("isExpense"));
                }
                if (getIntent().getStringExtra("title") != null) {
                    bundle.putString("title", getIntent().getStringExtra("title"));
                } else {
                    bundle.putString("title", "");
                }
                break;

            /**
             * required extras:
             *      String          key      EDIT_PROFILE
             *      Serializable    user
             */
            case IRequestCodes.EDIT_PROFILE:
                editFragment = new EditProfileFragment();
                bundle.putSerializable("user",  getIntent().getSerializableExtra("user"));
                break;

            /**
             * required extras:
             *      String          key         EDIT_TRANSACTION
             *      String          account
             *      Serializable    user
             *      ---- or
             *      Serializable    entry
             *
             */
            case IRequestCodes.EDIT_TRANSACTION:
                editFragment = new TransactionFragment();
                toolbar.setTitle("Transaction");
                tabLayout.setVisibility(View.VISIBLE);
                // inflate bundle
                if (getIntent().getStringExtra("category") != null) {
                    bundle.putString("category", getIntent().getStringExtra("category"));
                }

                if (getIntent().getStringExtra("account") != null) {
                    bundle.putString("account", getIntent().getStringExtra("account"));
                }

                if (getIntent().getSerializableExtra("user") != null) {
                    bundle.putSerializable("user", getIntent().getSerializableExtra("user"));
                }

                if (getIntent().getSerializableExtra("entry") != null) {
                    bundle.putSerializable("entry", getIntent().getSerializableExtra("entry"));
                }


                break;

            /**
             * required extras:
             *      String    key      EDIT_ACCOUNT
             *      String    title
             *      String    amount
             *      String    date
             */
            case IRequestCodes.EDIT_ACCOUNT:
                editFragment = new EditAccountFragment();
                // inflate bundle
                if (getIntent().getStringExtra("title") != null) {
                    bundle.putString("title", getIntent().getStringExtra("title"));
                } else {
                    bundle.putString("title", "");
                }

                if (getIntent().getStringExtra("amount") != null) {
                    bundle.putString("amount", getIntent().getStringExtra("amount"));
                } else {
                    bundle.putString("amount", "");
                }

                if (getIntent().getStringExtra("date") != null) {
                    bundle.putString("date", getIntent().getStringExtra("date"));
                } else {
                    bundle.putString("date", "");
                }

                break;

            /**
             * required extras:
             *      String          key      EDIT_ACCOUNT
             *      Serializable    user
             */
            case IRequestCodes.TRANSFER:
                editFragment = new TransferFragment();

                if (getIntent().getSerializableExtra("user") != null) {
                    bundle.putSerializable("user", getIntent().getSerializableExtra("user"));
                }

                break;

        }
//         Put it in all fragments not only to transactions?
//        bundle.putSerializable("user",user);
        editFragment.setArguments(bundle);

//        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        trans.replace(R.id.root_edit, editFragment, "category");
//        trans.commit();
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

    @Override
    public void setSubtitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public void notifyFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.root_edit, fragment);
        trans.commit();
    }

    @Override
    public void getAmount(String amount) {
        passedAmount = amount;
        toolbar.setSubtitle(amount);
    }

    @Override
    public String setAmount() {
        return passedAmount;
    }

    @Override
    public void getNote(String note) {
        this.passedNote  = note;
    }

    @Override
    public String setNote() {
        return this.passedNote;
    }

    @Override
    public void getDate(String date) {
        this.passedDate = date;
    }

    @Override
    public String setDate() {
        return this.passedDate;
    }
    //     Start   Tabbed test ---- >

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return editFragment;
            }
            if (position == 1) {
                Fragment fr = new DetailsTransactionFragment();
                fr.setArguments(bundle);
                return fr;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if (fragmentCode.equals(IRequestCodes.EDIT_TRANSACTION))
                return 2;
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            To show the labels or not to show
//            switch (position) {
//                case 0:
//                    return "History";
//                case 1:
//                    return "Stats";
//            }
            return null;
        }
    }

//        End Tabbed  <----
}
