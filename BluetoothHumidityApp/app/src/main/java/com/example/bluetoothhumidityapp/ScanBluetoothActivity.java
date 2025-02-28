//package com.example.bluetoothhumidityapp;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//
//
//import java.util.ArrayList;
//import java.util.Set;
//
//import kotlinx.coroutines.channels.ChannelSegment;
//
//
//public class ScanBluetoothActivity extends AppCompatActivity
//{
//    Button buttonON, buttonOFF, button;
//
//    ListView listView;
//    BluetoothAdapter myBluetoothAdapter;
//
//    Intent btEnablingIntent;
//    int requestCodeForEnable;
//
//    private ArrayList<String> deviceList;
//    private ArrayAdapter<String> adapter;
//
//
//    ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled", Toast.LENGTH_SHORT).show();
//                }
//            }
//    );
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(bluetoothReceiver);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_bluetooth);
//
//        buttonON = findViewById(R.id.btOn);
//        buttonOFF = findViewById(R.id.btOff);
//        button = findViewById(R.id.btShow);
//        listView = findViewById(R.id.listViewDevices);
//
//        deviceList = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
//        listView.setAdapter(adapter);
//
//        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        btEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        requestCodeForEnable = 1;
//
//
//        bluetoothONMethod();
//        bluetoothOFFMethod();
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(bluetoothReceiver, filter);
//
//        exeButton();
//    }
//
//    private void exeButton() {
//        button.setOnClickListener(v -> {
//            if (myBluetoothAdapter == null) {
//                Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            if (!myBluetoothAdapter.isEnabled()) {
//                Toast.makeText(getApplicationContext(), "Bluetooth is OFF. Please enable it first.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Request Location Permission (needed for discovery)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                return;
//            }
//
//            // Clear old data
//            deviceList.clear();
//            adapter.notifyDataSetChanged();
//
//            // Get paired devices
//            Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
//            for (BluetoothDevice device : pairedDevices) {
//                deviceList.add("Paired: " + device.getName() + " - " + device.getAddress());
//            }
//
//            // Start Bluetooth discovery
//            if (myBluetoothAdapter.isDiscovering()) {
//                myBluetoothAdapter.cancelDiscovery();
//            }
//            myBluetoothAdapter.startDiscovery();
//            Toast.makeText(getApplicationContext(), "Scanning for available devices...", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device != null) {
//                    String deviceName = device.getName();
//                    String deviceAddress = device.getAddress();
//                    if (deviceName != null) {
//                        deviceList.add("Available: " + deviceName + " - " + deviceAddress);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        }
//    };
//
//
//
////    private void exeButton() {
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
////                String[] strings = new String[bt.size()];
////                int index = 0;
////
////                if(bt.size() > 0)
////                {
////                    for(BluetoothDevice device:bt)
////                    {
////                        strings[index] = device.getName();
////                        index++;
////                    }
////                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings );
////                    listView.setAdapter(arrayAdapter);
////                }
////            }
////        });
////    }
//
//
//    private void bluetoothONMethod() {
//        buttonON.setOnClickListener(view -> {
//            if (myBluetoothAdapter == null) {
//                Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
//            } else {
//                // Check if Bluetooth permission is granted (for Android 12+)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
//                                != PackageManager.PERMISSION_GRANTED) {
//
//                    // Request permission
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
//                    return;
//                }
//
//                // If permission is granted, proceed with enabling Bluetooth
//                if (!myBluetoothAdapter.isEnabled()) {
//                    Toast.makeText(getApplicationContext(), "Requesting to enable Bluetooth", Toast.LENGTH_SHORT).show();
//                    enableBluetoothLauncher.launch(btEnablingIntent);
//                }
//            }
//        });
//    }
//
//    private void bluetoothOFFMethod() {
//        buttonOFF.setOnClickListener(view -> {
//            if (myBluetoothAdapter == null) {
//                Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            // Check Bluetooth permission for Android 12+
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
//                return;
//            }
//
//            if (myBluetoothAdapter.isEnabled()) {
//                Toast.makeText(getApplicationContext(), "Redirecting to Bluetooth settings", Toast.LENGTH_SHORT).show();
//
//                // Open Bluetooth settings since we can't turn it off programmatically
//                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//                startActivity(intent);
//            } else {
//                Toast.makeText(getApplicationContext(), "Bluetooth is already OFF", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
////    private void bluetoothOFFMethod() {
////        buttonOFF.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if(myBluetoothAdapter.isEnabled())
////                {
////                    Toast.makeText(getApplicationContext(), "Requesting to disable Bluetooth", Toast.LENGTH_SHORT).show();
////                    myBluetoothAdapter.disable();
////                }
////            }
////        });
////    }
//}


package com.example.bluetoothhumidityapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


import java.util.ArrayList;
import java.util.Set;

import kotlinx.coroutines.channels.ChannelSegment;


public class ScanBluetoothActivity extends AppCompatActivity
{
    Button buttonON, buttonOFF, button;

    ListView listView;
    BluetoothAdapter myBluetoothAdapter;

    Intent btEnablingIntent;
    int requestCodeForEnable;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth);

        buttonON = findViewById(R.id.btOn);
        buttonOFF = findViewById(R.id.btOff);
        button = findViewById(R.id.btShow);
        listView = findViewById(R.id.listViewDevices);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;


        bluetoothONMethod();
        bluetoothOFFMethod();
        exeButton();
    }

    private void exeButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if(bt.size() > 0)
                {
                    for(BluetoothDevice device:bt)
                    {
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings );
                        listView.setAdapter(arrayAdapter);
                }
            }
        });
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
