package com.zhuanghongji.android.webview.demo;

import android.app.Application;

import com.zhuanghongji.android.webview.demo.helper.MLog;

/**
 * Created by zhuanghongji on 2018/5/6.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.init(true);
    }
}
