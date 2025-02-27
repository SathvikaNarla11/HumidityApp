package com.example.bluetoothhumidityapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


import kotlinx.coroutines.channels.ChannelSegment;

public class ScanBluetoothActivity extends AppCompatActivity
{
    Button buttonON, buttonOFF;
    BluetoothAdapter myBluetoothAdapter;

    Intent btEnablingIntent;
    int requestCodeForEnable;

    ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Enabled doingggg", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled doinggg", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth);

        buttonON = findViewById(R.id.btOn);
        buttonOFF = findViewById(R.id.btOff);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothONMethod();
        bluetoothOFFMethod();
    }


    private void bluetoothONMethod() {
        buttonON.setOnClickListener(view -> {
            if (myBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
            } else {
                // Check if Bluetooth permission is granted (for Android 12+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                                != PackageManager.PERMISSION_GRANTED) {

                    // Request permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                    return;
                }

                // If permission is granted, proceed with enabling Bluetooth
                if (!myBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Requesting to enable Bluetooth", Toast.LENGTH_SHORT).show();
                    enableBluetoothLauncher.launch(btEnablingIntent);
                }
            }
        });
    }

    private void bluetoothOFFMethod() {
        buttonOFF.setOnClickListener(view -> {
            if (myBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
                return;
            }

            // Check Bluetooth permission for Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }

            if (myBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Redirecting to Bluetooth settings", Toast.LENGTH_SHORT).show();

                // Open Bluetooth settings since we can't turn it off programmatically
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth is already OFF", Toast.LENGTH_SHORT).show();
            }
        });
    }



//    private void bluetoothOFFMethod() {
//        buttonOFF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(myBluetoothAdapter.isEnabled())
//                {
//                    Toast.makeText(getApplicationContext(), "Requesting to disable Bluetooth", Toast.LENGTH_SHORT).show();
//                    myBluetoothAdapter.disable();
//                }
//            }
//        });
//    }
}
