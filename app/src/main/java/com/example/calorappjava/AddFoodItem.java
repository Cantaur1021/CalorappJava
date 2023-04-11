package com.example.calorappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddFoodItem extends AppCompatActivity {

    DatabaseReference dbFoodItem;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
    Spinner spinner;

    Button nextButton;
    String UUID;
    Intent getIntent;
    String dietType;
    Intent getDietIntent;
    String selectedItem;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_food_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        nextButton = (Button)findViewById(R.id.nextButtonAddFoodItem);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        getDietIntent = getIntent();
        dietType = getDietIntent.getStringExtra("dietType");
        dbFoodItem = FirebaseDatabase.getInstance().getReference(dietType);



        dbFoodItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> data = new ArrayList<String>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String item = dataSnapshot.getKey();
                    data.add(item);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddFoodItem.this, android.R.layout.simple_spinner_item, data);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddFoodItem.this, "Problem with connection", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                getIntent = getIntent();
                UUID = getIntent.getStringExtra("message_key");
                db.child(UUID).child("Logged Food").child("Food item").setValue(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddFoodItem.this, "Please select a food item", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(AddFoodItem.this, weighFoodActivity.class);
                nextActivity.putExtra("message_key", UUID);
                nextActivity.putExtra("foodName",selectedItem);
                nextActivity.putExtra("dietType", dietType);
                startActivity(nextActivity);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.settings:
                        Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(logoutIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
}