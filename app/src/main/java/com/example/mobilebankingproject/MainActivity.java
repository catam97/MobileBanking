package com.example.mobilebankingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText password, username;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

    }

    public void openActivity(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }





}
