package com.example.calorappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MedicationPage extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton yesButton;
    RadioButton noButton;
    EditText MedicationName;
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

        setContentView(R.layout.activity_medication_page);
        radioGroup = (RadioGroup) findViewById(R.id.yesOrNoRadio);
        MedicationName = (EditText)findViewById(R.id.MedicationName);
        yesButton = (RadioButton)findViewById(R.id.YesRadio);
        noButton = (RadioButton)findViewById(R.id.NoRadio);

        Button button = (Button)findViewById(R.id.MedicationNextButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DietType.class);
                i.putExtra("message_key", id);
                startActivity(i);
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = radioGroup.findViewById(radioId);
        String radioText = radioButton.getText().toString();
//        Toast.makeText(MedicationPage.this, ""+radioText, Toast.LENGTH_SHORT).show();
        if(radioId == yesButton.getId()){
            MedicationName.setVisibility(View.VISIBLE);
            Map<String, Object> yesUpdates = new HashMap<String, Object>();
            yesUpdates.put("Taking Medication", "Yes");
            db.child(id).updateChildren(yesUpdates);
            Map<String, Object> medicationName = new HashMap<String, Object>();
            medicationName.put("Medication Name", MedicationName.getText().toString());
        }else if(radioId == noButton.getId()){
            MedicationName.setVisibility(View.GONE);
            Map<String, Object> noUpdates = new HashMap<String, Object>();
            noUpdates.put("Taking Medication", "No");
            db.child(id).updateChildren(noUpdates);
        }
    }
}