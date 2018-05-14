package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import Fragment.Fragment_WebView;

public class CommunityActivity extends AppCompatActivity {
    private WebView luntanListview;
    String html;
    ViewGroup container;
    int webviewHeigh;
    Intent intent;
    TextView textView;
    Fragment_WebView.OnHeighChangerListener onHeighChangerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        intent = getIntent();
        html = intent.getStringExtra("html");

        showWebView();
    }

    private void showWebView() {
        luntanListview = findViewById(R.id.community_webview);
        // 设置WevView要显示的网页
        WebSettings webSettings = luntanListview.getSettings();
        //声明WebSettings子

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

//支持插件
        //webSettings.setPluginsEnabled(true);

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8",null);
        luntanListview.setVerticalScrollBarEnabled(false);
        luntanListview.setVerticalScrollbarOverlay(false);
        luntanListview.setHorizontalScrollBarEnabled(false);
        luntanListview.setHorizontalScrollbarOverlay(false);
        //                luntanListview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条
        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        luntanListview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    public void clearWebViewResource(ViewGroup container, WebView webView) {
        if (webView != null) {
            webView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            if (container != null)
                container.removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
        }
    }

    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
                luntanListview.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
                webviewHeigh = (int) (height);
                if (onHeighChangerListener != null)
                    onHeighChangerListener.valueChanged(webviewHeigh);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }


}
