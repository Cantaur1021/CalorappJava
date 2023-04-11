package com.example.calorappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;

import java.sql.Statement;

public class SignUp extends AppCompatActivity {

    public EditText name, email, password;
    public FirebaseAuth auth;
    public DatabaseReference db;
    public String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users");
        name = (EditText) findViewById(R.id.signUpUsersName);
        email = (EditText) findViewById(R.id.signUpEmailField);
        password = (EditText) findViewById(R.id.signUpPassword);


        Button nextButton = (Button) findViewById(R.id.signUpNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String nameText = name.getText().toString();
                    String emailText = email.getText().toString();
                    String passwordText = password.getText().toString();
                    if(TextUtils.isEmpty(nameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
                        Toast.makeText(SignUp.this, "Please fill all the details!", Toast.LENGTH_SHORT).show();
                    }else{
                        registerUser(emailText, passwordText);
                        addUserToDatabase(nameText,emailText);
//                        Intent i = new Intent(getApplicationContext(),ConditionPage.class);
//                        startActivity(i);
                    }
//                    Intent i = new Intent(getApplicationContext(),ConditionPage.class);
//                    startActivity(i);
                }
        });


    }
    private void registerUser(String emailUser, String passwordUser){
        auth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUp.this, "REGISTRATION FAILED!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserToDatabase(String nameUser, String emailUser){
        Intent i = new Intent(getApplicationContext(), connectToSensorActivity.class);
        i.putExtra("nameOfUser", nameUser);
        i.putExtra("emailUser", emailUser);
        startActivity(i);
    }
}

