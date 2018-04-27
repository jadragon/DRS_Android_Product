package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import org.json.JSONObject;

import java.util.Map;

import adapter.PcContentPagerAdapter;
import adapter.PcContentWebViewPagerAdapter;
import library.ContentViewPager;
import library.GetInformationByPHP;
import library.ResolveJsonData;

public class PcContentActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ContentViewPager webviewpager;
    private JSONObject json;
    private PcContentPagerAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public DisplayMetrics displayMetrics;
    String pno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pno=getIntent().getStringExtra("pno");
        setContentView(R.layout.activity_pccontent);
        displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        initViewPager();
        initToo();


    }

    private void initTabLayout() {
        webviewpager = findViewById(R.id.pccontent_web_viewpager);
        tabLayout=findViewById(R.id.pccontent_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        Map<String,String> map=ResolveJsonData.getWebView(json);
        Log.e("WEBVIEWWWWWWWWWWWW",map+"");
        webviewpager.setAdapter(new PcContentWebViewPagerAdapter(getSupportFragmentManager(),new String[]{"詳細說明","退/換貨須知"},new String[]{map.get("content"),map.get("rpolicy")}));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(webviewpager, true);
     //   webviewpager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,6500));
    }

    private void initToo() {
        //Toolbar 建立
        toolbar = findViewById(R.id.pccontent_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pc_content_menu, menu);
        return true;
    }


    private void initViewPager() {
        viewPager = findViewById(R.id.adView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPcontent(pno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new PcContentPagerAdapter(getWindow().getDecorView(), json, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels);
                        viewPager.setAdapter(adapter);
                        initTabLayout();
                    }
                });

            }
        }).start();

    }

    private void testJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPcontent("URwlZEnZscDdnIJN4vjczw==");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TestJSONONONONON", ResolveJsonData.getPcContent(json) + "");
                    }
                });
            }
        }).start();


    }

}
