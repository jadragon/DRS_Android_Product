package com.test.tw.wrokproduct;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

import adapter.PcContentPagerAdapter;
import adapter.PcContentWebViewPagerAdapter;
import adapter.ShipsWaysRecyclerViewAdapter;
import library.ContentViewPager;
import library.GetInformationByPHP;
import library.ResolveJsonData;

public class PcContentActivity extends AppCompatActivity {
    DisplayMetrics dm;
    private ViewPager viewPager;
    private ContentViewPager webviewpager;
    private JSONObject json;
    private PcContentPagerAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public DisplayMetrics displayMetrics;
    LinearLayout ship_ways;
    TextView pccontent_txt_title, pccontent_txt_descs, pccontent_txt_rsprice, pccontent_txt_rprice;
    String pno;
    Dialog dialog;
    private View inflate;
    RecyclerView recyclerView;
    ImageView heart;
    View enable_background;
    LinearLayout shipways_layout;
    private PopupWindow popWin = null; // 弹出窗口
    private View popView = null; // 保存弹出窗口布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pno = getIntent().getStringExtra("pno");
        pno = "URwlZEnZscDdnIJN4vjczw==";
        dm = getResources().getDisplayMetrics();
        setContentView(R.layout.activity_pccontent);
        heart = findViewById(R.id.pccontent_heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heart.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.heart_off).getConstantState())) {
                    heart.setImageResource(R.drawable.heart_on);
                    toastFavorate(0);
                } else {
                    heart.setImageResource(R.drawable.heart_off);
                    toastFavorate(1);
                }

            }

        });
        initEnableBackground();
        initShipWay();
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

                    }
                });

            }
        }).start();
    }

    private void initEnableBackground() {
        enable_background = findViewById(R.id.enable_background);
        enable_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popWin.dismiss();
                enable_background.setVisibility(View.INVISIBLE);
            }
        });
        enable_background.setVisibility(View.INVISIBLE);
    }

    private void toastFavorate(int yes_or_not) {
        dialog = new Dialog(this);
        inflate = LayoutInflater.from(this).inflate(R.layout.favorate_layout, null);
        if (yes_or_not == 0)
            ((TextView) inflate.findViewById(R.id.favor_txt)).setText("已加入我的最愛");
        else if (yes_or_not == 1)
            ((TextView) inflate.findViewById(R.id.favor_txt)).setText("已取消我的最愛");
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();//显示对话框
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }

    private void initShipWay() {
        ship_ways = findViewById(R.id.ship_ways);
        ship_ways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shipways_layout, null);
                recyclerView = popView.findViewById(R.id.ship_ways_review);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new ShipsWaysRecyclerViewAdapter(getApplicationContext(), json));
                popWin = new PopupWindow(popView,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false); // 实例化PopupWindow
                // 设置PopupWindow的弹出和消失效果

                popWin.setAnimationStyle(R.style.dialogWindowAnim);

                popWin.showAtLocation(enable_background, Gravity.BOTTOM, 0, 0); // 显示弹出窗口
                enable_background.setVisibility(View.VISIBLE);
            }
        });

    }

    private String getDeciamlString(String str) {
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
        pccontent_txt_rsprice.setText("$" + getDeciamlString(information.get("rsprice")));
        if (information.get("rsprice").equals(information.get("rprice")))
            pccontent_txt_rsprice.setVisibility(View.INVISIBLE);
        pccontent_txt_rprice = findViewById(R.id.pccontent_txt_rprice);
        pccontent_txt_rprice.setText("$" + getDeciamlString(information.get("rprice")));

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

    public void showShipways() {
        dialog = new Dialog(this, R.style.Theme_Design_Light_BottomSheetDialog);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.shipways_layout, null);
        //init控件
        recyclerView = inflate.findViewById(R.id.ship_ways_review);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ShipsWaysRecyclerViewAdapter(getApplicationContext(), json));
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.windowAnimations = R.style.dialogWindowAnim;
        lp.width = dm.widthPixels;
        lp.height = (int) (dm.heightPixels - 200 * dm.density);
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
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
                finish();
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
