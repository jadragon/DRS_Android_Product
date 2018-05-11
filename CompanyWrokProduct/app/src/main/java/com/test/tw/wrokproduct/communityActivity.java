package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import Fragment.Fragment_WebView;

public class CommunityActivity extends AppCompatActivity {
    private WebView luntanListview;
    String html;
    ViewGroup container;
    int webviewHeigh;
    Intent intent;
    Fragment_WebView.OnHeighChangerListener onHeighChangerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        intent=getIntent();
        html=intent.getStringExtra("html");
        showWebView();
    }

    private void showWebView() {
        luntanListview = findViewById(R.id.community_webview);
        // 设置WevView要显示的网页
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                null);
        luntanListview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        // 设置可以支持缩放
        luntanListview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        //luntanListview.getSettings().setBuiltInZoomControls(true);
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        luntanListview.getSettings().setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        luntanListview.getSettings().setLoadWithOverviewMode(true);
        //自适应屏幕
        luntanListview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        luntanListview.setVerticalScrollBarEnabled(false);
        luntanListview.setVerticalScrollbarOverlay(false);
        luntanListview.setHorizontalScrollBarEnabled(false);
        luntanListview.setHorizontalScrollbarOverlay(false);
        //                luntanListview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条
        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        luntanListview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                luntanListview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
            }
        });
        luntanListview.addJavascriptInterface(this, "App");
        //        luntanListview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        //            @Override
        //            public void onFocusChange(View v, boolean hasFocus) {
        //                if (hasFocus) {
        //                    try {
        //                        // 禁止网页上的缩放
        //                        Field defaultScale = WebView.class
        //                                .getDeclaredField("mDefaultScale");
        //                        defaultScale.setAccessible(true);
        //                        defaultScale.setFloat(luntanListview, 1.0f);
        //                    } catch (SecurityException e) {
        //                        e.printStackTrace();
        //                    } catch (IllegalArgumentException e) {
        //                        e.printStackTrace();
        //                    } catch (IllegalAccessException e) {
        //                        e.printStackTrace();
        //                    } catch (NoSuchFieldException e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //            }
        //        });
    }

    public void clearWebViewResource(ViewGroup container, WebView webView) {
        if (webView != null) {
            webView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
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
