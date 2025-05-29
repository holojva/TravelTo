package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    String name;
    String desc;
    String addr;
    String img;
    String lat;
    String lon;
    String num;
    WebView nWebView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            desc = extras.getString("desc");
            addr = extras.getString("addr");
            img = extras.getString("imagelink");
            lat = extras.getString("lat");
            lon = extras.getString("lon");
            num = extras.getString("num");
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("desc", desc);
            editor.putString("lat", lat);
            editor.putString("lon", lon);
            editor.putString("addr", addr);
            editor.putString("imagelink", img);
            editor.putString("num", num);
            editor.commit();
            WebView mWebView = (WebView) findViewById(R.id.web_view_img);
            WebSettings webSettings = mWebView.getSettings();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.zoomOut();
            mWebView.zoomOut();
            mWebView.zoomOut();
            mWebView.zoomOut();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl("http://travelto-holojva.amvera.io/image/"+num);
            TextView name_tv = (TextView)findViewById(R.id.placeName);
            TextView desc_tv = (TextView)findViewById(R.id.placeAddress);
            TextView addr_tv = (TextView)findViewById(R.id.placeAddress5);
            name_tv.setText(name);
            desc_tv.setText(desc);
            addr_tv.setText(addr);
            //The key argument here must match that used in the other activity
        } else {
            name = sharedPreferences.getString("name", "unknown");
            desc = sharedPreferences.getString("desc", "unknown");
            lat = sharedPreferences.getString("lat", "unknown");
            lon = sharedPreferences.getString("lon", "unknown");
            img = sharedPreferences.getString("imagelink", "unknown");
            addr = sharedPreferences.getString("addr", "unknown");
            num = sharedPreferences.getString("num", "unknown");
            WebView mWebView = (WebView) findViewById(R.id.web_view_img);
            WebSettings webSettings = mWebView.getSettings();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.zoomOut();
            mWebView.zoomOut();
            mWebView.zoomOut();
            mWebView.zoomOut();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl("http://travelto-holojva.amvera.io/image/"+num);
            TextView name_tv = (TextView)findViewById(R.id.placeName);
            TextView desc_tv = (TextView)findViewById(R.id.placeAddress);
            TextView addr_tv = (TextView)findViewById(R.id.placeAddress5);
            name_tv.setText(name);
            desc_tv.setText(desc);
            addr_tv.setText(addr);
        }
    }
    public void userProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    public void infoScreen(View view) {
        Intent i = new Intent(this, InfoScreenActivity.class);
        i.putExtra("name", name);
        i.putExtra("desc", desc);
        i.putExtra("lat", lat);
        i.putExtra("lon", lon);
        i.putExtra("addr", addr);
        i.putExtra("imagelink", img);
        i.putExtra("num", num);
        startActivity(i);
    }
//    @Override
//    public void onBackPressed() {
//        // Instead of going back, start a new activity
//        super.onBackPressed();
//        Intent intent = new Intent(this, MapChooseActivity.class);
//
//        // Clear back stack so user can't return to this activity
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        startActivity(intent);
//        finish(); // Optional: finish current activity if needed
//    }
    public void reviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("num", num);
        startActivity(intent);
    }
    public void report(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }
    public void favourite(View view) {
        Intent intent = new Intent(this, FavouritesActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("num", num);
        startActivity(intent);
    }
    public void fav(View view) {
        WebView WebViewwww = (WebView) findViewById(R.id.webViewwww);
        WebSettings webSettings = WebViewwww.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        WebViewwww.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        String username = signupSettings.getString("user_name", "none");
        Log.d("hi", "http://travelto-holojva.amvera.io/toggle_favorite?username="+username+"&place_id="+num);
        WebViewwww.loadUrl("http://travelto-holojva.amvera.io/toggle_favorite?username="+username+"&place_id="+num);
    }
}