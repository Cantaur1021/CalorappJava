package com.example.calorappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class weighFoodActivity extends AppCompatActivity {
    DatabaseReference dbListener;
    Intent getIntent;
    String id;
    Button nextButton;
    String dietType;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_weigh_food);
        getIntent = getIntent();
        nextButton = findViewById(R.id.weighFoodItemButton);
        id  = getIntent.getStringExtra("message_key");
        dietType = getIntent.getStringExtra("dietType");
        selectedItem = getIntent.getStringExtra("foodName");

        dbListener = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Logged Food");
        dbListener.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(weighFoodActivity.this, "Take the food item out of the weighing machine", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(weighFoodActivity.this, SummaryPage.class);
                intent.putExtra("message_key", id);
                intent.putExtra("dietType", dietType);
                intent.putExtra("foodName", selectedItem);
                startActivity(intent);
            }
        });
    }
}