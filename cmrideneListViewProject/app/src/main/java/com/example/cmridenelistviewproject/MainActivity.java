package com.example.cmridenelistviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.Map;
import java.util.function.BiConsumer;

public class MainActivity extends AppCompatActivity {
    // A boolean used to determine when clicking on list item will delete it.
    boolean delete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Runs the function to populate the list.
        populateListView();
    }
    private void populateListView(){
        // Imposrts the preferences from the shared preference with the name "ShoppingList"
        SharedPreferences myprefs = getSharedPreferences("ShoppingList", MODE_PRIVATE);
        // Get the map of the entire shopping list that was inputted into the Shared Preferences
        Map<String, ?> shoppingList = myprefs.getAll();
        // Creates a new array that will store the values of the map.
        String[] shopList = new String[shoppingList.size()];
        // An iterator for the for loop.
        int i = 0;
        // A for loop that iterates over all of the entries in the map.
        for(Map.Entry<String, ?> entry : shoppingList.entrySet()){
            shopList[i] = entry.getKey() + " Amount: " + (String) entry.getValue();
        }
    }


    public void setDeleteTrue(View view) {
        // Sets the delete value to true.
        delete = true;
    }

    public void addNewItem(View view) {
        startActivity(new Intent(MainActivity.this, AddItems.class));
    }
}
