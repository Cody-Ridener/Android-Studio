package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText minInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void convert(View view) {
        minInput = (EditText) findViewById(R.id.minInput);
        TextView secsTxt = findViewById(R.id.txtSecs);
        String txt = minInput.getText().toString();

        try{
            double num = Double.parseDouble(txt);
            num = num*60;
            secsTxt.setText(Double.toString(num));
        }catch(NumberFormatException err){
            Toast.makeText(this, "Please input a number", Toast.LENGTH_SHORT).show();
            secsTxt.setText("Error");
        }
    }
}
