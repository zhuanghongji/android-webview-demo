package com.zhuanghongji.android.webview.demo;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by zhuanghongji on 2018/5/9.
 */

public class CustomJavascriptInterface {

    private Context mContext;

    public CustomJavascriptInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
