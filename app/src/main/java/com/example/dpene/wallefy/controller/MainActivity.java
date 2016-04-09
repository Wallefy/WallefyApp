package com.example.dpene.wallefy.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.EditAccountFragment;
import com.example.dpene.wallefy.controller.fragments.EditProfileFragment;
import com.example.dpene.wallefy.controller.fragments.ListAccountsFragment;
import com.example.dpene.wallefy.controller.fragments.ListCategoryFragment;
import com.example.dpene.wallefy.controller.fragments.MainInfoFragment;
import com.example.dpene.wallefy.controller.fragments.ReportsFragment;
import com.example.dpene.wallefy.controller.fragments.TransactionFragment;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISaveSpinnerPosition;
import com.example.dpene.wallefy.model.classes.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISaveSpinnerPosition {

    User user;
    int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("ACTMain", "On create");

        user = (User) getIntent().getSerializableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fr = new MainInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.root_main, fr, "mainFr");
        ft.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFrag(new TransactionFragment());

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stats) {
            replaceFrag(new MainInfoFragment());

        } else if (id == R.id.nav_reports) {
            replaceFrag(new ReportsFragment());
        }else if (id == R.id.nav_accounts) {
            replaceFrag(new ListAccountsFragment());
        }else if (id == R.id.nav_categories) {
            replaceFrag(new ListCategoryFragment());

        } else if (id == R.id.nav_settings) {
            replaceFrag(new EditProfileFragment());

        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_logout) {
            //            Clear shared pref file
            SharedPreferences log = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            //     commit writes its data to persistent storage immediately, whereas apply will handle it in the background
            log.edit().clear().apply();
            SharedPreferences userId = getPreferences(MODE_PRIVATE);
            userId.edit().clear().apply();
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFrag(Fragment fr){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_left);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ACTMain", "On restart");
    }
}
