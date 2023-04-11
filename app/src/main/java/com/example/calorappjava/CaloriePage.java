package com.example.calorappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import android.Manifest;

public class CaloriePage extends AppCompatActivity {
    RadioGroup caloriesRadioGroup;
    RadioButton radioButton;
    String id;
    String dietType;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_calorie_page);
        caloriesRadioGroup =(RadioGroup)findViewById(R.id.caloriesRadioGroup);
        Intent getIntent = getIntent();
        dietType = getIntent.getStringExtra("dietType");
        Button nextButton = (Button) findViewById(R.id.calorieNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),HomePage.class);
                i.putExtra("message_key", id);
                i.putExtra("dietType", dietType);
                startActivity(i);

            }
        });
    }

    public void checkButton(View v){
        int radioId = caloriesRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String radioText = radioButton.getText().toString();
//        Toast.makeText(CaloriePage.this, ""+radioText, Toast.LENGTH_SHORT).show();
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("Calories: ", radioText);
        Intent i = getIntent();
        id = i.getStringExtra("message_key");
        db.child(id).updateChildren(updates);
    }
}