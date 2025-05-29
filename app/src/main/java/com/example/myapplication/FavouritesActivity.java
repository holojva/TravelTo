package com.example.myapplication;

import static com.example.myapplication.MapChooseActivity.stream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_view);
        WebView mWebView = (WebView) findViewById(R.id.webView);
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
        String num;
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
        mWebView.loadUrl("http://travelto-holojva.amvera.io/favorites?username="+name);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                mWebView.loadUrl(url);

                try {
                    Log.d("hi", url);
                    URL url_t;
                    String response = stream(url);
//                String upToNCharacters = response.substring(0, Math.min(response.length(), 10));
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = null;
                    try {
                        builder = factory.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                    ByteArrayInputStream input = null;
                    response = response.replaceAll("[^\\x20-\\x7e]", "");
                    Log.d("hi", response);
                    try {
                        input = new ByteArrayInputStream(response.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    Document document = null;
                    document = builder.parse(input);
                    try {
                        String name = document.getDocumentElement().getElementsByTagName("name").item(0).getTextContent();
                        String desc = document.getDocumentElement().getElementsByTagName("desc").item(0).getTextContent();
                        String lon = document.getDocumentElement().getElementsByTagName("lon").item(0).getTextContent();
                        String lat = document.getDocumentElement().getElementsByTagName("lat").item(0).getTextContent();
                        String addr = document.getDocumentElement().getElementsByTagName("addr").item(0).getTextContent();
                        String imagelink = document.getDocumentElement().getElementsByTagName("imagelink").item(0).getTextContent();
                        String num = document.getDocumentElement().getElementsByTagName("num").item(0).getTextContent();
                        Log.d("name", name);
                        Log.d("desc", desc);
                        Intent i = new Intent(FavouritesActivity.this, MainMenuActivity.class);
                        i.putExtra("name", name);
                        i.putExtra("desc", desc);
                        i.putExtra("lat", lat);
                        i.putExtra("lon", lon);
                        i.putExtra("addr", addr);
                        i.putExtra("imagelink", imagelink);
                        i.putExtra("num", num);
                        startActivity(i);
                    }
                    catch (NullPointerException ignored){
                        ;
                    }
                } catch (IOException | SAXException e) {
                    ;
                }
                return false;
            }
        });
        webSettings.setJavaScriptEnabled(true);
    }
}