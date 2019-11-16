package com.example.soff;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.nio.charset.Charset;

public class Settings_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);
    }

    public void connectivity(View view){
        // Create an Intent to start the connectivity activity
        Intent connectivity = new Intent(this,Connectivity_Activity.class);
        //Start the settings activity
        startActivity(connectivity);
    }
    public void fruit_list(View view)
    {
        // Create an Intent to start the settings activity
        Intent fruits = new Intent(this,Fruit_List.class);
        //Start the settings activity
        startActivity(fruits);
    }
    public void ripeness_list(View view)
    {
        Intent ripe = new Intent(this,Ripe_List.class);
        startActivity(ripe);
    }
}
