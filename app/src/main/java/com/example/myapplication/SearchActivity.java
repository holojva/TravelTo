package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SearchActivity extends AppCompatActivity {
    String desc;
    String name;
    WebView mWebView;
    public static String stream(String stringy)  {
        String content = null;
        URLConnection connection = null;
        try {
            connection =  new URL(stringy).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scanner.useDelimiter("\\Z");
        content = scanner.next();
        scanner.close();
        return content;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        String[] items = new String[]{"Памятник", "Музей", "Ресторан", "Досуг", "Достопримечательность"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        mWebView = (WebView) findViewById(R.id.webView2);

        WebSettings webSettings = mWebView.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mWebView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            desc = extras.getString("q");
        }
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        name = signupSettings.getString("user_name", "none");

        mWebView.loadUrl("http://travelto-holojva.amvera.io/search?username="+name+"&q="+desc);
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
                        Intent i = new Intent(SearchActivity.this, MainMenuActivity.class);
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
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl("http://travelto-holojva.amvera.io/search?username="+name+"&q="+desc); // Reloads the WebView when activity comes back to foreground
    }
}