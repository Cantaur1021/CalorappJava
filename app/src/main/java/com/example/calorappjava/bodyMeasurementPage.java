package com.example.calorappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class bodyMeasurementPage extends AppCompatActivity {

    EditText birthDate;
    EditText weight;
    EditText height;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_body_measurement_page);
        birthDate = findViewById(R.id.editTextBirthday);
        weight = findViewById(R.id.editTextWeight);
        height = findViewById(R.id.editTextHeight);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        bodyMeasurementPage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        birthDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        Button nextButton = (Button) findViewById(R.id.bodyMeasurementNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String birthdayString = birthDate.getText().toString();
                String weightString = weight.getText().toString();
                String heightString = height.getText().toString();
                if(TextUtils.isEmpty(birthdayString) || TextUtils.isEmpty(weightString) || TextUtils.isEmpty(heightString)){
                    Toast.makeText(bodyMeasurementPage.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = getIntent();
                        String id = intent.getStringExtra("message_key");
                        Map<String, Object> birthdayUpdate = new HashMap<String, Object>();
                        Map<String, Object> weightUpdate = new HashMap<String, Object>();
                        Map<String, Object> heightUpdate = new HashMap<String, Object>();
                        birthdayUpdate.put("Birthday: ", birthdayString);
                        weightUpdate.put("Weight: ", weightString);
                        heightUpdate.put("Height: ", heightString);
                        db.child(id).updateChildren(birthdayUpdate);
                        db.child(id).updateChildren(weightUpdate);
                        db.child(id).updateChildren(heightUpdate);
                        Log.d("ID", id);
                    intent = new Intent(getApplicationContext(), GenderPage.class);
                    intent.putExtra("message_key", id);
                    startActivity(intent);
                }
            }
        });
    }
}