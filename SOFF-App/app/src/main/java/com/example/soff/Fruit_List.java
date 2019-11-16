package com.example.soff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Fruit_List extends AppCompatActivity {
    ListView list;
    String[] strings;
    List<String> fruit_list = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit__list);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fruit_list);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(arrayAdapter);
        read_text_file();
    }
    public void read_text_file()
    {
        InputStream inputstream= null;
        try {
            inputstream = getApplicationContext().openFileInput("fruits.txt");

            if(inputstream != null){
                InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                BufferedReader bufferedReader = new BufferedReader(inputstreamreader);
                String receivestring = "";
                //StringBuilder stringBuilder = new StringBuilder();

                while( (receivestring = bufferedReader.readLine()) != null){
                    fruit_list.add(receivestring);
                    arrayAdapter.notifyDataSetChanged();
                }
                inputstream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void cleardata(View view)
    {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path,"fruits.txt");
        //FileOutputStream stream = new FileOutputStream(file);
        FileOutputStream stream = null;
        try {
            stream = openFileOutput("fruits.txt",0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try{
            stream.write("".getBytes());
            //stream.write("".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Fruit_List:", "Clearing File");
        //currentfruit = "";
    }
}