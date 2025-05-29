package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        String name = signupSettings.getString("user_name", "none");
        String email = signupSettings.getString("email", "none@none.com");
        TextView name_tv = (TextView)findViewById(R.id.placeName2);
        name_tv.setText(name);
        TextView email_tv = (TextView)findViewById(R.id.placeName3);
        email_tv.setText(email);
    }
}