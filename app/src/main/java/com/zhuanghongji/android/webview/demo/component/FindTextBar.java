package com.zhuanghongji.android.webview.demo.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhuanghongji.android.webview.demo.R;
import com.zhuanghongji.android.webview.demo.helper.MLog;

/**
 * "网页查找" 操作栏
 */

public class FindTextBar extends FrameLayout {

    public static final String TAG = "FindTextBar";

    private Context mContext;

    private TextView tvCancel;
    private EditText etText;
    private TextView tvIndexTotal;
    private TextView tvPreOne;
    private TextView tvNextOne;

    private boolean mIsDoneCounting = false;

    private OnFindTextListener mOnFindTextListener;

    private WebView.FindListener mFindListener;

    public FindTextBar(@NonNull Context context) {
        this(context, null);
    }

    public FindTextBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FindTextBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_find_text, this, true);
        initView();
        initEvent();
    }

    private void initEvent() {
        mFindListener = new WebView.FindListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFindResultReceived(int activeMatchOrdinal,
                                             int numberOfMatches, boolean isDoneCounting) {
                MLog.i(TAG, "网页查找结果回调：activeMatchOrdinal = %s, " +
                                "numberOfMatches = %s, isDoneCounting = %s",
                        activeMatchOrdinal, numberOfMatches, isDoneCounting);
                mIsDoneCounting = isDoneCounting;
                if (mIsDoneCounting) {
                    tvIndexTotal.setVisibility(VISIBLE);
                    int index = activeMatchOrdinal + 1;
                    tvIndexTotal.setText(index + "/" + numberOfMatches);
                } else {
                    tvIndexTotal.setVisibility(GONE);
                }
            }
        };

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFindTextListener != null) {
                    mOnFindTextListener.onCancel();
                }
                etText.setText("");
                etText.clearFocus();
            }
        });

        etText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard();
                } else {
                    closeKeyboard();
                }
            }
        });

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (mOnFindTextListener != null) {
                    mOnFindTextListener.onTextChange(text);
                }
            }
        });

        tvPreOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFindTextListener != null) {
                    mOnFindTextListener.onPreOne();
                }
            }
        });

        tvNextOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFindTextListener != null) {
                    mOnFindTextListener.onNextOne();
                }
            }
        });
    }

    private void initView() {
        tvCancel = findViewById(R.id.tv_cancel);
        etText = findViewById(R.id.et_find_text);
        tvIndexTotal = findViewById(R.id.tv_index_total);
        tvPreOne = findViewById(R.id.tv_pre_one);
        tvNextOne = findViewById(R.id.tv_next_one);
    }

    public void focus() {
        etText.requestFocus();
        tvIndexTotal.setVisibility(GONE);
    }

    private void showKeyboard() {
//        ((Activity) mContext).getWindow()
//                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//        InputMethodManager manager = (InputMethodManager) mContext
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (manager != null) {
//            manager.showSoftInput(etText, 0);
//        }
    }

    private void closeKeyboard() {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(etText.getWindowToken(), 0);
        }
    }

    public WebView.FindListener getFindListener() {
        return mFindListener;
    }

    public void setOnFindTextListener(OnFindTextListener onFindTextListener) {
        mOnFindTextListener = onFindTextListener;
    }

    public interface OnFindTextListener {
        void onCancel();
        void onTextChange(@NonNull String text);
        void onPreOne();
        void onNextOne();
    }
}
