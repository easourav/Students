package com.example.firebasedemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.firebasedemo.PlaceAPI.Prediction;
import com.example.firebasedemo.databinding.ActivityAddressBinding;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements PredictionInterface{
    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    private ActivityAddressBinding binding;

    private List<Prediction> predictions;
    private Geocoder geocoder;

    String studentName, schoolName, schoolClass, studentRoll, schoolKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address);

        studentName = getIntent().getStringExtra("sNamw");
        schoolClass = getIntent().getStringExtra("sClass");
        schoolName = getIntent().getStringExtra("scName");
        studentRoll = getIntent().getStringExtra("sRoll");
        schoolKey = getIntent().getStringExtra("sKey");

        initRecyclerView();
        hideSearchViewIcon();
        searchViewAction();
    }
    private void searchViewAction() {

        binding.searchlocationSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                predictions.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    predictions.clear();
                    placesAutoCompleteAdapter.notifyDataSetChanged();
                }
                placesAutoCompleteAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    private void hideSearchViewIcon() {
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) binding.searchlocationSV.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        predictions = new ArrayList<>();
        placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), predictions, (PredictionInterface) this);
        binding.locationNameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.locationNameRecyclerView.setAdapter(placesAutoCompleteAdapter);

    }

    
    public void getPrediction(Prediction prediction) {
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(prediction.getDescription(), 1);
            hideSoftKeyboard();
            if (addresses.size()>0){


                String lat = String.valueOf(addresses.get(0).getLatitude());
                String lng = String.valueOf(addresses.get(0).getLongitude());

                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("categoryValue", "location");

                intent.putExtra("searchLat", lat);
                intent.putExtra("searchLng", lng);
                intent.putExtra("schoolName", schoolName);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentClass", schoolClass);
                intent.putExtra("sRoll", studentRoll);
                intent.putExtra("schoolKey", schoolKey);
                startActivity(intent);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void btnBackPress(View view) {
        onBackPressed();
    }
}
