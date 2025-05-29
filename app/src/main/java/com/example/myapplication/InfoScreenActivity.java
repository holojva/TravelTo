package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoScreenActivity extends AppCompatActivity {
    String name;
    String desc;
    String addr;
    String img;
    String lat;
    String lon;
    String num;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            desc = extras.getString("desc");
            addr = extras.getString("addr");
            img = extras.getString("imagelink");
            lat = extras.getString("lat");
            lon = extras.getString("lon");
            num = extras.getString("num");
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(InfoScreenActivity.this);
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
            TextView latlon_tv = (TextView)findViewById(R.id.placeAddress13);
            name_tv.setText(name);
            desc_tv.setText(desc);
            addr_tv.setText(addr);
            latlon_tv.setText(lat+" "+lon);
        } else {
            name = sharedPreferences.getString("name", "unknown");
            desc = sharedPreferences.getString("desc", "unknown");
            lat = sharedPreferences.getString("lat", "unknown");
            lon = sharedPreferences.getString("lon", "unknown");
            img = sharedPreferences.getString("imagelink", "unknown");
            addr = sharedPreferences.getString("addr", "unknown");
            num = sharedPreferences.getString("num", "unknown");
        }
    }
}