package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewsActivity extends AppCompatActivity {
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_view);
        WebView mWebView = (WebView) findViewById(R.id.webView2);
        WebSettings webSettings = mWebView.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.zoomOut();
        mWebView.zoomOut();
        mWebView.zoomOut();
        mWebView.zoomOut();
        webSettings.setJavaScriptEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            num = extras.getString("num");
            SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = signupSettings.edit();
            prefEditor.putString("num", num);
        } else {
            SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
            num = signupSettings.getString("num", "none");
        }
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        String name = signupSettings.getString("user_name", "none");
        mWebView.loadUrl("http://travelto-holojva.amvera.io/view_reviews?place_id="+num+"&username="+name);
    }
    public void create_review(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("num", num);
        startActivity(intent);
    }
}