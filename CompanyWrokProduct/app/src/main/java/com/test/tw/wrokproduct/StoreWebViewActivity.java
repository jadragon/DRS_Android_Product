package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import library.GetJsonData.GetWebView;

public class StoreWebViewActivity extends AppCompatActivity {
    private XWalkView luntanListview;
    String html;
    ViewGroup container;
    String name, address, deliver;
    Intent intent;
    String logistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_webview);
        intent = getIntent();
        logistics = intent.getStringExtra("logistics");
        new Thread(new Runnable() {
            @Override
            public void run() {
                html = new GetWebView(logistics).getMap();
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
        luntanListview = findViewById(R.id.store_webview);
        //设置允许访问浏览器页面的js方法
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        luntanListview.load(null,html);
        luntanListview.setUIClient(new XWalkUIClient(luntanListview){
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                if (url.contains("js-call")) {
                    String[] uuu = url.split("&");
                    try {
                        deliver = uuu[0].replace("js-call://setDeliver?", "");
                        name = URLDecoder.decode(uuu[1], "UTF-8");
                        address = URLDecoder.decode(uuu[2], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("deliver", deliver);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    setResult(0, intent);
                    StoreWebViewActivity.this.finish();
                }
            }
        });
    }

    public void clearWebViewResource(ViewGroup container, XWalkView webView) {
        if (webView != null) {
            webView.clearCache(true);
            webView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            if (container != null)
                container.removeView(webView);
            webView.setTag(null);
            webView.onDestroy();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }


}
