package com.example.alex.posdemo;

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

import com.example.alex.posdemo.adapter.recylclerview.SliderMenuAdapter;
import com.example.alex.posdemo.fragment.Fragment_Home;

import Utils.ComponentUtil;
import library.MainSlideMenuAnimation;
import library.SubSlideMenuAnimation;
import library.ScrollSliderMenu;

public class MainActivity extends AppCompatActivity {

    private Button slider_menu_btn, alert_btn, accoption_btn;
    private View dismiss;
    DisplayMetrics dm;
    private RecyclerView mainslide_reclerview, subslide_recylcetview;
    private SliderMenuAdapter mainslide_adapter, subslide_adapter;
    private View subview, menu, content;
    private ComponentUtil componentUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        componentUtil = new ComponentUtil();
        dm = getResources().getDisplayMetrics();
        initValues();
        initFragment();
        initReyclerView();
        initToolbarButton();
    }

    private void initFragment() {
        Fragment_Home fragment_home = new Fragment_Home();
        switchFrament(fragment_home);
    }

    private void initReyclerView() {
        subview = findViewById(R.id.home_subslide_layout);
        //main
        mainslide_reclerview = findViewById(R.id.home_mainslide_recylcetview);
        mainslide_adapter = new SliderMenuAdapter(this, R.array.slider_main_bg, R.array.slider_main_img, getResources().getStringArray(R.array.slider_main_txt), 0);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mainslide_reclerview.setLayoutManager(layoutManager1);
        mainslide_reclerview.setAdapter(mainslide_adapter);
        //sub
        subslide_recylcetview = findViewById(R.id.home_subslide_recylcetview);
        subslide_adapter = new SliderMenuAdapter(this, R.array.slider_main_bg, R.array.slider_main_img, getResources().getStringArray(R.array.slider_main_txt), 0);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        subslide_recylcetview.setLayoutManager(layoutManager2);
        subslide_recylcetview.setAdapter(subslide_adapter);
    }

    //============================================================================================================================================
    private static final int TYPE_ALERT = 1;
    private static final int TYPE_OPTION = 2;
    private boolean alertIsShow, accOpIsShow, subIsShow;

    public boolean isAlertIsShow() {
        return alertIsShow;
    }

    public void setAlertIsShow(boolean alertIsShow) {
        this.alertIsShow = alertIsShow;
    }

    public boolean isAccOpIsShow() {
        return accOpIsShow;
    }

    public void setAccOpIsShow(boolean accOpIsShow) {
        this.accOpIsShow = accOpIsShow;
    }

    public boolean isSubIsShow() {
        return subIsShow;
    }

    public void setSubIsShow(boolean subIsShow) {
        this.subIsShow = subIsShow;
    }

    PopupWindow popWin;

    public PopupWindow getPopWin() {
        return popWin;
    }

    private int menuWidth;
    private boolean isMenuVisible;

    private void initToolbarButton() {
        //main_slider
        menu = findViewById(R.id.menu);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        menuWidth = layoutParams.width;
        layoutParams.leftMargin = -menuWidth;
        //
        content = findViewById(R.id.content);
        dismiss = findViewById(R.id.home_dismiss);
        dismiss.setAlpha(0);
        slider_menu_btn = findViewById(R.id.slider_menu_btn);
        slider_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    MainSlideMenuAnimation aa = new MainSlideMenuAnimation(dm, menu, content, menuWidth, 200, MainSlideMenuAnimation.COLLAPSE);
                    menu.startAnimation(aa);
                    isMenuVisible = false;
                    if (subIsShow) {
                        if (!(alertIsShow || accOpIsShow)) {
                            componentUtil.hideDissMiss(dismiss);
                        }
                        SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.COLLAPSE);
                        subview.startAnimation(a);
                        subIsShow = false;
                    }
                } else {
                    MainSlideMenuAnimation aa = new MainSlideMenuAnimation(dm, menu, content, menuWidth, 200, MainSlideMenuAnimation.EXPAND);
                    menu.startAnimation(aa);
                    isMenuVisible = true;
                }
            }
        });
        alert_btn = findViewById(R.id.alert_btn);
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertIsShow || accOpIsShow || subIsShow) {
                    if (popWin != null)
                        popWin.dismiss();
                    if (alertIsShow) {
                        componentUtil.hideDissMiss(dismiss);
                        alertIsShow = false;
                    } else {
                        View popView = getLayoutInflater().inflate(
                                R.layout.item_alert, null);
                        popUpMyOverflow(popView, TYPE_ALERT);
                        if (subview.getVisibility() == View.VISIBLE) {
                            SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.COLLAPSE);
                            subview.startAnimation(a);
                        }
                        alertIsShow = true;
                        accOpIsShow = false;
                        subIsShow = false;
                    }

                } else {
                    componentUtil.showDissMiss(dismiss);
                    View popView = getLayoutInflater().inflate(
                            R.layout.item_alert, null);
                    popUpMyOverflow(popView, TYPE_ALERT);
                    alertIsShow = true;
                }
            }
        });
        accoption_btn = findViewById(R.id.accoption_btn);
        accoption_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertIsShow || accOpIsShow || subIsShow) {
                    if (popWin != null)
                        popWin.dismiss();
                    if (accOpIsShow) {
                        componentUtil.hideDissMiss(dismiss);
                        accOpIsShow = false;
                    } else {
                        View popView = getLayoutInflater().inflate(
                                R.layout.item_logout, null);
                        popUpMyOverflow(popView, TYPE_OPTION);
                        if (subview.getVisibility() == View.VISIBLE) {
                            SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.COLLAPSE);
                            subview.startAnimation(a);
                        }
                        alertIsShow = false;
                        accOpIsShow = true;
                        subIsShow = false;
                    }
                } else {
                    componentUtil.showDissMiss(dismiss);
                    View popView = getLayoutInflater().inflate(
                            R.layout.item_logout, null);
                    popUpMyOverflow(popView, TYPE_OPTION);
                    accOpIsShow = true;
                }
            }
        });
    }

    //============================================================================================================================================
    ScrollSliderMenu scrollSliderMenu;

    private void initValues() {
        View content = findViewById(R.id.content);
        View menu = findViewById(R.id.menu);
        //scrollSliderMenu = new ScrollSliderMenu(content, menu, dm.widthPixels);
    }

//============================================================================================================================================

    public void popUpMyOverflow(View popView, int type) {
        /**
         * 定位PopupWindow，让它恰好显示在Action Bar的下方。 通过设置Gravity，确定PopupWindow的大致位置。
         * 首先获得状态栏的高度，再获取Action bar的高度，这两者相加设置y方向的offset样PopupWindow就显示在action
         * bar的下方了。 通过dp计算出px，就可以在不同密度屏幕统一X方向的offset.但是要注意不要让背景阴影大于所设置的offset，
         * 否则阴影的宽度为offset.
         */
        if (type == TYPE_ALERT) {
            popWin = new PopupWindow(popView, (int) (800 * dm.density), (int) (300 * dm.density), false);
        } else if (type == TYPE_OPTION) {
            popWin = new PopupWindow(popView, (int) (200 * dm.density), (int) (150 * dm.density), false);
        }
        popWin.setAnimationStyle(android.R.style.Animation_Dialog);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        popWin.showAtLocation(getCurrentFocus(), Gravity.TOP | Gravity.RIGHT, 0, (int) (50 * dm.density));
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, fragment).commitAllowingStateLoss();

    }

}
