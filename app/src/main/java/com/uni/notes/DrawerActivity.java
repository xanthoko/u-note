package com.uni.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

//περιεχει την αριστερη στηλη που επεκτεινεται.
public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //δημιουργει αρχικο fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.draw_rel , new StartFrag() , new StartFrag().getTag()).commit();
        }

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(id == R.id.action_about){
            Intent intent = new Intent(this , InfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager =getSupportFragmentManager();

        Profile profile = new Profile("","","","","");

        if (id == R.id.nav_start) {
            StartFrag startFrag = new StartFrag();
            manager.beginTransaction().replace(R.id.draw_rel , startFrag , startFrag.getTag()).commit();
        } else if (id == R.id.nav_profile) {
            ProfileFrag profileFrag = new ProfileFrag();
            manager.beginTransaction().replace(R.id.draw_rel , profileFrag , profileFrag.getTag()).commit();
        } else if (id == R.id.nav_subs) {
            WeekFragment blankFragment = new WeekFragment();
            manager.beginTransaction().replace(R.id.draw_rel , blankFragment).commit();
        } else if (id == R.id.nav_calendar) {
            CalendarFrag calendarFrag = new CalendarFrag();
            manager.beginTransaction().replace(R.id.draw_rel , calendarFrag , calendarFrag.getTag()).commit();
        } else if (id == R.id.nav_edit_pr) {
            OneFragment oneFragment = new OneFragment();
            manager.beginTransaction().replace(R.id.draw_rel , oneFragment , oneFragment.getTag()).commit();
        } else if (id == R.id.nav_edit_subs) {
            TwoFragment twoFragment = new TwoFragment();
            manager.beginTransaction().replace(R.id.draw_rel , twoFragment , twoFragment.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
