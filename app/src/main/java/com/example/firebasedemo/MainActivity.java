package com.example.firebasedemo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


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
                        Toast.makeText(MainActivity.this, "report", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
    }

    public void btnBackPress(View view) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
