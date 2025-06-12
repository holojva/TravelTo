package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NewPointActivity extends AppCompatActivity {
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
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }
    }
    class MyWebChromeClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            NewPointActivity.this.startActivityForResult(
                    Intent.createChooser(i, "Image Browser"),
                    FILECHOOSER_RESULTCODE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_point_screen);
        String[] items = new String[]{"Памятник", "Музей", "Ресторан", "Досуг", "Достопримечательность"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        Log.d("Button", "Clicked");
        Intent intent = new Intent(NewPointActivity.this, MapChooseActivity.class);
        Log.d("Button", "Clicked2");
        WebView mWebView = (WebView) findViewById(R.id.webView2);
        Log.d("hi", "test1");
        WebSettings webSettings = mWebView.getSettings();
        Log.d("hi", "tes21");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("hi", "test3");
        mWebView.setWebChromeClient(new MyWebChromeClient());
        webSettings.setJavaScriptEnabled(true);
        Log.d("hi", "test4");
        SharedPreferences signupSettings = getSharedPreferences("LoginSignUp", MODE_PRIVATE);
        Log.d("hi", "test5");
        String name = signupSettings.getString("user_name", "none");
        Log.d("hi", "http://travelto-holojva.amvera.io/new?username="+name);
        mWebView.loadUrl("http://travelto-holojva.amvera.io/new?username="+name);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                mWebView.loadUrl(url);

                try {
                    Log.d("hi", url);
                    URL url_t;
                    String response = stream(url);
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
                    Intent inq = new Intent(NewPointActivity.this, MapChooseActivity.class);
                    startActivity(inq);
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
                        Intent i = new Intent(NewPointActivity.this, MainMenuActivity.class);
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
                Intent inq = new Intent(NewPointActivity.this, MapChooseActivity.class);
                startActivity(inq);
                return false;
            }
        });

        webSettings.setJavaScriptEnabled(true);
    }
}