package com.example.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;

    String schoolKey;

    EditText studentNameET, studentRollET;
    AutoCompleteTextView schoolNameAC, schoolClassAc;
    Spinner studentCameWaySP;
    Button locationBTN, saveBTN;

    String studentName, schoolClass, studentRoll,schoolName, studentLat , studentLng;
    Double lat, lng;

    ArrayList<String> schoolArrayList;
    ArrayList<String> schoolKeyArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();


        schoolNameAC = findViewById(R.id.actvSchoolSelect);
        studentNameET = findViewById(R.id.etStudentName);
        studentRollET = findViewById(R.id.etStudentRoll);
        schoolNameAC = findViewById(R.id.actvSchoolSelect);
        schoolClassAc = findViewById(R.id.actvClassSelect);
        studentCameWaySP = findViewById(R.id.spSchoolCameWay);
        locationBTN = findViewById(R.id.btnGetStudentLocation);
        saveBTN = findViewById(R.id.btnSaveStudentInfo);

        setSchoolNameAutoComplite();
        setClassNameAutoComplite();

        studentName = getIntent().getStringExtra("sNamw");
        schoolClass = getIntent().getStringExtra("sClass");
        schoolName = getIntent().getStringExtra("scName");
        studentRoll = getIntent().getStringExtra("sRoll");
        schoolKey = getIntent().getStringExtra("sKey");

         lat = getIntent().getDoubleExtra("Lat", 0);
         lng = getIntent().getDoubleExtra("Lng", 0);

        studentNameET.setText(studentName);
        schoolClassAc.setText(schoolClass);
        schoolNameAC.setText(schoolName);
        studentRollET.setText(studentRoll);


        schoolNameAC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolKey = schoolKeyArrayList.get(position);
            }
        });

    }

    private void setClassNameAutoComplite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("class");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    assert student != null;
                    schoolArrayList.add(student.getClassName());

                    setClassName(schoolArrayList);
                }
            }

            private void setClassName(ArrayList<String> classArrayList) {
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.custom_list_item, R.id.text_view_list_item, classArrayList);
                schoolClassAc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSchoolNameAutoComplite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schools");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolArrayList = new ArrayList<>();
                schoolKeyArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    assert student != null;
                    schoolArrayList.add(student.getSchoolName());
                    schoolKeyArrayList.add(student.getSchoolKey());
                    setSchoolName(schoolArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.childrenMenu) {
            Intent intentStudentMap = new Intent(this, MapsActivity.class);
            intentStudentMap.putExtra("categoryValue", "students");
            startActivity(intentStudentMap);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {


         studentName = studentNameET.getText().toString();
         studentRoll = studentRollET.getText().toString();
         schoolName = schoolNameAC.getText().toString();
         schoolClass = schoolClassAc.getText().toString();
        String studentCameWay = studentCameWaySP.getSelectedItem().toString();

        String latt = String.valueOf(lat);
        String lngg = String.valueOf(lng);


        DatabaseReference databaseReference = firebaseDatabase.getReference("students");

        String keyNode = databaseReference.push().getKey();

        if (!schoolName.isEmpty() && !studentRoll.isEmpty() && !studentName.isEmpty() && lat!=0.0){

            Student student = new Student(latt, lngg, keyNode, schoolName, schoolClass, studentName, studentRoll, studentCameWay, schoolKey);
            databaseReference.child(keyNode).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        studentNameET.setText("");
                        studentRollET.setText("");
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            Toast.makeText(MainActivity.this, "Insert all field & address", Toast.LENGTH_SHORT).show();
        }

    }

    private void setSchoolName(ArrayList<String> valueEJ) {

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,R.layout.custom_list_item, R.id.text_view_list_item, valueEJ);
        schoolNameAC.setAdapter(adapter);

    }

    public void setAddress(View view) {

        studentName = studentNameET.getText().toString();
        studentRoll = studentRollET.getText().toString();
        schoolName = schoolNameAC.getText().toString();
        schoolClass = schoolClassAc.getText().toString();

        Intent intentStudentMap = new Intent(this,MapsActivity.class);
        intentStudentMap.putExtra("categoryValue", "address");
        intentStudentMap.putExtra("schoolName", schoolName);
        intentStudentMap.putExtra("studentName", studentName);
        intentStudentMap.putExtra("studentClass", schoolClass);
        intentStudentMap.putExtra("sRoll", studentRoll);
        intentStudentMap.putExtra("schoolKey", schoolKey);
        startActivity(intentStudentMap);
    }


}