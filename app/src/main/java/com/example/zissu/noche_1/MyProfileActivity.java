package com.example.zissu.noche_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        TextView tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etAge = (EditText) findViewById(R.id.etAge);

        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        String pass = intent.getExtras().get("pass").toString();

//        String username = intent.getStringExtra("userName");
//        String age = intent.getStringExtra("age");
//
//        String message = username + "welcome";
        tvWelcomeMsg.setText(email + " welcome");
        etUsername.setText(email);
        etAge.setText(pass + "");
    }
}
