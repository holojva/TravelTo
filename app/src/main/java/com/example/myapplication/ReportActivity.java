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

public class ReportActivity extends AppCompatActivity {
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_view);
    }
    public void send(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        WebView mWebView = (WebView) findViewById(R.id.webVieww);
        WebSettings webSettings = mWebView.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mWebView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            num = extras.getString("num");
            SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = signupSettings.edit();
            prefEditor.putString("num", num);
            prefEditor.commit();
        } else {
            SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
            num = signupSettings.getString("num", "none");
        }
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        String name = signupSettings.getString("user_name", "none");
        EditText rating = (EditText)findViewById(R.id.plain_text_input6);
        String rating_s = rating.getText().toString();
        EditText text = (EditText)findViewById(R.id.plain_text_input3);
        String text_s = text.getText().toString();
        Log.d("hi", "http://travelto-holojva.amvera.io/submit_review?place_id="+num+"&username="+name+"&review_text="+text_s+"&review_num="+rating_s);
        mWebView.loadUrl("http://travelto-holojva.amvera.io/submit_review?place_id="+num+"&username="+name+"&review_text="+text_s+"&review_num="+rating_s);
        startActivity(intent);
    }
}
