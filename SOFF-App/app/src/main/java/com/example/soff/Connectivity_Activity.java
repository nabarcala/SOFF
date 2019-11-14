package com.example.soff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class Connectivity_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "Connectivity_Activity";
    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService mBluetoothConnection;

    Button btnEnableDisable_Discoverable;
    Button btnStartConnection;
    Button btnSend;

    TextView incomingMessages;
    StringBuilder messages;

    EditText etSend;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothDevice mBTDevice;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;

    //Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadCastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //catch action state change request
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);
                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG,"onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG,"onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"onReceive: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"onReceive: STATE TURNING ON");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG,"onReceive: Connected ON");
                        break;

                }
            }
            else if(action.equals(mBluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED))
            {
                Log.d(TAG, "Connection state change");
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,mBluetoothAdapter.ERROR);
                switch(state){
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadCastReceiver2: Connecting");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadCastReceiver2: Connected");
                        Context context1 = getApplicationContext();
                        CharSequence text = "Device Connected";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context1, text, duration);
                        toast.show();
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTING:
                        Log.d(TAG, "mBroadCastReceiver2: Disconnecting");
                        break;
                }

            }
        }

    };
    //Broadcast receiver for changes made to bluetooth states such as Discoverability mode on/off
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode =  intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch(mode){
                    //device is in descoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadCastReceiver2: Discoverability enabled");
                        break;
                    //device is not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadCastReceiver2: Discoverability enabled. Able to receive connections");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadCastReceiver2: Discoverability disabled. Not able to receive connections");
                        break;

                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG,"onReceive: ACTION_FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getName() != null)
                {
                    mBTDevices.add(device);
                    Log.d(TAG,"onReceive: "+device.getName() +" "+device.getAddress());
                }

                mDeviceListAdapter = new DeviceListAdapter(context,R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };
    //Broadcast receiver that detects bond state changes(pairing)
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //case 1: bonded already
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    Log.d(TAG,"BroadcastReceiver4: BOND_BONDED");
                    mBTDevice = mDevice;
                    Context context1 = getApplicationContext();
                    CharSequence text = "Device Bonded";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context1, text, duration);
                    toast.show();
                }
                //case: 2 creating a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING)
                {
                    Log.d(TAG,"BroadcastReceiver4: BOND_BONDING");
                }
                //case 3: bond is broken
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE)
                {
                    Log.d(TAG,"BroadcastReceiver4: BOND_NONE");
                }
            }
        }
    };

    @Override
    protected void onDestroy(){
        Log.d(TAG,"onDestroy: called");
        //fixes a crash when a receiver that is unregistered is registered
        //may cause problems later, idk
        try {
            unregisterReceiver(mBroadCastReceiver1);
            //Register or UnRegister your broadcast receiver here

        } catch(IllegalArgumentException e) {

            e.printStackTrace();
        }
        try{
            unregisterReceiver(mBroadcastReceiver2);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        try{
            unregisterReceiver(mBroadcastReceiver3);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        try{
            unregisterReceiver(mBroadcastReceiver4);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectivity);
        Button btnONOFF = (Button) findViewById(R.id.btnOFFON);

        // Should enable discoverability
        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnOFFON);

        // Should starting listing bluetooth devices when turned on
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        btnStartConnection = (Button)findViewById(R.id.btnStartConnection);
        btnSend = (Button)findViewById(R.id.btnSend);

        etSend = (EditText)findViewById(R.id.editText);

        incomingMessages = (TextView) findViewById(R.id.incomingMessage);
        messages = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        //broadcasts when bond state changes(pairing)
        IntentFilter filter  = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4,filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(Connectivity_Activity.this);

        btnONOFF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG,"onClick: enabling/disabling bluetooth");
                enableDisableBT();
            }
        });
        btnStartConnection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startconnection();

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View view){
                try {
                    byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    etSend.setText("");
                }
                catch(NullPointerException e)
                {
                    Context context1 = getApplicationContext();
                    CharSequence text = "NullPointer Exception: Error sending";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context1, text, duration);
                    toast.show();
                }

            }
        });

    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");
            messages.append(text + "\n");
            Log.d(TAG,"Displaying message");
            incomingMessages.setText(messages);
        }
    };
    //will crash if device hasnt been paired first
    public void startconnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);

    }
    public void startBTConnection(BluetoothDevice device,UUID uuid)
    {
        try{
            Context context = getApplicationContext();
            CharSequence text = "Pairing with: "+ device.getName();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.d(TAG,"StartBTConnection: Initializing RFCOM BT connection");

            mBluetoothConnection.startClient(device,uuid);
            Thread.sleep(5000);
            Log.d(TAG, "Swag");
            if(mBluetoothConnection.getconnection() == 0)
            {
                Log.d(TAG,"Connection Failed");
                CharSequence text2 = "Connection Failed with "+ device.getName();
                int duration2 = Toast.LENGTH_SHORT;

                Toast toast2 = Toast.makeText(context, text2, duration2);
                toast2.show();
            }
            else
            {
                Log.d(TAG,"Connection Successful");
                Context context3 = getApplicationContext();
                CharSequence text3 = "Connected with "+ device.getName();
                int duration3 = Toast.LENGTH_SHORT;

                Toast toast3 = Toast.makeText(context, text3, duration3);
                toast3.show();

                // Redirect to main activity after successfully connecting
                int timeout = 2000; // time out 2 seconds
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent home = new Intent(Connectivity_Activity.this, MainActivity.class);
                        startActivity(home);
                    }
                }, timeout);
            }
        }
        catch(NullPointerException e)
        {
            Context context = getApplicationContext();
            CharSequence text = "You must pair a device first";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void enableDisableBT()
    {
        if (mBluetoothAdapter == null)
        {
            Log.d(TAG,"device not capable of using bluetooth");
        }
        if(!mBluetoothAdapter.isEnabled())
        {
            Log.d(TAG,"onClick: enabling bluetooth");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            //catches state changes of BT and logs it
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadCastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled())
        {
            Log.d(TAG,"onClick: disabling bluetooth");
            mBluetoothAdapter.disable();
            //catches state changes of BT and logs it
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadCastReceiver1, BTIntent);
        }
    }
    public void btnEnableDisable_Discoverable(View view)
    {
        Log.d("TAG","Making device discoverable for 300 seconds");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);

        IntentFilter intentfilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentfilter);
    }
    public void btnDiscover(View view)
    {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

//        mDeviceListAdapter.clear();
//        lvNewDevices.setAdapter(null);

        if(mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG,"btnDiscover: Cancelling Discovery.");

            checksBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering())
        {
             checksBTPermissions();
             mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

    }
    private void checksBTPermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
            {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
            else{
                Log.d(TAG,"checkBTPermissions: No need to check permissions. SDK version < Lollipop");
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //cancels device discovery for performance(memory intensive)
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG,"onItemClick: You clicked on a device");
        String devicename = mBTDevices.get(i).getName();
        String deviceaddress = mBTDevices.get(i).getAddress();

        Log.d(TAG,"onItemClick: "+devicename);
        Log.d(TAG,"onItemClick: "+deviceaddress);

        //creates bond
        //requires api 17+
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG,"Trying to pair with "+devicename);
            mBTDevices.get(i).createBond();
            mBTDevice = mBTDevices.get(i);
            //mBluetoothConnection = new BluetoothConnectionService(Connectivity_Activity.this);
            mBluetoothConnection = ((Startup)this.getApplicationContext()).b;
        }
    }
    public void main1(View view)
    {
        // Create an Intent to start the settings activity
        Intent main_activity = new Intent(this,MainActivity.class);
        //Start the settings activity
        startActivity(main_activity);
    }
}
