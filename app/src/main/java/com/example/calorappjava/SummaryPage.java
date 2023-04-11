package com.example.calorappjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SummaryPage extends AppCompatActivity {

    String foodItemName;
    String id;
    String dietType;

    DatabaseReference dbUser;
    DatabaseReference dbFoodItem;
    String weight;
    float WeightInteger;
    float ItemWeight;
    float newSugar;

    Button returnButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_summary_page);
        Intent getIntent = getIntent();

        foodItemName = getIntent.getStringExtra("foodName");
        id = getIntent.getStringExtra("message_key");
        dietType = getIntent.getStringExtra("dietType");
        dbUser = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Logged Food");
        dbFoodItem = FirebaseDatabase.getInstance().getReference(dietType).child(foodItemName);
        returnButton = findViewById(R.id.returnHomePage);

        dbFoodItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String calories = snapshot.child("calories").getValue(String.class);
                    String fat = snapshot.child("fat").getValue(String.class);
                    String protein = snapshot.child("protein").getValue(String.class);
                    String sodium = snapshot.child("sodium").getValue(String.class);
                    String sugar = snapshot.child("sugar").getValue(String.class);

                    TextView foodName = findViewById(R.id.foodNameTextView);
                    foodName.setText(foodItemName);

                    TextView caloriesText = findViewById(R.id.caloriesTextView);
                    float newCalories = Float.parseFloat(calories);
                    newCalories = newCalories * ItemWeight;
                    String newCaloriesString = String.valueOf(newCalories);
                    caloriesText.setText(newCaloriesString);

                    TextView fatText = findViewById(R.id.fatTextView);
                    float newFat = Float.parseFloat(fat);
                    newFat = newFat * ItemWeight;
                    String newFatString = String.valueOf(newFat);
                    fatText.setText(newFatString);

                    TextView sugarText = findViewById(R.id.sugarTextView);
                    newSugar = Float.parseFloat(sugar);
                    newSugar = newSugar * ItemWeight;
                    String newSugarString = String.valueOf(newSugar);
                    sugarText.setText(newSugarString);

                    TextView proteinText = findViewById(R.id.proteinTextView);
                    float newProtein = Float.parseFloat(protein);
                    newProtein = newProtein * ItemWeight;
                    String newProteinString = String.valueOf(newProtein);
                    proteinText.setText(newProteinString);

                    TextView sodiumText = findViewById(R.id.sodiumTextView);
                    float newSodium = Float.parseFloat(sodium);
                    newSodium = newSodium * ItemWeight;
                    String newSodiumString = String.valueOf(newSodium);
                    sodiumText.setText(newSodiumString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Throwable databaseError = new Throwable();
                Log.e(TAG, "Failed to retrieve nutritional values: " + databaseError.getMessage());
            }
        });

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    weight = snapshot.child("Weight In Grams").getValue(String.class);
                    if(weight != null){
                        WeightInteger = Float.parseFloat(weight);
                        ItemWeight = WeightInteger/100;
                    }else{
                        weight = "10";
                    }
                    TextView weightTextView = findViewById(R.id.weightTextView);
                    weightTextView.setText(weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                homeIntent.putExtra("sugar", newSugar);
                homeIntent.putExtra("message_key", id);
                homeIntent.putExtra("dietType", dietType);
                startActivity(homeIntent);
                finish();
            }
        });
    }
}