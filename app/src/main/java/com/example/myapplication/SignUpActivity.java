package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
    }
    public void signUp(View view) {
        Log.d("Button", "Clicked");
        Intent intent = new Intent(SignUpActivity.this, MapChooseActivity.class);
        Log.d("Button", "Clicked2");
        EditText name = (EditText)findViewById(R.id.plain_text_input2);
        String name_s = name.getText().toString();
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = signupSettings.edit();
        prefEditor.putString("user_name", name_s);
        prefEditor.commit();
        WebView mWebView = (WebView) findViewById(R.id.webViewww);
        WebSettings webSettings = mWebView.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mWebView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://travelto-holojva.amvera.io/set_user?username="+name_s);
        startActivity(intent);
    }
    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}