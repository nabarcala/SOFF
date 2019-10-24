package com.example.soff;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {


    private static final String TAG = "BluetoothConnectionServ";
    private static final String appname = "SOFF";
    private static final UUID My_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectthread;
    private BluetoothDevice mmdevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedThread;
    private String DeviceName;



    //runs until a connection is accepted
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            //create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appname, My_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up the Server using" + My_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "AcceptingThread: IOException: " + e.getMessage());
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run:AcceptThread Running");
            BluetoothSocket socket = null;
            try {

                //this is a blocking call and will only return on a successful conection
                //or an exception
                Log.d(TAG, "run: RFCOM server socket start....");
                socket = mmServerSocket.accept();
                Log.d(TAG, "run: RFCOM server socket accepted connection.");
            } catch (IOException e) {
                Log.e(TAG, "AcceptingThread: IOException: " + e.getMessage());
            }
            if (socket != null) {
                connected(socket, mmdevice);
            }
            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Cancelling AcceptThread");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed " + e.getMessage());
            }
        }


    }

    //runs while attempting to make an outgoing connection
    //connection either succeeds or fails
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            mmdevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "run mConnectThread");

            //get a bluetooth socket for a connection with the given bluetooth device
            try {
                Log.d(TAG, "ConnectedThread: trying to create InsecureRFcommSocket using UUId" + My_UUID_INSECURE);
                tmp = mmdevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "ConnectedThread: could not create InsecureRFCommSocket " + e.getMessage());
            }
            mmSocket = tmp;
            //cancels discovery
            mBluetoothAdapter.cancelDiscovery();
            //make a connection to the BluetoothSocket
            try {
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {

                    Log.d(TAG, "mConnectThread: run: Unable to close connection in socket" + e1.getMessage());
                }
                Log.d(TAG, "run:ConnectedThread: could not connect to UUID: " + My_UUID_INSECURE);

            }
            connected(mmSocket, mmdevice);
        }

        public void cancel() {
            try {
                Log.d(TAG, "cancel: close client socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in ConnectThread failed" + e.getMessage());
            }
        }
    }

    public synchronized void start()
    {
        Log.d(TAG,"start");

        //cancel any thread attempting to make a connection
        if(mConnectthread != null)
        {
            mConnectthread.cancel();
            mConnectthread = null;
        }
        if(mInsecureAcceptThread == null)
        {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }
//Acceptthread starts and sits waiting for a connection
    //Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread
    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG,"StartClient: Started");
        //initprogress Dialog
        //mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth","Please wait...",true);

        mConnectthread = new ConnectThread(device,uuid);
        mConnectthread.start();
        DeviceName = device.getName();
    }
    public String getGetDeviceName()
    {
        return DeviceName;
    }
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            Log.d(TAG,"ConnectedThread: Starting");
            mmSocket = socket;
            InputStream tmpin = null;
            OutputStream tmpout = null;

            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch(NullPointerException e){
                e.printStackTrace();
            }

            try {
                tmpin = mmSocket.getInputStream();
                tmpout = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpin;
            mmOutStream = tmpout;
        }
        public void run()
        {
            byte[] buffer  = new byte[8192]; //bufer store for the stream
            int bytes;
            boolean eof = false;
            while(true) {
                //read from inputstream
                try {
                    mConnectedThread.sleep(1000);
                    bytes = mmInStream.read(buffer);

                    int offset = 11;
                    String incomingmessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream123 " + incomingmessage);

                    if (incomingmessage.length() < 100)
                    {
                        Log.d(TAG, "InputStream " + incomingmessage);

                        Intent incomingMessageIntent = new Intent("incomingMessage");
                        incomingMessageIntent.putExtra("theMessage",incomingmessage);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);
                    }
                    else
                    {
                        Log.d(TAG, "InputStream " + "incomingimage");
                        //int bytes2 = mmInStream.read(buffer);
                        //String incomingmessage2 = new String(buffer,0,bytes2);
                        //comment out if error
                        //incomingmessage = incomingmessage+incomingmessage2;
                        Log.d(TAG, "InputStream image:");
                        Intent incomingImageIntent = new Intent("incomingImage");
                        incomingImageIntent.putExtra("theImage",incomingmessage);
                       // incomingImageIntent.putExtra("theImage",bytes);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingImageIntent);
                    }

                } catch (IOException e) {
                    Log.e(TAG,"write: Error reading from inputstream "+e.getMessage());
                    e.printStackTrace();
                    break;
                } //catch (InterruptedException e) {
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //e.printStackTrace();
                //}
            }
        }
        public void write(byte[] bytes)
        {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG,"write:Writing to OutputStream: "+text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG,"write: Error writing to outputstream "+e.getMessage());
            }
        }
        //call to shut down connection
        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        }
    private void connected(BluetoothSocket mmSocket,BluetoothDevice mmDevice){
        Log.d(TAG,"Connected: Starting");
        //Start the thread to manage the connection and perfrom transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out)
    {
        //create temp object
        ConnectedThread r;
        //Synchronize a copy of the connected thread
        Log.d(TAG,"write: write called");
        //perform the write
        try{
            mConnectedThread.write(out);
        }
        catch(NullPointerException e)
        {

            CharSequence text = "NullPointerException: Error";
            Log.d("BlueToothConnection"," Error writing to output stream");
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(mContext, text, duration);
            toast.show();
        }


    }
}
