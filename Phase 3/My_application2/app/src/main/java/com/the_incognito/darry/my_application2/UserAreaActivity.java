package com.the_incognito.darry.my_application2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Intent intent = getIntent();
        String name = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");

        TextView tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etEmail = (EditText) findViewById(R.id.etEmail);

        // Display user details
        String message = name + " welcome to your user area";
        tvWelcomeMsg.setText(message);
//        etEmail.setText(email);
        //etAge.setText(age + "");
    }
}
