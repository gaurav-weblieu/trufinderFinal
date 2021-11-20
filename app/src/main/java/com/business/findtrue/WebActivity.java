package com.business.findtrue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    WebView web_view_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        web_view_main = findViewById(R.id.web_view_main);

        String url = getIntent().getStringExtra("url");

        web_view_main.loadUrl(url);
        web_view_main.getSettings().setJavaScriptEnabled(true);
        web_view_main.setWebViewClient(new WebViewClient());
    }

    public void back(View view) {
        finish();
    }
}