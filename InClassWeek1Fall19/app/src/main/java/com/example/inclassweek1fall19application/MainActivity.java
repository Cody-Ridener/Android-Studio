package com.example.inclassweek1fall19application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1;
        btn1 = findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void pressCallback(View view) {
       Toast.makeText(this, "you clicked me", Toast.LENGTH_SHORT).show();
    }
}
