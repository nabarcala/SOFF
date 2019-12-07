package com.example.soff;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DatabaseCommunication {
    List<String> stringList = new ArrayList<String>();

    // Find the fruit data based on the fruit name
    public List<String> runQuery(String fruitName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FruitData");

        // Find fruits whose name is equal to the given fruit name
        query.whereEqualTo("fruitName", fruitName);

        try{
            List<ParseObject> results = query.find();
            for(ParseObject result : results){
                stringList.add("Amount per: " + result.getString("amountPer"));
                stringList.add("Scientific Name: " + result.getString("scientificName"));
                stringList.add("Calories: " + result.getString("calories"));
                stringList.add("Sugar: " + result.getString("sugar"));
                stringList.add("Protein: " + result.getString("protein"));
            }
            Log.d("Run Query", "done: " + fruitName);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return stringList;
    }
}
