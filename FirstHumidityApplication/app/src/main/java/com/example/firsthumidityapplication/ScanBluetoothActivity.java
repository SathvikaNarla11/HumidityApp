package com.example.firsthumidityapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Set;
import android.Manifest;


public class ScanBluetoothActivity extends AppCompatActivity {

    //Step 1 : Create objects
    Button buttonOn, buttonOff, button;
    ListView listView;
    Set<BluetoothDevice> ad;
    BluetoothAdapter myBluetoothAdapter;

    Intent btEnablingIntent;
    int requestCodeForEnable;

    //Step 2 : Declaring constants of Bluetooth Adapter class
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;

    ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth);

        Button scanBackBtn = findViewById(R.id.scanBackBtn);
        scanBackBtn.setOnClickListener(v -> {
            String testData = "$MIST, 40.0, 37, 1, 1, 0, 1";
            Intent intent = new Intent();
            intent.putExtra("BLUETOOTH_DATA", testData);
            setResult(RESULT_OK, intent);
            finish();  // This closes the current activity and goes back to MainActivity
        });

        //Step 3 : Fetch the References
        buttonOn = findViewById(R.id.btnBTOn);
        buttonOff = findViewById(R.id.btnBTOff);
        button = findViewById(R.id.showBtn);
        listView = findViewById(R.id.listViewDevices);

        //Step 4 : Create the object of bluetooth adapter class
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothONMethod();
        // bluetoothOFFMethod();
        // exeButton();
    }

    private void bluetoothONMethod() {
        buttonOn.setOnClickListener(view -> {
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
}