package com.example.soff;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseInstallation;

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

        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            // if defined
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build()
        );

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    //((Startup)this.getApplicationContext()).b.SomeMethod();
}
