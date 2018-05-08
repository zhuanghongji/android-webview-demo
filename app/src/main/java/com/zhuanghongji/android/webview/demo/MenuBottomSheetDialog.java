package com.zhuanghongji.android.webview.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by zhuanghongji on 2018/5/8.
 */

public class MenuBottomSheetDialog extends BottomSheetDialog {

    public static final String TAG = "MenuBottomSheetDialog";

    private Context mContext;

    private View mContentView;

//    private TextView tvFindText;
//    private TextView tvClearCache;

    // don't forget to set the OnMenuClickListener for this dialog
    private OnMenuClickListener mOnMenuClickListener;

    public MenuBottomSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MenuBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = View.inflate(mContext, R.layout.dialog_menu, null);
        initEvent();
        setContentView(mContentView);
    }

    private void initEvent() {
        setOnClickListenerByViewId(R.id.tv_find_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onFindText(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_clear_cache, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onClearCache(v);
            }
        });

        SwitchCompat scSetNetworkAvailable = mContentView.findViewById(R.id.sc_set_network_available);
        scSetNetworkAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnMenuClickListener.onNetworkAvailable(isChecked);
            }
        });

        SwitchCompat scSetDebugEnabled = mContentView.findViewById(R.id.sc_set_web_contents_debugging_enabled);
        scSetDebugEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnMenuClickListener.onDebugEnabled(isChecked);
            }
        });

        setOnClickListenerByViewId(R.id.tv_copy_back_forward_list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onCopyBackForwardList(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_clear_history, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onClearHistory(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_clear_form_data, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onClearFormData(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_document_has_images, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onDocumentHasImages(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_request_focus_node_href, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onRequestFocusNodeHref(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_request_image_ref, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onRequestImageHref(v);
            }
        });

        setOnClickListenerByViewId(R.id.tv_clear_client_cert_preferences, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuClickListener.onClearClientCertPreferences(v);
            }
        });
    }

    private void setOnClickListenerByViewId(@IdRes int viewId, View.OnClickListener listener) {
        mContentView.findViewById(viewId).setOnClickListener(listener);
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {
        void onFindText(View view);

        void onClearCache(View view);

        void onNetworkAvailable(boolean available);

        void onDebugEnabled(boolean enable);

        void onCopyBackForwardList(View v);

        void onClearHistory(View v);

        void onClearFormData(View v);

        void onDocumentHasImages(View v);

        void onRequestFocusNodeHref(View v);

        void onRequestImageHref(View v);

        void onClearClientCertPreferences(View v);
    }
}
