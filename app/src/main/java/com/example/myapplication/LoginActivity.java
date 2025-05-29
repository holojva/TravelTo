package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
    }
    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    public void login(View view) {
        Intent intent = new Intent(this, MapChooseActivity.class);
        EditText name = (EditText)findViewById(R.id.plain_text_input2);
        String name_s = name.getText().toString();
        EditText desc = (EditText)findViewById(R.id.plain_text_input);
        String desc_s = desc.getText().toString();
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = signupSettings.edit();
        prefEditor.putString("user_name", desc_s);
        prefEditor.putString("email", name_s);
        prefEditor.commit();
        startActivity(intent);
    }
}