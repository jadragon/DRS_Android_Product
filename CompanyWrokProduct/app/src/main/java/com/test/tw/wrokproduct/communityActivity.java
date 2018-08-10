package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class CommunityActivity extends AppCompatActivity {
    private XWalkView luntanListview;
    String title, html;
    ViewGroup container;
    Intent intent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        //设置允许访问浏览器页面的js方法
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        luntanListview = findViewById(R.id.community_webview);
        luntanListview.load(null, html);
        luntanListview.setUIClient(new XWalkUIClient(luntanListview));
    }

    public void clearWebViewResource(ViewGroup container, XWalkView webView) {
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
            webView.onDestroy();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource(container, luntanListview);
    }


}
