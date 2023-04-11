package com.example.calorappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsPage extends AppCompatActivity {

    Button logOutButton;
    String id;
    String dietType;
    Button foodPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        logOutButton = findViewById(R.id.logOutPage);
        Intent getIntent = getIntent();
        dietType = getIntent.getStringExtra("dietType");
        id = getIntent.getStringExtra("message_key");
        foodPlan = findViewById(R.id.foodPlan);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(getApplicationContext(),MainActivity.class);
                returnIntent.putExtra("dietType", dietType);
                returnIntent.putExtra("message_key", id);
                startActivity(returnIntent);
            }
        });

        foodPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent returnIntent = new Intent(getApplicationContext(), FoodPlanActivity.class);
                returnIntent.putExtra("dietType", dietType);
                returnIntent.putExtra("message_key", id);
                startActivity(returnIntent);
            }

        });
    }
}