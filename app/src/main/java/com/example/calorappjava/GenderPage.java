package com.example.calorappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

public class GenderPage extends AppCompatActivity {
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
        Intent intent = getIntent();
        id = intent.getStringExtra("message_key");
        setContentView(R.layout.activity_gender_page);
        radioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        Button nextButton = (Button) findViewById(R.id.genderSelectPageNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MedicationPage.class);
                i.putExtra("message_key", id);
                startActivity(i);
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = radioGroup.findViewById(radioId);
        String radioText = radioButton.getText().toString();
//        Toast.makeText(GenderPage.this, ""+radioText, Toast.LENGTH_SHORT).show();
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("Gender", radioText);
        db.child(id).updateChildren(updates);
    }
}
