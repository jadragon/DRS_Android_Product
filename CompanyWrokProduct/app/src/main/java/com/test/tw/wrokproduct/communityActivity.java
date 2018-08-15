package com.test.tw.wrokproduct;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CommunityActivity extends AppCompatActivity {
    private WebView luntanListview;
    String title, html;
    ViewGroup container;
    Intent intent;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        intent = getIntent();
        html = intent.getStringExtra("html");
        title = intent.getStringExtra("title");
        initToolbar();
        showWebView();
    }


    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText(title);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showWebView() {
        luntanListview = findViewById(R.id.community_webview);
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        luntanListview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                SslCertificate sslCertificate = error.getCertificate();

                final AlertDialog.Builder builder = new AlertDialog.Builder(CommunityActivity.this);
                builder.setTitle("SSL 憑證錯誤");
                builder.setMessage("無法驗證伺服器SSL憑證。\n仍要繼續嗎?");
                builder.setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        /*
        //设置允许访问浏览器页面的js方法
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        luntanListview = findViewById(R.id.community_webview);
        luntanListview.load(null,html);
        luntanListview.setUIClient(new XWalkUIClient(luntanListview));
        */
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
            //   webView.clearHistory();
            //   webView.destroy();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }


}
