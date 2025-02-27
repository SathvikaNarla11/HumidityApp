package com.example.firsthumidityapplication;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ScanBluetoothActivity extends AppCompatActivity {

    //Step 1 : Create objects
    Button b1, b2, b3;
    TextView tv1;

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
        b1 = (Button)findViewById(R.id.buttonOn);
        b2 = (Button)findViewById(R.id.buttonOff);
        b3 = (Button)findViewById(R.id.buttonShow);
        tv1 = (TextView)findViewById(R.id.textView);

        //Step 4 : Create the object of bluetooth adapter class
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        //Step 5 : check bluetooth is available or not
        if(adapter == null)
        {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).SHOW();
        }

    }
}
