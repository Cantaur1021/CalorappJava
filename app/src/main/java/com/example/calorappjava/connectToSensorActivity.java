package com.example.calorappjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class connectToSensorActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mSendButton;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private OutputStream mOutputStream;

    Intent getIntent;
    DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("Users");

    private static final UUID UUID_HC05 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // HC-05 UUID

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_PERMISSION_BLUETOOTH = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_connect_to_sensor);

        mEditText = findViewById(R.id.edit_text);
        mSendButton = findViewById(R.id.connectionNextButton);

        getIntent = getIntent();


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check for permission to access Bluetooth
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
        }

        // Check if Bluetooth is enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        // Connect to HC-05 module
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("HC-05")) { // Replace with your module's name
                    mDevice = device;
                    break;
                }
            }
        }

        if (mDevice == null) {
            Toast.makeText(this, "HC-05 module not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            mSocket = mDevice.createRfcommSocketToServiceRecord(UUID_HC05);
            mSocket.connect();
            mOutputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Bluetooth connection failed", e);
            Toast.makeText(this, "Bluetooth connection failed", Toast.LENGTH_SHORT).show();
            finish();
        }

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mEditText.getText().toString();
                String newMessage = message+ ";";
                try {
                    mOutputStream.write(newMessage.getBytes());
                    Intent nextActivityIntent = new Intent(connectToSensorActivity.this, ConditionPage.class);
                    String userName = getIntent.getStringExtra("nameOfUser");
                    String userEmail = getIntent.getStringExtra("emailUser");

                    Map<String,Object> nameOfUser = new HashMap<String,Object>();
                    nameOfUser.put("Name", userName);
                    Map<String,Object> emailOfUser = new HashMap<String,Object>();
                    emailOfUser.put("Email", userEmail);
                    dbUser.child(message).updateChildren(nameOfUser);
                    dbUser.child(message).updateChildren(emailOfUser);
                    nextActivityIntent.putExtra("message_key", message);
                    startActivity(nextActivityIntent);
                } catch (IOException e) {
                    Log.e(TAG, "Error sending message", e);
                    Toast.makeText(connectToSensorActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }
}
