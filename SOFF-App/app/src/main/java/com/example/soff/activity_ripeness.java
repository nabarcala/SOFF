package com.example.soff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class activity_ripeness extends AppCompatActivity {
    TextView results;
    String ripefruit = ""; // fruit chosen in drop down
    String ripeness = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripeness);

        results = (TextView) findViewById(R.id.results);

        // Get the result with the receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(ripeReceiver, new IntentFilter("incomingRipeness"));

        //code for the dropdown menu for ripe fruits
        Spinner ripeSpinner = (Spinner) findViewById(R.id.ripe_fruit_spin);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(activity_ripeness.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ripe_fruits));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ripeSpinner.setAdapter(myAdapter);

        // Change the fruit name when selected
        ripeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                String selectedText = adapterView.getSelectedItem().toString();
                // Save the selected fruit as the current fruit to check ripeness
                ripefruit = selectedText;
                // Only display the toast when it changes from default
                if(i > 0){
                    Toast.makeText(activity_ripeness.this, selectedText, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String selectedText = "Avocado";
                Toast.makeText(activity_ripeness.this, selectedText, Toast.LENGTH_SHORT).show();
                ripefruit = selectedText;
            }
        });
    }

    // Get the result of the colorsensor and display it
    BroadcastReceiver ripeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String encodedmessage = intent.getStringExtra("theRipeness");

            // Set text on main screem
            Log.d("activity_ripeness: ",encodedmessage);

            // Display the result
            results.setText(encodedmessage);
            //saves fruit and ripeness state to textfile;
            ripeness = encodedmessage;
            try {
                save_to_textfile(ripefruit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    // Start the colorsensor to find is ripe or not
    public void colorsensor(View view){
        if (ripefruit.equals("Avocado"))
        {
            String command = "checkripe";
            byte[] bytes = command.toString().getBytes(Charset.defaultCharset());
            ((Startup)this.getApplicationContext()).b.write(bytes);

            Context context = getApplicationContext();
            //CharSequence text = ((Startup)this.getApplicationContext()).b.getGetDeviceName();
            CharSequence text = "Checking ripeness";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            results.setText("Checking fruit...");
            ripeness = "";
        }
        else if(ripefruit.equals("Banana"))
        {
            String command = "checkbanana";
            byte[] bytes = command.toString().getBytes(Charset.defaultCharset());
            ((Startup)this.getApplicationContext()).b.write(bytes);

            Context context = getApplicationContext();
            //CharSequence text = ((Startup)this.getApplicationContext()).b.getGetDeviceName();
            CharSequence text = "Checking ripeness";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            results.setText("Checking fruit...");
            ripeness = "";
        }


        //uncomment this to test saving to textfile without bluetooth
        //ripeness = "Green not ripe";
       // try {
         //   save_to_textfile(ripefruit);
        //} catch (IOException e) {
         //   e.printStackTrace();
        //}
    }
    public void save_to_textfile(String fruit) throws IOException {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path,"fruit_ripe.txt");
        //FileOutputStream stream = new FileOutputStream(file);
        FileOutputStream stream = openFileOutput("fruit_ripe.txt",MODE_APPEND);
        try{
            stream.write(fruit.getBytes());
            stream.write(" ".getBytes());
            //stream.write("".getBytes());
            stream.write(ripeness.getBytes());
            stream.write(" ".getBytes());
            LocalDate localDate = LocalDate.now();

            stream.write((DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate).getBytes()));
            stream.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            stream.close();
        }
        Log.d("activity_ripeness:", "Writing fruit name to file");
        //currentfruit = "";
    }
}
