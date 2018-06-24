package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import library.GetJsonData.GetWebView;
import library.GetJsonData.ReCountJsonData;

public class GoldFlowActivity extends AppCompatActivity {

    private WebView luntanListview;
    String html;
    ViewGroup container;
    String success, msg;
    String token;
    int count_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldflow);
        count_type = getIntent().getIntExtra("count_type", 0);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                html = new ReCountJsonData().setGoldFlow(count_type,token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWebView();
                    }
                });
            }
        }).start();

    }

    private void showWebView() {
        luntanListview = findViewById(R.id.goldflow_activity_webview);
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
        //  webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        luntanListview.setVerticalScrollBarEnabled(false);
        luntanListview.setVerticalScrollbarOverlay(false);
        luntanListview.setHorizontalScrollBarEnabled(false);
        luntanListview.setHorizontalScrollbarOverlay(false);
        //                luntanListview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条
        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        //webSettings.setAllowUniversalAccessFromFileURLs(true);
        luntanListview.clearCache(true);//清除暫存
        luntanListview.setWebChromeClient(new WebChromeClient());
        luntanListview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("URL", url);
                if (url.contains("js-call")) {
                    url = url.replace("js-call://setDeliver?", "");
                    String[] uuu = url.split("&");
                    try {
                        success = URLDecoder.decode(uuu[0], "UTF-8");
                        msg = URLDecoder.decode(uuu[1], "UTF-8");
                        Log.e("JS", "\n" + success + "\n" + msg);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    GoldFlowActivity.this.finish();
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("URL", url);
            }
        });
    }

    public void clearWebViewResource(ViewGroup container, WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }
}
