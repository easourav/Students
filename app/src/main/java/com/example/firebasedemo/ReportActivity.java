package com.example.firebasedemo;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {
    TextView totalStudentTv,totalByCngTv, totalByBusTv, totalByCarTv, totalByRikshawTv, totalByUberTv, totalBywalkingTv, totalByOtherTv;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    ArrayList<String> schoolKeyArrayList;
    ArrayList<String> studentNammes;

    ArrayList<String> busArrayList;
    ArrayList<String> carArrayList;
    ArrayList<String> uberArrayList;
    ArrayList<String> cngArrayList;
    ArrayList<String> rikshawaArrayList;
    ArrayList<String> walkingArrayList;
    ArrayList<String> othersArrayList;

    String totalSrudent, totalByBus, totalByCar, totalByUber, totalByCng, totalByRikshawa, totalByWalking, totalByOthers;

    AutoCompleteTextView searchSchoolAC;

    ArrayList<String> schoolNameArrayList;
    ArrayList<String> schoolNameKeyArrayList;
    ArrayList<String> studentcameWayArrayList;

    PieChart pieChart;

    String schoolKey;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        drawerLayout = findViewById(R.id.nav_drawer_maniActivity);
        navigationView = findViewById(R.id.navigationView);
        searchSchoolAC = findViewById(R.id.actvSchoolSelect);
        totalStudentTv = findViewById(R.id.totalStudent);

        totalByBusTv = findViewById(R.id.budTv);
        totalByCarTv = findViewById(R.id.carTv);
        totalByCngTv = findViewById(R.id.cngTv);
        totalByUberTv = findViewById(R.id.uberTv);
        totalByRikshawTv = findViewById(R.id.rikshawaTv);
        totalBywalkingTv = findViewById(R.id.walkingTv);
        totalByOtherTv = findViewById(R.id.othersTv);

        /*pieChart = findViewById(R.id.chart);
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(100f, "jan"));
        value.add(new PieEntry(35f, "Feb"));
        value.add(new PieEntry(55f, "March"));

        PieDataSet pieDataSet = new PieDataSet(value, "Month");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);

        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        Description description = new Description();
        description.setText("");
        description.setTextSize(20f);
        pieChart.setDescription(description);*/


        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setSearchSchoolAutoComplite();
        navItemClicked();

        searchSchoolAC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolKey = schoolNameKeyArrayList.get((int) id);
                getStudentReport();

            }

            private void getStudentReport() {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        schoolKeyArrayList = new ArrayList<>();
                        studentNammes = new ArrayList<>();
                        studentcameWayArrayList = new ArrayList<>();

                        busArrayList = new ArrayList<>();
                        carArrayList = new ArrayList<>();
                        uberArrayList = new ArrayList<>();
                        cngArrayList = new ArrayList<>();
                        rikshawaArrayList = new ArrayList<>();
                        walkingArrayList = new ArrayList<>();
                        othersArrayList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.child("schoolKey").exists()){
                                Student student = dataSnapshot1.getValue(Student.class);

                                assert student != null;
                                schoolKeyArrayList.add(student.getSchoolKey());
                                String keys = dataSnapshot1.child("schoolKey").getValue().toString();

                                if (keys.equals(schoolKey)){
                                    studentNammes.add(student.getStudentName());
                                    studentcameWayArrayList.add(student.getStudentCaneWay());
                                    totalSrudent = String.valueOf(studentNammes.size());
                                    String cameToWay = dataSnapshot1.child("studentCaneWay").getValue().toString();
                                    if (cameToWay.equals("Bus")){
                                        busArrayList.add(student.getStudentCaneWay());
                                        totalByBus = String.valueOf(busArrayList.size());

                                    }
                                    if (cameToWay.equals("Personal car")){
                                        carArrayList.add(student.getStudentCaneWay());
                                        totalByCar = String.valueOf(carArrayList.size());
                                    }
                                    if (cameToWay.equals("Uber/Pathao")){
                                        uberArrayList.add(student.getStudentCaneWay());
                                        totalByUber = String.valueOf(uberArrayList.size());
                                    }
                                    if (cameToWay.equals("CNG")){
                                        cngArrayList.add(student.getStudentCaneWay());
                                        totalByCng = String.valueOf(cngArrayList.size());
                                    }
                                    if (cameToWay.equals("Rikshawa")){
                                        rikshawaArrayList.add(student.getStudentCaneWay());
                                        totalByRikshawa = String.valueOf(rikshawaArrayList.size());

                                    }
                                    if (cameToWay.equals("Walking")){
                                        walkingArrayList.add(student.getStudentCaneWay());
                                        totalByWalking = String.valueOf(walkingArrayList.size());
                                    }
                                    if (cameToWay.equals("Others")){
                                        othersArrayList.add(student.getStudentCaneWay());
                                        totalByOthers = String.valueOf(othersArrayList.size());
                                    }
                                }
                            }
                        }
                        if (totalSrudent == null){
                            totalStudentTv.setText("0");
                        }
                        else {
                            totalStudentTv.setText(totalSrudent);
                        }
                        if (totalByBus == null){
                            totalByBusTv.setText("0");
                        }
                        else {
                            totalByBusTv.setText(totalByBus);
                        }
                        if (totalByCar == null){
                            totalByCarTv.setText("0");
                        }
                        else {
                            totalByCarTv.setText(totalByCar);
                        }

                        if (totalByUber == null){
                            totalByUberTv.setText("0");
                        }
                        else {
                            totalByUberTv.setText(totalByUber);
                        }

                        if (totalByCng == null){
                            totalByCngTv.setText("0");
                        }
                        else {
                            totalByCngTv.setText(totalByCng);
                        }
                        if (totalByRikshawa == null){
                            totalByRikshawTv.setText("0");
                        }
                        else {
                            totalByRikshawTv.setText(totalByRikshawa);
                        }
                        if (totalByWalking == null){
                            totalBywalkingTv.setText("0");
                        }
                        else {
                            totalBywalkingTv.setText(totalByWalking);
                        }
                        if (totalByOthers == null){
                            totalByOtherTv.setText("0");
                        }
                        else {
                            totalByOtherTv.setText(totalByOthers);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ReportActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

    private void setSearchSchoolAutoComplite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schools");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolNameArrayList = new ArrayList<>();
                schoolNameKeyArrayList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    assert student != null;
                    schoolNameArrayList.add(student.getSchoolName());
                    schoolNameKeyArrayList.add(student.getSchoolKey());

                    setSchoolName(schoolNameArrayList);
                }
            }

            private void setSchoolName(ArrayList<String> schoolNameArrayList) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportActivity.this,R.layout.custom_list_item, R.id.text_view_list_item, schoolNameArrayList);
                searchSchoolAC.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navItemClicked() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeMenu :
                        Intent intent = new Intent(ReportActivity.this,MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addStudentMenu :
                        Intent intentAddStudent = new Intent(ReportActivity.this,StudentRegActivity.class);
                        startActivity(intentAddStudent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.viewMapMenu :
                        Intent intentStudentMap = new Intent(ReportActivity.this, MapsActivity.class);
                        intentStudentMap.putExtra("categoryValue", "students");
                        startActivity(intentStudentMap);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.viewReportMenu :
                        Intent intentReport = new Intent(ReportActivity.this, ReportActivity.class);
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
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
