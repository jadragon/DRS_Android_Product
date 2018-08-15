package com.example.alex.posdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {
    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
     */
    private int leftEdge;

    /**
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge = 0;

    /**
     * 主内容的布局。
     */
    private View content;

    /**
     * menu的布局。
     */
    private View menu;


    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams menuParams;
    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams contentParams;

    /**
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;

    private Button slider_menu_btn, alert_btn, accoption_btn;
    private View dismiss;
    DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = getResources().getDisplayMetrics();
        initValues();

        initToolbarButton();
    }

    private void initToolbarButton() {
        dismiss = findViewById(R.id.home_dismiss);
        dismiss.setVisibility(View.INVISIBLE);
        slider_menu_btn = findViewById(R.id.slider_menu_btn);
        slider_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    scrollToContent();
                    isMenuVisible = false;
                } else {
                    scrollToMenu();
                    isMenuVisible = true;
                }
            }
        });
        alert_btn = findViewById(R.id.alert_btn);
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWin != null && popWin.isShowing()) {
                    if (alertIsShow) {
                        popWin.dismiss();
                        dismiss.setVisibility(View.INVISIBLE);
                        accOpIsShow = false;
                        alertIsShow = false;
                    } else {
                        popWin.dismiss();
                        initPopView(TYPE_ALERT);
                        dismiss.setVisibility(View.VISIBLE);
                        alertIsShow = true;
                        accOpIsShow = false;
                    }
                } else {
                    if (alertIsShow) {
                        popWin.dismiss();
                        dismiss.setVisibility(View.INVISIBLE);
                        alertIsShow = false;
                    } else {
                        initPopView(TYPE_ALERT);
                        dismiss.setVisibility(View.VISIBLE);
                        alertIsShow = true;
                    }
                }
            }
        });
        accoption_btn = findViewById(R.id.accoption_btn);
        accoption_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popWin != null && popWin.isShowing()) {
                    if (accOpIsShow) {
                        popWin.dismiss();
                        dismiss.setVisibility(View.INVISIBLE);
                        accOpIsShow = false;
                        alertIsShow = false;
                    } else {
                        popWin.dismiss();
                        initPopView(TYPE_OPTION);
                        dismiss.setVisibility(View.VISIBLE);
                        accOpIsShow = true;
                        alertIsShow = false;
                    }
                } else {
                    if (accOpIsShow) {
                        popWin.dismiss();
                        dismiss.setVisibility(View.INVISIBLE);
                        accOpIsShow = false;
                    } else {
                        initPopView(TYPE_OPTION);
                        dismiss.setVisibility(View.VISIBLE);
                        accOpIsShow = true;
                    }
                }
            }
        });
    }

    private static final int TYPE_ALERT = 1;
    private static final int TYPE_OPTION = 2;
    private boolean alertIsShow, accOpIsShow;


    private void initPopView(int type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View popView = null;
        if (type == TYPE_ALERT)
            popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_alert, null);
        else if (type == TYPE_OPTION)
            popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_logout, null);
        showPopWin(popView, type);
    }

    PopupWindow popWin;

    //顯示POP UP
    public void showPopWin(View popView, int type) {
       int heigh = 0;
        if (type == TYPE_ALERT) {
           heigh = (int) (300 * dm.density);
            popWin = new PopupWindow(popView, (int) (800 * dm.density), heigh, false);
        } else if (type == TYPE_OPTION) {
          heigh = (int) (150 * dm.density);
            popWin = new PopupWindow(popView, (int) (200 * dm.density), heigh, false);
        }

        // 设置PopupWindow的弹出和消失效果
        popWin.setAnimationStyle(R.style.dialogWindowAnim);
          popWin.showAtLocation(content, Gravity.TOP, screenWidth, (int) (50 * dm.density)); // 显示弹出窗口
       // popWin.showAsDropDown(content, screenWidth, -heigh);
    }

    /**
     * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
     */
    private void initValues() {
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = window.getDefaultDisplay().getWidth();
        content = findViewById(R.id.content);
        menu = findViewById(R.id.menu);

        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        // 将menu的宽度设置为屏幕宽度减去menuPadding
        //  menuParams.width = screenWidth - menuPadding;
        // 左边缘的值赋值为menu宽度的负数
        leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度

        contentParams.width = screenWidth;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为30.
     */
    private void scrollToMenu() {
        new ScrollTask().execute(30);
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-30.
     */
    private void scrollToContent() {
        new ScrollTask().execute(-30);
    }


    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = menuParams.leftMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
                sleep(20);
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            menuParams.leftMargin = leftMargin[0];
            menu.setLayoutParams(menuParams);
            contentParams.width = screenWidth - (menuParams.width + leftMargin[0]);

        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            menuParams.leftMargin = leftMargin;
            menu.setLayoutParams(menuParams);
            contentParams.width = screenWidth - (menuParams.width + leftMargin);
        }

    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
