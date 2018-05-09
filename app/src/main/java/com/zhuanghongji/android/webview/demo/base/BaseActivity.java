package com.zhuanghongji.android.webview.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhuanghongji on 2018/5/6.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
