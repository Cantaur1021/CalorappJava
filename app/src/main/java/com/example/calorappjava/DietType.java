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

public class DietType extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    String id;
    String radioText;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_diet_type);
        radioGroup = (RadioGroup)findViewById(R.id.dietRadioGroup);
        Button nextButton = (Button) findViewById(R.id.dietNextPageButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CaloriePage.class);
                i.putExtra("message_key", id);
                i.putExtra("dietType", radioText);
                startActivity(i);
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        radioText = radioButton.getText().toString();
//        Toast.makeText(DietType.this, ""+radioText, Toast.LENGTH_SHORT).show();
        Map<String, Object>updates = new HashMap<String, Object>();
        updates.put("Diet", radioText);
        Intent i = getIntent();
        id = i.getStringExtra("message_key");
        db.child(id).updateChildren(updates);
    }
}