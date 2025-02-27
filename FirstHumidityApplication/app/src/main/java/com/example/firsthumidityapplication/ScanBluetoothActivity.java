package com.example.firsthumidityapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class ScanBluetoothActivity extends AppCompatActivity {

    //Step 1 : Create objects
    Button b1, b2, b3;
    TextView tv1;
    Set<BluetoothDevice> ad;
    BluetoothAdapter adapter;

    //Step 2 : Declaring constants of Bluetooth Adapter class
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth);

        Button scanBackBtn = findViewById(R.id.scanBackBtn);
        scanBackBtn.setOnClickListener(v -> {
            finish();  // This closes the current activity and goes back to MainActivity
        });

        //Step 3 : Fetch the References
        b1 = findViewById(R.id.buttonOn);
        b2 = findViewById(R.id.buttonOff);
        b3 = findViewById(R.id.showBtn);
        tv1 = findViewById(R.id.textView);

        //Step 4 : Create the object of bluetooth adapter class
        adapter = BluetoothAdapter.getDefaultAdapter();

        //Step 5 : check bluetooth is available or not
        if(adapter == null)
        {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!adapter.isEnabled())
            {
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, REQUEST_ENABLE_BLUETOOTH);
            }
        }

        //Step 6 : Enable Bluetooth
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!adapter.isEnabled())
                {
                    Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(i, REQUEST_ENABLE_BLUETOOTH);
                }
            }
        });

        //Step 7 : Disable bluetooth
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.isEnabled())
                {
                    adapter.disable();
                }
            }
        });

        //Step 8 : Show the list of available bonded bluetooth devices
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                ad = adapter.getBondedDevices();
                for(BluetoothDevice temp : ad)
                {
                    sb.append("\n" + temp.getName()+"\n");
                }

                tv1.setText(sb.toString());
            }
        });
    }
}
