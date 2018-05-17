package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import library.GetJsonData.GetWebView;

public class StoreWebViewActivity extends AppCompatActivity {
    private WebView luntanListview;
    String html;
    ViewGroup container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_webview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                html=new GetWebView("").getMap();
                Log.e("html",html);
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
        luntanListview =findViewById(R.id.store_webview);
        // 设置WevView要显示的网页
      //  luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8",
          //      null);
        luntanListview.loadUrl("");
        luntanListview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript


        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        luntanListview.setWebChromeClient(new WebChromeClient());
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
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }


}
