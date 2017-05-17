package com.example.zissu.noche_1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,  View.OnClickListener {

    private Button mSendData , b_login , b_register;
    private SearchFragment fragment = null;
    private android.app.FragmentManager fragmentManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b_login = (Button) findViewById(R.id.bLogin);
        b_login.setOnClickListener((View.OnClickListener) this);
        b_register = (Button) findViewById(R.id.bRegister1);
        b_register.setOnClickListener((View.OnClickListener) this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayView(0);
    }


    @Override
    public void onClick(View v) {
       if ( v == b_login ) {

           Login login_fragment = new Login();
           FragmentManager manager = getSupportFragmentManager();
           manager.beginTransaction().replace(R.id.content_main_layout,
                   login_fragment, login_fragment.getTag()).commit();
       }

           else if ( v == b_register) {
           Register register_fragment = new Register();
           FragmentManager manager1 = getSupportFragmentManager();
           manager1.beginTransaction().replace(R.id.content_main_layout,
                   register_fragment, register_fragment.getTag()).commit();

       }


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

        if (id == R.id.nav_bars) {
            Intent intent = new Intent(this, BarActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_restaurants) {
            RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main_layout ,
                    restaurantsFragment , restaurantsFragment.getTag()).commit();

        } else if (id == R.id.nav_clubs) {
            ClubsFragment clubsFragment = new ClubsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main_layout ,
                    clubsFragment , clubsFragment.getTag()).commit();


        }
        displayView(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClickBar (View view){
        BarsFragment barsFragment = new BarsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main_layout ,
                barsFragment ,barsFragment.getTag() ).commit();
    }
    public void onClickClub (View view){
        ClubsFragment clubsFragment = new ClubsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main_layout ,
                clubsFragment ,clubsFragment.getTag() ).commit();

    }
    public void onClickRes (View view){
        RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main_layout ,
                restaurantsFragment ,restaurantsFragment.getTag() ).commit();

    }




    //serach bar funcation
    private void displayView(int position) {
        fragment = null;
        String fragmentTags = "";
        switch (position) {
            case 0:
                fragment = new SearchFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, fragmentTags).commit();
        }
    }

}
