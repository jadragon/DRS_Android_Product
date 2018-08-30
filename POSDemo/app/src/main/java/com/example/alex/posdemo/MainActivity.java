package com.example.alex.posdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.adapter.recylclerview.SliderMainMenuAdapter;
import com.example.alex.posdemo.adapter.recylclerview.SliderSubMenuAdapter;
import com.example.alex.posdemo.fragment.Fragment_home;
import com.example.alex.posdemo.pojo.CheckMainButtonPojo;

import java.util.HashMap;
import java.util.Map;

import Utils.ComponentUtil;
import db.SQLiteDatabaseHandler;


public class MainActivity extends AppCompatActivity {

    private Button main_back, slider_menu_btn, alert_btn, accoption_btn;
    private View dismiss;
    DisplayMetrics dm;
    private RecyclerView mainslide_reclerview, subslide_recylcetview;
    private SliderMainMenuAdapter mainslide_adapter;
    private SliderSubMenuAdapter subslide_adapter;
    private View subview, menu, content;
    private ComponentUtil componentUtil;
    public static CheckMainButtonPojo checkMainButtonPojo = new CheckMainButtonPojo();
    private UserInfo userInfo;
    private String MAIN_FRAGMENT_TAG = "";
    private String SUB_FRAGMENT_TAG = "";

    public void setSUB_FRAGMENT_TAG(String SUB_FRAGMENT_TAG) {
        this.SUB_FRAGMENT_TAG = SUB_FRAGMENT_TAG;
    }

    public void setMAIN_FRAGMENT_TAG(String MAIN_FRAGMENT_TAG) {
        this.MAIN_FRAGMENT_TAG = MAIN_FRAGMENT_TAG;
    }

    public void showBack(boolean ok) {
        if (ok) {
            main_back.setVisibility(View.VISIBLE);
            slider_menu_btn.setVisibility(View.GONE);
            if (isMenuVisible) {
                componentUtil.showMainMenu(dm.widthPixels, menu, content, false);
                isMenuVisible = false;
            }
        } else {
            main_back.setVisibility(View.GONE);
            slider_menu_btn.setVisibility(View.VISIBLE);
        }


    }

    public SliderSubMenuAdapter getSubslide_adapter() {
        return subslide_adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfo = (UserInfo) getApplicationContext();
        componentUtil = new ComponentUtil();
        dm = getResources().getDisplayMetrics();
        initTitleData();
        initFragment();
        initReyclerView();
        initToolbarButton();
    }

    private void initTitleData() {
        ((TextView) findViewById(R.id.main_store)).setText(userInfo.getStore());
        ((TextView) findViewById(R.id.main_name)).setText("使用者:" + userInfo.getName());
        ((TextView) findViewById(R.id.main_dname)).setText("部門:" + userInfo.getDname());
    }

    private void initFragment() {
        Fragment_home fragment_home = new Fragment_home();
        switchFrament(fragment_home, "home");
    }

    private void initReyclerView() {
        subview = findViewById(R.id.home_subslide_layout);
        //main
        mainslide_reclerview = findViewById(R.id.home_mainslide_recylcetview);
        Map<String, Integer> map = new HashMap<>();
        map.put("background", R.array.slider_main_bg);
        map.put("image", R.array.slider_main_img);
        map.put("arrow", R.array.slider_main_arrow);
        map.put("text", R.array.slider_main_txt);
        mainslide_adapter = new SliderMainMenuAdapter(this, map);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mainslide_reclerview.setLayoutManager(layoutManager1);
        mainslide_reclerview.setAdapter(mainslide_adapter);
        //sub
        subslide_recylcetview = findViewById(R.id.home_subslide_recylcetview);
        subslide_adapter = new SliderSubMenuAdapter(this, null);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        subslide_recylcetview.setLayoutManager(layoutManager2);
        subslide_recylcetview.setAdapter(subslide_adapter);
    }

    //============================================================================================================================================
    private static final int TYPE_ALERT = 1;
    private static final int TYPE_OPTION = 2;


    PopupWindow popWin;

    public PopupWindow getPopWin() {
        return popWin;
    }


    private boolean isMenuVisible;

    private void initToolbarButton() {
        //main_back
        main_back = findViewById(R.id.main_back);
        main_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).commit();
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(SUB_FRAGMENT_TAG)).commit();
                showBack(false);
            }
        });
        main_back.setVisibility(View.GONE);
        //main_slider
        menu = findViewById(R.id.menu);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        int menuWidth = layoutParams.width;
        layoutParams.leftMargin = -menuWidth;
        //主內容
        content = findViewById(R.id.content);
        //遮罩
        dismiss = findViewById(R.id.home_dismiss);
        dismiss.setAlpha(0);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWin != null)
                    popWin.dismiss();
                if (checkMainButtonPojo.subIsShow) {
                    componentUtil.showSubMenu(subview, false);
                }
                checkMainButtonPojo.accOpIsShow = false;
                checkMainButtonPojo.alertIsShow = false;
                checkMainButtonPojo.subIsShow = false;
                componentUtil.showDissMiss(dismiss, false);
                checkMainButtonPojo.isSelectSlide[checkMainButtonPojo.preClick] = false;
                mainslide_adapter.notifyDataSetChanged();
            }
        });
        dismiss.setClickable(false);
        //slidermenu
        slider_menu_btn = findViewById(R.id.slider_menu_btn);
        slider_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    componentUtil.showMainMenu(dm.widthPixels, menu, content, false);
                    isMenuVisible = false;
                    if (checkMainButtonPojo.subIsShow) {
                        if (!(checkMainButtonPojo.alertIsShow || checkMainButtonPojo.accOpIsShow)) {
                            componentUtil.showDissMiss(dismiss, false);
                        }
                        componentUtil.showSubMenu(subview, false);
                        checkMainButtonPojo.subIsShow = false;
                        checkMainButtonPojo.isSelectSlide[checkMainButtonPojo.preClick] = false;
                        mainslide_adapter.notifyDataSetChanged();
                    }
                } else {
                    componentUtil.showMainMenu(dm.widthPixels, menu, content, true);
                    isMenuVisible = true;
                }
            }
        });
        //通知按鈕
        alert_btn = findViewById(R.id.alert_btn);
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMainButtonPojo.alertIsShow || checkMainButtonPojo.accOpIsShow) {
                    if (popWin != null)
                        popWin.dismiss();
                    if (checkMainButtonPojo.alertIsShow) {
                        componentUtil.showDissMiss(dismiss, false);
                        checkMainButtonPojo.alertIsShow = false;
                    } else {
                        popUpMyOverflow(TYPE_ALERT);
                        checkMainButtonPojo.alertIsShow = true;
                        checkMainButtonPojo.accOpIsShow = false;
                        checkMainButtonPojo.subIsShow = false;
                    }
                } else {
                    if (checkMainButtonPojo.subIsShow) {
                        componentUtil.showSubMenu(subview, false);
                        checkMainButtonPojo.subIsShow = false;
                        checkMainButtonPojo.isSelectSlide[checkMainButtonPojo.preClick] = false;
                        mainslide_adapter.notifyDataSetChanged();
                    } else {
                        componentUtil.showDissMiss(dismiss, true);
                    }
                    popUpMyOverflow(TYPE_ALERT);
                    checkMainButtonPojo.alertIsShow = true;

                }
            }
        });
        //帳戶設定
        accoption_btn = findViewById(R.id.accoption_btn);
        accoption_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMainButtonPojo.alertIsShow || checkMainButtonPojo.accOpIsShow) {
                    if (popWin != null)
                        popWin.dismiss();
                    if (checkMainButtonPojo.accOpIsShow) {
                        componentUtil.showDissMiss(dismiss, false);
                        checkMainButtonPojo.accOpIsShow = false;
                    } else {
                        popUpMyOverflow(TYPE_OPTION);
                        checkMainButtonPojo.alertIsShow = false;
                        checkMainButtonPojo.accOpIsShow = true;
                        checkMainButtonPojo.subIsShow = false;
                    }
                } else {
                    if (checkMainButtonPojo.subIsShow) {
                        componentUtil.showSubMenu(subview, false);
                        checkMainButtonPojo.subIsShow = false;
                        checkMainButtonPojo.isSelectSlide[checkMainButtonPojo.preClick] = false;
                        mainslide_adapter.notifyDataSetChanged();
                    } else {
                        componentUtil.showDissMiss(dismiss, true);
                    }
                    popUpMyOverflow(TYPE_OPTION);
                    checkMainButtonPojo.accOpIsShow = true;

                }
            }

        });
    }


//============================================================================================================================================

    public void popUpMyOverflow(int type) {
        /**
         * 定位PopupWindow，让它恰好显示在Action Bar的下方。 通过设置Gravity，确定PopupWindow的大致位置。
         * 首先获得状态栏的高度，再获取Action bar的高度，这两者相加设置y方向的offset样PopupWindow就显示在action
         * bar的下方了。 通过dp计算出px，就可以在不同密度屏幕统一X方向的offset.但是要注意不要让背景阴影大于所设置的offset，
         * 否则阴影的宽度为offset.
         */
        View popView;
        if (type == TYPE_ALERT) {
            popView = getLayoutInflater().inflate(
                    R.layout.item_alert, null);
            popWin = new PopupWindow(popView, (int) (800 * dm.density), (int) (300 * dm.density), false);
        } else if (type == TYPE_OPTION) {
            popView = getLayoutInflater().inflate(
                    R.layout.item_logout, null);

            popView.findViewById(R.id.main_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(MainActivity.this);
                    db.resetTables();
                    db.close();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            popWin = new PopupWindow(popView, (int) (200 * dm.density), (int) (150 * dm.density), false);
        }
        popWin.setAnimationStyle(android.R.style.Animation_Dialog);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        popWin.showAtLocation(content, Gravity.TOP | Gravity.RIGHT, 0, (int) (50 * dm.density));
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentfrom = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragmentfrom != null) {    // 先判断是否被add过
            transaction.show(fragmentfrom).commit();
        } else {
            transaction.replace(R.id.fragment_content, fragment, tag).commitAllowingStateLoss();
        }
    }

}
