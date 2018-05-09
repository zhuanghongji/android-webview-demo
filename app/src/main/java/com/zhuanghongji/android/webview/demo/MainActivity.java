package com.zhuanghongji.android.webview.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zhuanghongji.android.webview.demo.base.BaseActivity;
import com.zhuanghongji.android.webview.demo.component.FindTextBar;
import com.zhuanghongji.android.webview.demo.component.MenuBottomSheetDialog;
import com.zhuanghongji.android.webview.demo.helper.MLog;
import com.zhuanghongji.android.webview.demo.helper.Utils;

/**
 * WebView 相关 API 测试唯一页面
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

//    private static final String DEFAULT_URL = "http://www.baidu.com/";
    private static final String DEFAULT_URL = "file:///android_asset/Apps.html";

    private Context mContext;

    private Toolbar mToolbar;

    private FindTextBar mFindTextBar;

    private ProgressBar mProgressBar;

    private WebView mWebView;

    private ImageView ivGoBack;
    private ImageView ivGoForward;
    private ImageView ivMenu;
    private ImageView ivHelp;
    private ImageView ivApps;

    private MenuBottomSheetDialog.OnMenuClickListener mOnBottomMenuClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
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
            case R.id.action_page_up:
                mWebView.pageUp(false);
                break;
            case R.id.action_page_down:
                mWebView.pageDown(false);
                break;
            case R.id.action_page_top:
                mWebView.pageUp(true);
                break;
            case R.id.action_page_bottom:
                mWebView.pageDown(true);
                break;
            case R.id.action_zoom_in:
                mWebView.zoomIn();
                break;
            case R.id.action_zoom_out:
                mWebView.zoomOut();
                break;
        }
        return true;
    }

    private void setupWebView() {
//        setupWebSettings();
        setupWebViewClient();
        setupWebChromeClient();
    }

    private void setupWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public Bitmap getDefaultVideoPoster() {
                // <video /> 控件在未播放时，会展示为一张海报图，HTML中可通过它的'poster'属性来指定。
                // 如果未指定'poster'属性，则通过此方法提供一个默认的海报图。
                MLog.i(TAG, "getDefaultVideoPoster");
                return super.getDefaultVideoPoster();
            }

            @Override
            public View getVideoLoadingProgressView() {
                MLog.i(TAG, "当全屏的视频正在缓冲时，此方法返回一个占位视图(比如旋转的菊花)");
                return super.getVideoLoadingProgressView();
            }

            @Override
            public void getVisitedHistory(ValueCallback<String[]> callback) {
                super.getVisitedHistory(callback);
                MLog.i(TAG, "获得所有访问历史项目的列表，用于链接着色");
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                MLog.i(TAG, "通知应用关闭窗口");
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                MLog.i(TAG, "接收JavaScript控制台消息 consoleMessage = %s", consoleMessage);
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                MLog.i(TAG, "通知应用打开新窗口 isDialog = %s, isUserGesture = %s, resultMsg = %s",
                        isDialog, isUserGesture, resultMsg);
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
                MLog.i(TAG, "onGeolocationPermissionsHidePrompt");
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                // 指定源的网页内容在没有设置权限状态下尝试使用地理位置API。
                // 从API24开始，此方法只为安全的源(https)调用，非安全的源会被自动拒绝
                MLog.i(TAG, "onGeolocationPermissionsShowPrompt");
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                MLog.i(TAG, "通知应用当前页退出了全屏模式，此时应用必须隐藏之前显示的自定义 View");
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                MLog.i(TAG, "显示一个alert对话框");
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                MLog.i(TAG, "显示一个confirm对话框");
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                MLog.i(TAG, "显示一个prompt对话框");
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                MLog.i(TAG, "显示一个对话框让用户选择是否离开当前页面");
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                MLog.i(TAG, "通知应用网页内容申请访问指定资源的权限(该权限未被授权或拒绝)");
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
                MLog.i(TAG, "通知应用权限的申请被取消，隐藏相关的 UI");
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                MLog.i(TAG, "接收监听页面加载进度 newProgress = %s", newProgress);
                mProgressBar.setProgress(newProgress);
                mProgressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                MLog.i(TAG, "通知应用当前页进入了全屏模式，此时应用必须显示一个包含网页内容的自定义 View");
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                MLog.i(TAG, "为'<input type=\"file\" />'显示文件选择器，返回false使用默认处理");
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                MLog.i(TAG, "接收页面图标");
                mToolbar.setNavigationIcon(new BitmapDrawable(icon));
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                MLog.i(TAG, "接收页面标题 title = %s", title);
                mToolbar.setTitle(title);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                MLog.i(TAG, "Android 中处理 Touch Icon 的方案");
                // http://droidyue.com/blog/2015/01/18/deal-with-touch-icon-in-android/index.html
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
                MLog.i(TAG, "请求获取取焦点");
            }
        });
    }

    private void setupWebViewClient() {
        // 希望目标网页在当前 WebView 显示，而不是系统浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                // 通知应用可以将当前的url存储在数据库中，意味着当前的访问url已经生效并被记录在内核当中。
                // 此方法在网页加载过程中只会被调用一次，网页前进后退并不会回调这个函数。
                MLog.i(TAG, "doUpdateVisitedHistory url = %s, isReload = %s", url, isReload);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                MLog.i(TAG, "是否重新提交表单，默认不重发 dontResend = %s, resend = %s",
                        dontResend, resend);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                MLog.i(TAG, "将要加载资源 url = %s", url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                // 这个回调添加于API23，仅用于主框架的导航
                // 通知应用导航到之前页面时，其遗留的WebView内容将不再被绘制。
                // 这个回调可以用来决定哪些WebView可见内容能被安全地回收，以确保不显示陈旧的内容
                // 它最早被调用，以此保证WebView.onDraw不会绘制任何之前页面的内容，随后绘制背景色或需要加载的新内容。
                // 当HTTP响应body已经开始加载并体现在DOM上将在随后的绘制中可见时，这个方法会被调用。
                // 这个回调发生在文档加载的早期，因此它的资源(css,和图像)可能不可用。
                // 如果需要更细粒度的视图更新，查看 postVisualStateCallback(long, WebView.VisualStateCallback).
                // 请注意这上边的所有条件也支持 postVisualStateCallback(long ,WebView.VisualStateCallback)
                MLog.i(TAG, "onPageCommitVisible url = %s", url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                MLog.i(TAG, "页面开始加载 url = %s, favicon = %s", url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MLog.i(TAG, "页面完成加载 url = %s", url);
                updateBottomButtonsState(url);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                // 此方法添加于API21，在UI线程被调用
                // 处理SSL客户端证书请求，必要的话可显示一个UI来提供KEY。
                // 有三种响应方式：proceed()/cancel()/ignore()，默认行为是取消请求
                // 如果调用proceed()或cancel()，Webview 将在内存中保存响应结果且对相同的"host:port"不会再次调用 onReceivedClientCertRequest
                // 多数情况下，可通过KeyChain.choosePrivateKeyAlias启动一个Activity供用户选择合适的私钥
                MLog.i(TAG, "onReceivedClientCertRequest request = %s", request);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                MLog.i(TAG, "主框架加载资源时出错 errorCode = %s, description = %s, failingUrl = %s",
                        errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // 通常意味着连接不到服务器
                // 由于所有资源加载错误都会调用此方法，所以此方法应尽量逻辑简单
                MLog.i(TAG, "主框架加载资源时出错 request = %s, error = %s", request, error);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                MLog.i(TAG, "通知应用有个已授权账号自动登陆了 realm = %s, account = %s, args = %s",
                        realm, account, args);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                MLog.i(TAG, " 处理HTTP认证请求，默认行为是取消请求 host = %s, realm = %s", host, realm);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 此方法添加于API23
                // 在加载资源(iframe,image,js,css,ajax...)时收到了 HTTP 错误(状态码>=400)
                MLog.i(TAG, "onReceivedHttpError request = %s, errorResponse = %s",
                        request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                // 加载资源时发生了一个SSL错误，应用必需响应(继续请求或取消请求)
                // 处理决策可能被缓存用于后续的请求，默认行为是取消请求
                MLog.i(TAG, "onReceivedSslError error = %s", error);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                MLog.i(TAG, "通知应用页面缩放系数变化 oldScale = %s, newScale = %s", oldScale, newScale);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                super.onUnhandledKeyEvent(view, event);
                // 处理未被WebView消费的按键事件
                // WebView总是消费按键事件，除非是系统按键或shouldOverrideKeyEvent返回true
                // 此方法在按键事件分派时被异步调用
                MLog.i(TAG, "onUnhandledKeyEvent event = %s", event);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                // 此方法废弃于API21，调用于非UI线程
                // 拦截资源请求并返回响应数据，返回null时WebView将继续加载资源
                // 注意：API21以下的AJAX请求会走onLoadResource，无法通过此方法拦截
                MLog.i(TAG, "shouldInterceptRequest url = %s", url);
                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // 此方法添加于API21，调用于非UI线程
                // 拦截资源请求并返回数据，返回null时WebView将继续加载资源
                MLog.i(TAG, "shouldInterceptRequest url = %s, method = %s, headers = %s",
                        request.getUrl(), request.getMethod(), request.getRequestHeaders());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                // 如果返回true，WebView不处理该事件，否则WebView会一直处理，默认返回false
                MLog.i(TAG, " 给应用一个机会处理按键事件 event = %s", event);
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截页面加载，返回 true 表示宿主 app 拦截并处理了该 url，否则返回 false 由当前 WebView 处理
                // 此方法在 API 24 被废弃，不处理 POST 请求
                MLog.i(TAG, "shouldOverrideUrlLoading url = %s", url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 拦截页面加载，返回 true 表示宿主 app 拦截并处理了该 url，否则返回 false 由当前 WebView 处理
                // 此方法添加于 API24，不处理 POST 请求，可拦截处理子 frame 的非 http 请求
                MLog.i(TAG, "shouldOverrideUrlLoading url = %s, method = %s, headers = %s",
                        request.getUrl(), request.getMethod(), request.getRequestHeaders());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void setupWebSettings() {
        WebSettings settings = mWebView.getSettings();

        // 启用HTML5 DOM storage API，默认值 false
        settings.setDomStorageEnabled(true);

        // 启用Web SQL Database API，这个设置会影响同一进程内的所有WebView，默认值 false
        // 此API已不推荐使用，参考：https://www.w3.org/TR/webdatabase/
        settings.setDatabaseEnabled(true);

        // 启用Application Caches API，必需设置有效的缓存路径才能生效，默认值 false
        // 此API已废弃，参考：https://developer.mozilla.org/zh-CN/docs/Web/HTML/Using_the_application_cache
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mContext.getCacheDir().getAbsolutePath());

        // 定位(location)
        settings.setGeolocationEnabled(true);

        // 是否保存表单数据
        settings.setSaveFormData(true);

        // 是否当webview调用requestFocus时为页面的某个元素设置焦点，默认值 true
        settings.setNeedInitialFocus(true);

        // 是否支持viewport属性，默认值 false
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);

        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(true);

        // 布局算法
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        // 是否支持Javascript，默认值false
        settings.setJavaScriptEnabled(true);

        // 是否支持多窗口，默认值false
        settings.setSupportMultipleWindows(false);

        // 是否可用Javascript(window.open)打开窗口，默认值 false
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        // 资源访问
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true

        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);

        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);

        // 资源加载
        settings.setLoadsImagesAutomatically(true); // 是否自动加载图片
        settings.setBlockNetworkImage(false);       // 禁止加载网络图片
        settings.setBlockNetworkLoads(false);       // 禁止加载所有网络资源

        // 缩放(zoom)
        settings.setSupportZoom(true);          // 是否支持缩放
        settings.setBuiltInZoomControls(false); // 是否使用内置缩放机制
        settings.setDisplayZoomControls(true);  // 是否显示内置缩放控件

        // 默认文本编码，默认值 "UTF-8"
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDefaultFontSize(16);        // 默认文字尺寸，默认值16，取值范围1-72
        settings.setDefaultFixedFontSize(16);   // 默认等宽字体尺寸，默认值16
        settings.setMinimumFontSize(8);         // 最小文字尺寸，默认值 8
        settings.setMinimumLogicalFontSize(8);  // 最小文字逻辑尺寸，默认值 8
        settings.setTextZoom(100);              // 文字缩放百分比，默认值 100

        // 字体
        settings.setStandardFontFamily("sans-serif");   // 标准字体，默认值 "sans-serif"
        settings.setSerifFontFamily("serif");           // 衬线字体，默认值 "serif"
        settings.setSansSerifFontFamily("sans-serif");  // 无衬线字体，默认值 "sans-serif"
        settings.setFixedFontFamily("monospace");       // 等宽字体，默认值 "monospace"
        settings.setCursiveFontFamily("cursive");       // 手写体(草书)，默认值 "cursive"
        settings.setFantasyFontFamily("fantasy");       // 幻想体，默认值 "fantasy"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 用户是否需要通过手势播放媒体(不会自动播放)，默认值 true
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 是否在离开屏幕时光栅化(会增加内存消耗)，默认值 false
            settings.setOffscreenPreRaster(false);
        }
        if (Utils.isNetworkConnected(mContext)) {
            // 根据cache-control决定是否从网络上取数据
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            // 没网，离线加载，优先加载缓存(即使已经过期)
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // deprecated
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setDatabasePath(mContext.getDir("database", Context.MODE_PRIVATE).getPath());
        settings.setGeolocationDatabasePath(mContext.getFilesDir().getPath());
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_launcher_round);
    }

    private void initEvent() {
        mWebView.setFindListener(mFindTextBar.getFindListener());

        mFindTextBar.setOnFindTextListener(new FindTextBar.OnFindTextListener() {
            @Override
            public void onCancel() {
                mFindTextBar.setVisibility(View.GONE);
                mWebView.clearMatches();
            }

            @Override
            public void onTextChange(@NonNull String text) {
                mWebView.findAllAsync(text);
            }

            @Override
            public void onPreOne() {
                mWebView.findNext(false);
            }

            @Override
            public void onNextOne() {
                mWebView.findNext(true);
            }
        });

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

        ivGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuBottomSheetDialog dialog = new MenuBottomSheetDialog(mContext);
                dialog.setOnMenuClickListener(mOnBottomMenuClickListener);
                dialog.show();
            }
        });

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mWebView.getUrl();
                String originalUrl = mWebView.getOriginalUrl();
                String message = "url = " + url + "\n\noriginalUrl = " + originalUrl;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("当前网页信息")
                        .setMessage(message)
                        .setPositiveButton("确定", null);
                builder.create().show();
            }
        });

        ivApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(DEFAULT_URL);
            }
        });

        mOnBottomMenuClickListener = new MenuBottomSheetDialog.OnMenuClickListener() {
            @Override
            public void onFindText(View view) {
                MLog.i(TAG, "查找下一个匹配的字符串");
                mFindTextBar.setVisibility(View.VISIBLE);
                mFindTextBar.focus();
            }

            @Override
            public void onClearCache(View view) {
                mWebView.clearCache(false);
            }

            @Override
            public void onNetworkAvailable(boolean available) {
                mWebView.setNetworkAvailable(available);
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDebugEnabled(boolean enable) {
                WebView.setWebContentsDebuggingEnabled(enable);
            }

            @Override
            public void onCopyBackForwardList(View v) {
                mWebView.copyBackForwardList();
            }

            @Override
            public void onClearHistory(View v) {
                mWebView.clearHistory();
            }

            @Override
            public void onClearFormData(View v) {
                mWebView.clearFormData();
            }

            @Override
            public void onDocumentHasImages(View v) {
                // 查询结果将被发送到 msg.getTarget()
                // 如果包含图片，msg.arg1 为 1，否则为 0
                Message message = new Message();
                mWebView.documentHasImages(message);
                MLog.i(TAG, "查询文档中是否有图片 message = %s", message);
            }

            @Override
            public void onRequestFocusNodeHref(View v) {
                // 查询结果将被发送到msg.getTarget()
                // msg.getData()中的url是锚点的href属性，title是锚点的文本，src是图像的src接
                Message message = new Message();
                mWebView.requestFocusNodeHref(message);
                MLog.i(TAG, "请求最近轻点的 锚点/图像 元素的 URL message = %s", message);
            }

            @Override
            public void onRequestImageHref(View v) {
                // 查询结果将被发送到msg.getTarget()
                // msg.getData()中的url是图像链接
                Message message = new Message();
                mWebView.requestImageRef(message);
                MLog.i(TAG, "请求最近触摸的图像元素的 URL message = %s", message);
            }

            @Override
            public void onClearClientCertPreferences(View v) {
                // TODO
            }
        };
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mFindTextBar = findViewById(R.id.find_text_bar);
        mProgressBar = findViewById(R.id.progress_bar);
        mWebView = findViewById(R.id.web_view);

        ivGoBack = findViewById(R.id.iv_go_back);
        ivGoForward = findViewById(R.id.iv_go_forward);
        ivMenu = findViewById(R.id.iv_menu);
        ivHelp = findViewById(R.id.iv_help);
        ivApps = findViewById(R.id.iv_apps);
    }

    /**
     * 更新底部操作按钮的 enable 状态
     * @param url 当前页面的 url
     */
    private void updateBottomButtonsState(String url) {
        ivGoBack.setEnabled(mWebView.canGoBack());
        ivGoForward.setEnabled(mWebView.canGoForward());
        ivApps.setEnabled(!DEFAULT_URL.equals(url));
    }
}
