package com.example.firebasedemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button setBtn;
    AutoCompleteTextView searchSchoolAC;
    ImageView addressMarkerIv, closeIv;

    DatabaseReference databaseReference;

    ArrayList<String> schoolNameArrayList;
    ArrayList<String> schoolNameKeyArrayList;
    ArrayList<String> schoolLatAL;
    ArrayList<String> schoolLngAL;
    ArrayList<String> studentsLat;
    ArrayList<String> studentsLng;
    ArrayList<String> studentSchoolKey;
    ArrayList<String> aSchoolKey;
    ArrayList<String> studentNammes;
    Integer total;

    String schoolName, schoolKey, schoolLat, schoolLng;
    String studentName, studentClass, studentRoll,studentSchoolName, schoolKeys;

    String doctorCategoryId;
    double studentLat, studentLng;

    LatLng latLngSchool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchSchoolAC = findViewById(R.id.actvSchoolSelect);
        addressMarkerIv = findViewById(R.id.addressMarkerIv);
        closeIv = findViewById(R.id.ivClose);
        setBtn = findViewById(R.id.btnSet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("schools");

        studentName = getIntent().getStringExtra("studentName");
        studentClass = getIntent().getStringExtra("studentClass");
        studentSchoolName = getIntent().getStringExtra("schoolName");
        studentRoll = getIntent().getStringExtra("sRoll");
        schoolKeys = getIntent().getStringExtra("schoolKey");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            doctorCategoryId = bundle.getString("categoryValue");


            if (doctorCategoryId.equals("students")) {
                setSearchSchoolAutoComplite();

                searchSchoolAC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        schoolName = schoolNameArrayList.get(position);
                        schoolKey = schoolNameKeyArrayList.get(position);
                        schoolLat = schoolLatAL.get(position);
                        schoolLng = schoolLngAL.get(position);

                        getStudentSchoolKey();


                        assert schoolLat != null;
                        assert schoolLng != null;
                        double lat = Double.valueOf(schoolLat);
                        double lng = Double.valueOf(schoolLng);

                        latLngSchool = new LatLng(lat, lng);

                        mMap.addMarker(new MarkerOptions().position(latLngSchool).icon(BitmapDescriptorFactory.fromBitmap(CreateCustomMarker(MapsActivity.this))).title(schoolName));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngSchool, 14));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngSchool));

                    }

                    private void getStudentSchoolKey() {
                        studentsLat = new ArrayList<>();
                        studentsLng = new ArrayList<>();

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                studentSchoolKey = new ArrayList<>();
                                studentNammes = new ArrayList<>();
                                aSchoolKey = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("schoolKey").exists()){
                                        Student student = dataSnapshot1.getValue(Student.class);

                                        assert student != null;
                                        studentSchoolKey.add(student.getSchoolKey());
                                        String keys = dataSnapshot1.child("schoolKey").getValue().toString();
                                        if (keys.equals(schoolKey)){
                                            studentsLat.add(student.getStudentLat());
                                            studentsLng.add(student.getStudentLng());
                                            studentNammes.add(student.getStudentName());
                                             total = studentNammes.size();
                                        }

                                        for (int i = 0; i < studentsLat.size(); i++){
                                            double studentLatt = Double.valueOf(studentsLat.get(i));
                                            double studentLngg = Double.valueOf(studentsLng.get(i));
                                            String studdentNames = String.valueOf(studentNammes.get(i));

                                            LatLng latLng = new LatLng(studentLatt, studentLngg);
                                            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                                                    .fromBitmap(customStudentMarker(MapsActivity.this))).title(studdentNames));

                                        }
                                    }
                                }
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    private Bitmap CreateCustomMarker(MapsActivity mapsActivity) {
                        return getBitmap(mapsActivity);
                    }

                    private Bitmap getBitmap(MapsActivity mapsActivity) {
                        View marker = ((LayoutInflater) mapsActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        ((Activity) mapsActivity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
                        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                        marker.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        marker.draw(canvas);

                        return bitmap;
                    }
                });
            }
        }

    }

    private Bitmap customStudentMarker(MapsActivity mapsActivity) {
        View marker = ((LayoutInflater) mapsActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.student_custom_marker, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mapsActivity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(20, 30));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;    }

    private void setSearchSchoolAutoComplite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schools");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolNameArrayList = new ArrayList<>();
                schoolNameKeyArrayList = new ArrayList<>();
                schoolLatAL = new ArrayList<>();
                schoolLngAL = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    assert student != null;
                    schoolNameArrayList.add(student.getSchoolName());
                    schoolNameKeyArrayList.add(student.getSchoolKey());
                    schoolLatAL.add(student.getSchoolLat());
                    schoolLngAL.add(student.getSchoolLng());

                    setSchoolName(schoolNameArrayList);
                }
            }

            private void setSchoolName(ArrayList<String> schoolNameArrayList) {
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(MapsActivity.this,R.layout.custom_list_item, R.id.text_view_list_item, schoolNameArrayList);
                searchSchoolAC.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (doctorCategoryId.equals("address")){
            addressMarkerIv.setVisibility(View.VISIBLE);
            searchSchoolAC.setCursorVisible(false);
            searchSchoolAC.getTextSize();
            closeIv.setVisibility(View.GONE);
            setBtn.setVisibility(View.VISIBLE);
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng centerLatLng = mMap.getCameraPosition().target;

                    Geocoder geocoder = new Geocoder(MapsActivity.this , Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(centerLatLng.latitude , centerLatLng.longitude,1);
                        if (addresses != null && addresses.size() > 0) {
                            String locality = addresses.get(0).getAddressLine(0);
                            String country = addresses.get(0).getCountryName();
                            studentLat = addresses.get(0).getLatitude();
                            studentLng = addresses.get(0).getLongitude();
                            if (!locality.isEmpty() && !country.isEmpty())
                                searchSchoolAC.setText(locality );
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }

        LatLng dhaka = new LatLng(23.837097, 90.38483);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 18));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.isBuildingsEnabled();
        mMap.isIndoorEnabled();
        mMap.getUiSettings().isTiltGesturesEnabled();
        mMap.getUiSettings().isCompassEnabled();
        mMap.getUiSettings().isMapToolbarEnabled();
        mMap.stopAnimation();
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void btnBackPress(View view) {
        onBackPressed();
    }

    public void btnClose(View view) {
        mMap.clear();
        searchSchoolAC.setText(null);
    }

    public void setAddress(View view) {
        Intent intent = new Intent(this, StudentRegActivity.class);
        intent.putExtra("Lat", studentLat);
        intent.putExtra("Lng", studentLng);

        intent.putExtra("sNamw", studentName);
        intent.putExtra("sClass", studentClass);
        intent.putExtra("scName", studentSchoolName);
        intent.putExtra("sRoll", studentRoll);
        intent.putExtra("sKey", schoolKeys);
        startActivity(intent);

        Toast.makeText(this, "Address set", Toast.LENGTH_SHORT).show();
    }
}
