package com.example.soff;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class Startup extends Application {

    public BluetoothConnectionService b;
    @Override
    public void onCreate()
    {
        super.onCreate();
        b = new BluetoothConnectionService(Startup.this);
        // Create an Intent to start the connectivity activity
        //Intent connectivity = new Intent( this,Connectivity_Activity.class);
        //Start the settings activity
        //startActivity(connectivity);
    }

    //((Startup)this.getApplicationContext()).b.SomeMethod();
}
