package com.zhuanghongji.android.webview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static final String DEFAULT_URL = "http://www.baidu.com/";

    private Toolbar mToolbar;

    private EditText mEditText;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setupToolbar();
        setupWebView();
        mWebView.loadUrl(DEFAULT_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mWebView.reload();
                break;

        }
        return true;
    }

    private void setupWebView() {
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);

        // 希望目标网页在当前 WebView 显示，而不是系统浏览器
        mWebView.setWebViewClient(new WebViewClient());
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initEvent() {
        mToolbar = findViewById(R.id.toolbar);
        mEditText = findViewById(R.id.edit_text);
        mWebView = findViewById(R.id.web_view);
    }

    private void initView() {

    }
}
