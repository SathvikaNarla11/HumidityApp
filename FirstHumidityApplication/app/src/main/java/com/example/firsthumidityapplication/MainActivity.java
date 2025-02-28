package com.example.firsthumidityapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.firsthumidityapplication.ScanBluetoothActivity;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button scanBtn, automaticBtn, manualBtn;
    TextView tvTemp, tvHum;
    BluetoothAdapter myBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanBtn = findViewById(R.id.scanBtn);
        automaticBtn = findViewById(R.id.btnAutomatic);
        manualBtn = findViewById(R.id.btnManual);
        tvTemp = findViewById(R.id.tvTemperature);
        tvHum = findViewById(R.id.tvHumidity);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        manualBtn.setEnabled(false);
        automaticBtn.setEnabled(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        exeButton();

//        // Toggle between Automatic & Manual mode when clicked
//        automaticBtn.setOnClickListener(v -> {
//            automaticBtn.setSelected(true);
//            manualBtn.setSelected(false);
//        });
//
//        manualBtn.setOnClickListener(v -> {
//            automaticBtn.setSelected(false);
//            manualBtn.setSelected(true);
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) { // Request code 1 for ScanActivity
            String receivedData = data.getStringExtra("BLUETOOTH_DATA");
            if (receivedData != null) {
                Toast.makeText(getApplicationContext(), "Packet received", Toast.LENGTH_SHORT).show();
                processReceivedData(receivedData); // Update UI
            }
        }
    }

    private void processReceivedData(String data) {
        if(data.startsWith("$MIST"))
        {
            String[] parts = data.split(",");
            if(parts.length == 7)
            {
                String temperature = parts[1].trim();
                String humidity = parts[2].trim();
                boolean fanStatus = parts[3].trim().equals("1");
                boolean mistStatus = parts[4].trim().equals("1");
                boolean ledStatus = parts[5].trim().equals("1");
                boolean isAutoMode = parts[6].trim().equals("1");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTemp.setText("TEMPERATURE  :     " + temperature + "  °C");
                        tvHum.setText("HUMIDITY           :     " + humidity + "  %Rh");

                        automaticBtn.setEnabled(isAutoMode);
                        manualBtn.setEnabled(!isAutoMode);

                        // Find ImageView
                        ImageView fanImageView = findViewById(R.id.imageFan);
                        // Set image based on fanStatus
                        if (fanStatus) {
                            fanImageView.setImageResource(R.drawable.led_on);
                        } else {
                            fanImageView.setImageResource(R.drawable.led_off);
                        }

                        ImageView mistImage = findViewById(R.id.imageMist);
                        if(mistStatus)
                        {
                            mistImage.setImageResource(R.drawable.led_on);
                        }
                        else {
                            mistImage.setImageResource(R.drawable.led_off);
                        }

                        ImageView ledImage = findViewById(R.id.imageLed);
                        if(ledStatus)
                        {
                            ledImage.setImageResource(R.drawable.led_on);
                        }
                        else {
                            ledImage.setImageResource(R.drawable.led_off);
                        }
                    }
                });
            }
        }
    }





    private void exeButton() {
            scanBtn.setOnClickListener(v ->
            {
                Intent intent = new Intent(MainActivity.this, ScanBluetoothActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, 1);

            });

        // Listen for Bluetooth state changes
        BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                   // manualBtn.setEnabled(true);
                   // automaticBtn.setEnabled(true); // Enable btnAutomatic when Bluetooth is ON
                    Toast.makeText(getApplicationContext(), "Bluetooth is ON. btnAutomatic Enabled!", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                   // manualBtn.setEnabled(false);
                   // automaticBtn.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Turn ON bluetooth to enable automatic mode!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Register Bluetooth state change listener
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, filter);
    }
}