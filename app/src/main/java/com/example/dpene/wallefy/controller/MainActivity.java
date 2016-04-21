package com.example.dpene.wallefy.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.ListAccountsFragment;
import com.example.dpene.wallefy.controller.fragments.ListCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.MainInfoFragment;
import com.example.dpene.wallefy.controller.fragments.PieChartFragment;
import com.example.dpene.wallefy.controller.fragments.ReportsFragment;
import com.example.dpene.wallefy.controller.fragments.ViewPagerFragment;
import com.example.dpene.wallefy.controller.fragments.interfaces.IPieChartCommunicator;
import com.example.dpene.wallefy.controller.fragments.interfaces.IRequestCodes;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISaveSpinnerPosition;
import com.example.dpene.wallefy.model.classes.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISaveSpinnerPosition, IPieChartCommunicator {

    public static User user;
    int spinnerPosition;



    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ---- To make status bar brow on devices with android above Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#8F542C"));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//      Get user email and username
        View headView = navigationView.getHeaderView(0);
        ((TextView) headView.findViewById(R.id.nav_header_email)).setText(MainActivity.user.getEmail());
        ((TextView) headView.findViewById(R.id.nav_header_username)).setText(user.getUsername());

        replaceFrag(new ViewPagerFragment());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        if (id == R.id.nav_main_board) {
            replaceFrag(new ViewPagerFragment());
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

        } else if (id == R.id.nav_edit_profile) {

            Intent editActivity = new Intent(this, EditActivity.class);
            editActivity.putExtra("key", IRequestCodes.EDIT_PROFILE);
            startActivity(editActivity);

        }
//        else if (id == R.id.nav_export) {
//            replaceFrag(new ExportFragment());
//            toolbar.setSubtitle("Export");
//
//        }
        else if (id == R.id.nav_logout) {
            //            Clear shared pref file
            SharedPreferences log = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            //     commit writes its data to persistent storage immediately, whereas apply will handle it in the background
            log.edit().clear().apply();
            SharedPreferences userId = getPreferences(MODE_PRIVATE);
            userId.edit().clear().apply();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//            Clears all and loads only new
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_left);
        Bundle bundle = new Bundle();
        ft.replace(R.id.root_main, fr);
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

    @Override
    protected void onResume() {
        super.onResume();
        user = (User) getIntent().getSerializableExtra("user");
    }

}
