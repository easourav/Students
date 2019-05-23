package com.example.firebasedemo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.nav_drawer_maniActivity);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        /*toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);*/


        navItemClicked();
    }

    private void navItemClicked() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeMenu :
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addStudentMenu :
                        Intent intentAddStudent = new Intent(MainActivity.this,StudentRegActivity.class);
                        startActivity(intentAddStudent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.viewMapMenu :
                        Intent intentStudentMap = new Intent(MainActivity.this, MapsActivity.class);
                        intentStudentMap.putExtra("categoryValue", "students");
                        startActivity(intentStudentMap);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.viewReportMenu :
                        Intent intentReport = new Intent(MainActivity.this, ReportActivity.class);
                        startActivity(intentReport);
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
