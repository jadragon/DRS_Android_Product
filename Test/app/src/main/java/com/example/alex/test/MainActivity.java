package com.example.alex.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView txt_award;
    private TextView switcher1, switcher2, switcher3;
    private Wheel wheel1, wheel2, wheel3;
    private Thread tread;
    public static final Random RANDOM = new Random();
    private View bar1, bar2, ball;

    int ball_lenth;
    private int bar1_visible_height, bar2_visible_height, limit_top, limit_bottom;
    int status_height;
    int screen_width, screen_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_height = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }

        screen_width = getResources().getDisplayMetrics().widthPixels;
        screen_height = getResources().getDisplayMetrics().heightPixels-status_height;
        initView();

        initBall();
    }

    private void initView() {
        int paddingTop = screen_width / 16;
        int main_height = screen_width / 16 * 9;

        int logo_width = screen_width / 5 * 2;
        int logo_height = logo_width / 2;

        int machine_height = screen_width / 5 * 2;

        ball_lenth = screen_width / 7;
        int hole_lenth = screen_width / 8;

        int bar_width = screen_width / 16;
        int bar_marginRight = screen_width / 16;

        bar1_visible_height = paddingTop + screen_height / 2 - ball_lenth;
        bar2_visible_height = paddingTop + screen_height / 2;
        limit_top = paddingTop + screen_height / 2 - ball_lenth * 3 / 2;
        limit_bottom = paddingTop + screen_height / 2 + ball_lenth * 3 / 2;

        findViewById(R.id.screen).getLayoutParams().height = main_height;

        findViewById(R.id.logo).getLayoutParams().width = logo_width;
        findViewById(R.id.logo).getLayoutParams().height = logo_height;

        findViewById(R.id.machine).getLayoutParams().height = machine_height;
        findViewById(R.id.machine).setPadding(0, 0, bar_marginRight, 0);
        ((FrameLayout.LayoutParams) findViewById(R.id.machine).getLayoutParams()).topMargin = paddingTop + (main_height - machine_height) / 2;
        // findViewById(R.id.rollbar_layout).getLayoutParams().width = screen_width / 7;
        findViewById(R.id.hole).getLayoutParams().width = hole_lenth;
        findViewById(R.id.hole).getLayoutParams().height = hole_lenth;
        bar1 = findViewById(R.id.bar1);
        bar1.getLayoutParams().width = bar_width;
        ((LinearLayout.LayoutParams) bar1.getLayoutParams()).setMargins(0, machine_height / 2 - ball_lenth, 0, 0);
        bar2 = findViewById(R.id.bar2);
        bar2.getLayoutParams().width = bar_width;
        ((LinearLayout.LayoutParams) bar2.getLayoutParams()).setMargins(0, 0, 0, machine_height / 2 - ball_lenth);
        ball = findViewById(R.id.ball);
        ball.getLayoutParams().width = ball_lenth;
        ball.getLayoutParams().height = ball_lenth;
        ((FrameLayout.LayoutParams) ball.getLayoutParams()).setMargins(0, limit_top, bar_marginRight, 0);

        findViewById(R.id.switcher_layout).setPadding(screen_width / 16, screen_width / 8, 0, screen_width / 128 * 5);

        switcher1 = findViewById(R.id.switcher1);
        switcher2 = findViewById(R.id.switcher2);
        switcher3 = findViewById(R.id.switcher3);
        switcher1.setText(7 + "");
        switcher2.setText(7 + "");
        switcher3.setText(7 + "");
        txt_award = findViewById(R.id.txt_award);
        txt_award.getLayoutParams().height = main_height / 9;
        ((FrameLayout.LayoutParams) txt_award.getLayoutParams()).setMargins(screen_width/7*2, paddingTop + (main_height - machine_height) / 2, 0, 0);

    }

    private void initBall() {
        ball.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.e("View", v.toString());
                int ea = event.getAction();
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                        lastY = (int) event.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        //event.getRawX();获得移动的位置
                        //  int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int l = v.getLeft();
                        int b = v.getBottom() + dy;
                        int r = v.getRight();
                        int t = v.getTop() + dy;
                        //下面判断移动是否超出屏幕
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }
                        if (t < limit_top) {
                            t = limit_top;
                            b = t + v.getHeight();
                        }
                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }
                        if (b > limit_bottom) {
                            b = limit_bottom;
                            t = b - v.getHeight();
                        }

                        if (t < bar1_visible_height) {
                            bar1.setVisibility(View.VISIBLE);
                            bar2.setVisibility(View.INVISIBLE);
                        } else if (t > bar1_visible_height && t < bar2_visible_height) {
                            bar1.setVisibility(View.INVISIBLE);
                            bar2.setVisibility(View.INVISIBLE);
                        } else if (t > bar2_visible_height) {
                            bar1.setVisibility(View.INVISIBLE);
                            bar2.setVisibility(View.VISIBLE);
                        }
                        v.layout(l, t, r, b);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        dy = (int) event.getRawY() - lastY;
                        if (v.getTop() + dy > bar2_visible_height) {
                            startRoll();
                        }
                        l = v.getLeft();
                        r = v.getRight();
                        t = limit_top;
                        b = t + v.getHeight();
                        v.layout(l, t, r, b);
                        bar1.setVisibility(View.VISIBLE);
                        bar2.setVisibility(View.INVISIBLE);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                        break;
                }
                 /*
                switch (event.getAction() & MotionEvent.ACTION_MASK) {          //判斷觸控的動作

                    case MotionEvent.ACTION_DOWN:// 按下圖片時
                        y = event.getY();                  //觸控的Y軸位置
                        x = v.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:// 移動圖片時
                        //getX()：是獲取當前控件(View)的座標
                        //getRawX()：是獲取相對顯示螢幕左上角的座標
                        int dy = (int) (event.getRawY() - y);
                        if (Math.abs(dy - preY) > 3) {
                            // v.scrollTo(0, -dy);

                            if (dy < limit_top || dy > limit_bottom) {
                                break;
                            }
                            ViewGroup.MarginLayoutParams mlp =
                                    (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                            mlp.topMargin = dy;
                            v.setLayoutParams(mlp);
                            //  ball.layout((int) x, dy, (int) (x + ball_lenth), dy + ball_lenth);
                            if (dy < bar1_visible_height) {
                                bar1.setVisibility(View.VISIBLE);
                                bar2.setVisibility(View.INVISIBLE);
                            } else if (dy > bar1_visible_height && dy < bar2_visible_height) {
                                bar1.setVisibility(View.INVISIBLE);
                                bar2.setVisibility(View.INVISIBLE);
                            } else if (dy > bar2_visible_height) {
                                bar2.setVisibility(View.VISIBLE);
                                bar1.setVisibility(View.INVISIBLE);
                            }
                            preY = dy;
                        }
                        // Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
                        break;
                    case MotionEvent.ACTION_UP:
                        dy = (int) (event.getRawY() - y);
                        // v.scrollTo(0, (int) -y);
                        //   ball.layout((int) x, limit_top, (int) (x + ball_lenth), limit_top + ball_lenth);
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                        mlp.topMargin = limit_top;
                        v.setLayoutParams(mlp);
                        bar1.setVisibility(View.VISIBLE);
                        bar2.setVisibility(View.INVISIBLE);
                        if (dy > bar2_visible_height) {
                            startRoll();
                        }

                        break;

                }
                */
                return true;

            }
        });
    }

    private void startRoll() {
        if (wheel1 != null) {
            wheel1.stopWheel();
            wheel2.stopWheel();
            wheel3.stopWheel();
        }
        if (tread != null) {
            tread.interrupt();
        }
        wheel1 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switcher1.setText(img + "");
                    }
                });
            }
        }, randomLong(50, 100), 0);

        wheel2 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switcher2.setText(img + "");
                    }
                });
            }
        }, randomLong(50, 100), 0);
        wheel3 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switcher3.setText(img + "");
                    }
                });
            }
        }, randomLong(50, 100), 0);
        wheel1.start();
        wheel2.start();
        wheel3.start();
        tread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();
                    Thread.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "得獎人員:" + switcher1.getText().toString() + switcher2.getText().toString() + switcher3.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tread.start();
    }

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }
}
