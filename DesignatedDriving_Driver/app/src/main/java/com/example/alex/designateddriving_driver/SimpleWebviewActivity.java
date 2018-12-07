package com.example.alex.designateddriving_driver;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.alex.designateddriving_driver.API.CustomerApi;
import com.example.alex.designateddriving_driver.Utils.AsyncTaskUtils;
import com.example.alex.designateddriving_driver.Utils.IDataCallBack;

import org.json.JSONException;
import org.json.JSONObject;


public class SimpleWebviewActivity extends ToolbarActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_webview);
        final String title = getIntent().getStringExtra("title");
        initToolbar(title, true);
        initWebview();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {

                switch (title) {
                    case "服務條款":
                        return CustomerApi.tservice();
                    case "隱私權政策":
                        return CustomerApi.privacy();
                    case "服務宗旨":
                        return CustomerApi.pservice();
                    case "關於我們":
                        return CustomerApi.about();
                    case "常見問題":
                        return CustomerApi.question();
                }
                return null;
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                String html = "";
                try {
                    html = jsonObject.getString("Data");
                    webview.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                            null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("html", html);
            }
        });
    }

    private void initWebview() {
        webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(200);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(this.getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //设置UA
        //    webSettings.setUserAgentString(webSettings.getUserAgentString() + " kaolaApp/" + AppUtils.getVersionName());
        //移除部分系统JavaScript接口
        //   KaolaWebViewSecurity.removeJavascriptInterfaces(webView);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }
}
