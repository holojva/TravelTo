package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MapChooseActivity extends AppCompatActivity {
    String name;
    String num;
    public static boolean isXml(InputStream input) {
        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
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
        setContentView(R.layout.map_choose_screen);
        WebView mWebView = (WebView) findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                        name = document.getDocumentElement().getElementsByTagName("name").item(0).getTextContent();
                        String desc = document.getDocumentElement().getElementsByTagName("desc").item(0).getTextContent();
                        String lon = document.getDocumentElement().getElementsByTagName("lon").item(0).getTextContent();
                        String lat = document.getDocumentElement().getElementsByTagName("lat").item(0).getTextContent();
                        String addr = document.getDocumentElement().getElementsByTagName("addr").item(0).getTextContent();
                        String imagelink = document.getDocumentElement().getElementsByTagName("imagelink").item(0).getTextContent();
                        num = document.getDocumentElement().getElementsByTagName("num").item(0).getTextContent();
                        Log.d("name", name);
                        Log.d("desc", desc);
                        Intent i = new Intent(MapChooseActivity.this, MainMenuActivity.class);
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
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        String name = signupSettings.getString("user_name", "none");
        mWebView.loadUrl("http://travelto-holojva.amvera.io?username="+name);
        String value="Hello world";
//        Intent i = new Intent(MapChooseActivity.this, MainMenuActivity.class);
//        i.putExtra("key",value);
//        startActivity(i);
    }
    public void newPoint(View view) {
        Intent intent = new Intent(this, NewPointActivity.class);
        startActivity(intent);
    }
    public void favourite(View view) {
        Intent intent = new Intent(this, FavouritesActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("num", num);
        startActivity(intent);
    }
    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        EditText desc = (EditText)findViewById(R.id.editTextText);
        String desc_s = desc.getText().toString();
        intent.putExtra("q", desc_s);
        startActivity(intent);
    }
}