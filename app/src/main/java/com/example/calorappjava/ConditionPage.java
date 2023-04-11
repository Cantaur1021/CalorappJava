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

public class ConditionPage extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    String id;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_condition_page);
        Button nextButton = (Button) findViewById(R.id.AboutConditionNextButton);
        radioGroup= (RadioGroup) findViewById(R.id.conditionRadioGroup);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),bodyMeasurementPage.class);
                intent.putExtra("message_key", id);
                startActivity(intent);
            }
        });
    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = radioGroup.findViewById(radioId);
        String radioText = radioButton.getText().toString();
//        Toast.makeText(ConditionPage.this, ""+radioText, Toast.LENGTH_SHORT).show();
        Map<String,Object> updates = new HashMap<String,Object>();
        updates.put("Condition", radioText);
        Intent i = getIntent();
        id = i.getStringExtra("message_key");
        db.child(id).updateChildren(updates);
    }
}