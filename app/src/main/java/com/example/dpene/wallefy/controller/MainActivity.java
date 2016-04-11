package com.example.dpene.wallefy.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.EditAccountFragment;
import com.example.dpene.wallefy.controller.fragments.EditProfileFragment;
import com.example.dpene.wallefy.controller.fragments.ExportFragment;
import com.example.dpene.wallefy.controller.fragments.ListAccountsFragment;
import com.example.dpene.wallefy.controller.fragments.ListCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.MainInfoFragment;
import com.example.dpene.wallefy.controller.fragments.PieChartFragment;
import com.example.dpene.wallefy.controller.fragments.ReportsFragment;
import com.example.dpene.wallefy.controller.fragments.TransactionFragment;
import com.example.dpene.wallefy.controller.fragments.interfaces.IPieChartCommunicator;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISaveSpinnerPosition;
import com.example.dpene.wallefy.model.classes.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISaveSpinnerPosition, IPieChartCommunicator {

    User user;
    int spinnerPosition;

    //     Start   Tabbed test ---- >
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    //        End Tabbed  <----

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//      Get user email and username
        View headView = navigationView.getHeaderView(0);
        ((TextView) headView.findViewById(R.id.nav_header_email)).setText(user.getEmail());
        ((TextView) headView.findViewById(R.id.nav_header_username)).setText(user.getUsername());

        //     Start   Tabbed test ---- >

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        End Tabbed  <----

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (mViewPager.getVisibility() == View.VISIBLE)
            finish();
        else {
            toolbar.setSubtitle(null);
            mViewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_transfer) {
            Intent editActivity = new Intent(this, EditActivity.class);
            editActivity.putExtra("key", IRequestCodes.TRANSFER);
            editActivity.putExtra("user", user);
            startActivity(editActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mViewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        if (id == R.id.nav_stats) {
            replaceFrag(new MainInfoFragment());
            mViewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            toolbar.setSubtitle(null);

        } else if (id == R.id.nav_reports) {
            replaceFrag(new ReportsFragment());
            toolbar.setSubtitle("Reports");
        } else if (id == R.id.nav_accounts) {
            replaceFrag(new ListAccountsFragment());
            toolbar.setSubtitle("Accounts");
        } else if (id == R.id.nav_categories) {
            replaceFrag(new ListCategoryFragment());
            toolbar.setSubtitle("Categories");

        } else if (id == R.id.nav_settings) {

            mViewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            Intent editActivity = new Intent(this, EditActivity.class);
            editActivity.putExtra("key", IRequestCodes.EDIT_PROFILE);
            editActivity.putExtra("user", user);
            startActivity(editActivity);

        } else if (id == R.id.nav_export) {
            replaceFrag(new ExportFragment());
            toolbar.setSubtitle("Export");

        } else if (id == R.id.nav_logout) {
            //            Clear shared pref file
            SharedPreferences log = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            //     commit writes its data to persistent storage immediately, whereas apply will handle it in the background
            log.edit().clear().apply();
            SharedPreferences userId = getPreferences(MODE_PRIVATE);
            userId.edit().clear().apply();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFrag(Fragment fr) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_left);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fr.setArguments(bundle);
        ft.replace(R.id.root_main, fr);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void setPosition(int position) {
        this.spinnerPosition = position;
    }

    @Override
    public int getPosition() {
        return spinnerPosition;
    }

    public void notifyFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.root_main, fragment);
        trans.commit();
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
                Fragment fr = new MainInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                fr.setArguments(bundle);
                return fr;
            }
            if (position == 1)
                return new PieChartFragment();
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
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
