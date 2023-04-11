package com.example.calorappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    BottomAppBar appBar;
    FloatingActionButton camera;
    int prog;
    ProgressBar progressBar;
    TextView progressText;
    DatabaseReference dbUser;
    String dietType;
    String id;
    TextView mealText;
    TextView timeText;
    FloatingActionButton cameraAdd;
    float sugarLevel;
    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_home_page);
        appBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        cameraAdd = (FloatingActionButton) findViewById(R.id.cameraButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mealText = (TextView)findViewById(R.id.foodItemText);
        timeText = (TextView)findViewById(R.id.timeLoggedText);

        Intent intent = getIntent();
        id = intent.getStringExtra("message_key");
        dietType = intent.getStringExtra("dietType");
        sugarLevel = intent.getFloatExtra("sugar", 0.0f);
        dbUser = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Logged Food");


        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String lastName = snapshot.child("Food item").getValue(String.class);
                    String lastTime = snapshot.child("Time").getValue(String.class);
                    String errorName = "Log food to see updates";

                    if(prog <= 100){
                        prog = (int) (prog+ sugarLevel);
                        updateProgressBar();
                    }

                    if(lastName == null || lastTime == null){
                        mealText.setText(errorName);
                        timeText.setText(errorName);
                    }
                    else{
                        mealText.setText(lastName);
                        timeText.setText(lastTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        cameraAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddFoodItem.class);
                addIntent.putExtra("message_key", id);
                addIntent.putExtra("dietType", dietType);
                startActivity(addIntent);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.settings:
                        Intent logoutIntent = new Intent(getApplicationContext(),SettingsPage.class);
                        logoutIntent.putExtra("dietType", dietType);
                        logoutIntent.putExtra("message_key", id);
                        startActivity(logoutIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
    private void updateProgressBar(){
        progressBar.incrementProgressBy(prog);
        progressText.setText(String.valueOf(prog));
    }
}
