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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
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
    LinearLayout ship_ways;
    TextView pccontent_txt_title,pccontent_txt_descs,pccontent_txt_rsprice,pccontent_txt_rprice;
    String pno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pno = getIntent().getStringExtra("pno");
        setContentView(R.layout.activity_pccontent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPcontent(pno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setText();
                        displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                        initViewPager();
                        initTabLayout();
                        initToo();
                        initShipWay();
                    }
                });

            }
        }).start();

    }
    private void initShipWay(){
        ship_ways=findViewById(R.id.ship_ways);
        ship_ways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PcContentActivity.this, "ShipWays", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }
    public void setText() {
        Map<String, String> information = ResolveJsonData.getPcContentInformation(json);
        Log.e("information", information + "");
        pccontent_txt_title = findViewById(R.id.pccontent_txt_title);
        pccontent_txt_title.setText(information.get("pname"));
        pccontent_txt_descs = findViewById(R.id.pccontent_txt_descs);
        pccontent_txt_descs.setText(information.get("descs"));
        pccontent_txt_rsprice = findViewById(R.id.pccontent_txt_rsprice);
        pccontent_txt_rsprice.setText("$"+getDeciamlString(information.get("rsprice")));
        pccontent_txt_rprice = findViewById(R.id.pccontent_txt_rprice);
        pccontent_txt_rprice.setText("$"+getDeciamlString(information.get("rprice")));

    }

    private void initTabLayout() {
        webviewpager = findViewById(R.id.pccontent_web_viewpager);
        tabLayout = findViewById(R.id.pccontent_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        Map<String, String> map = ResolveJsonData.getWebView(json);

        webviewpager.setAdapter(new PcContentWebViewPagerAdapter(getSupportFragmentManager(), new String[]{"詳細說明", "退/換貨須知"}, new String[]{map.get("content"), map.get("rpolicy")}));
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
        adapter = new PcContentPagerAdapter(getWindow().getDecorView(), json, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels);
        viewPager.setAdapter(adapter);
    }

}
