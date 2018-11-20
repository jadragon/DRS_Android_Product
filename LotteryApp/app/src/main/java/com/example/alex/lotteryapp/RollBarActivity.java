package com.example.alex.lotteryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.util.ArrayList;
import java.util.Random;

public class RollBarActivity extends AppCompatActivity {
    private TextView txt_award;
    private TextView switcher1, switcher2, switcher3;
    private Wheel wheel1, wheel2, wheel3;
    private Thread tread;
    // private boolean isStarted, one, two, three;
    public static final Random RANDOM = new Random();
    private SQLiteDatabaseHandler db;
    private View bar1, bar2, ball;
    private Button back;
    private int bar1_visible_height, bar2_visible_height, limit_top, limit_bottom;
    private int status_height;

    private ArrayList<String> left_awardlist;
    private String award_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollbar);
        db = new SQLiteDatabaseHandler(this);
        initAwardType();
        initView();
        initListener();
        Log.e("RollBarActivity", "RollBarActivity");
    }

    private void initAwardType() {
        award_type = getIntent().getStringExtra("type");
        txt_award = findViewById(R.id.txt_award);
        txt_award.setText("獎項:" + db.getGift(award_type));
        refreshData();
    }

    private void initView() {
        int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_height = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }

        int screen_width = getResources().getDisplayMetrics().widthPixels;
        int screen_height = getResources().getDisplayMetrics().heightPixels - status_height;
        int paddingTop = screen_width / 16;
        int main_height = screen_width / 16 * 9;

        int logo_width = screen_width / 5 * 2;
        int logo_height = logo_width / 2;

        int machine_height = screen_width / 5 * 2;

        int ball_lenth = screen_width / 7;
        int hole_lenth = screen_width / 8;

        int bar_width = screen_width / 16;
        int bar_marginRight = screen_width / 16;

        bar1_visible_height = paddingTop + screen_height / 2 - ball_lenth;
        bar2_visible_height = paddingTop + screen_height / 2;
        limit_top = paddingTop + screen_height / 2 - ball_lenth * 3 / 2;
        limit_bottom = paddingTop + screen_height / 2 + ball_lenth * 3 / 2;


        //螢幕
        ViewGroup.LayoutParams params = findViewById(R.id.screen).getLayoutParams();
        params.height = main_height;
        //logo
        params = findViewById(R.id.logo).getLayoutParams();
        params.width = logo_width;
        params.height = logo_height;
        //機台
        params = findViewById(R.id.machine).getLayoutParams();
        params.height = machine_height;
        findViewById(R.id.machine).setPadding(0, 0, bar_marginRight, 0);
        ((FrameLayout.LayoutParams) params).topMargin = paddingTop + (main_height - machine_height) / 2;
        // findViewById(R.id.rollbar_layout).getLayoutParams().width = screen_width / 7;

        //拉桿洞
        params = findViewById(R.id.hole).getLayoutParams();
        params.width = hole_lenth;
        params.height = hole_lenth;

        //拉桿
        bar1 = findViewById(R.id.bar1);
        params = bar1.getLayoutParams();
        params.width = bar_width;
        ((LinearLayout.LayoutParams) params).setMargins(0, machine_height / 2 - ball_lenth, 0, 0);
        bar2 = findViewById(R.id.bar2);
        params = bar2.getLayoutParams();
        params.width = bar_width;
        ((LinearLayout.LayoutParams) params).setMargins(0, 0, 0, machine_height / 2 - ball_lenth);

        //球
        ball = findViewById(R.id.ball);
        params = ball.getLayoutParams();
        params.width = ball_lenth;
        params.height = ball_lenth;
        ((FrameLayout.LayoutParams) params).setMargins(0, limit_top, bar_marginRight, 0);

        //顯示螢幕
        findViewById(R.id.switcher_layout).setPadding(screen_width / 16, screen_width / 8, 0, screen_width / 128 * 5);

        switcher1 = findViewById(R.id.switcher1);
        switcher2 = findViewById(R.id.switcher2);
        switcher3 = findViewById(R.id.switcher3);
        switcher1.setText(7 + "");
        switcher2.setText(7 + "");
        switcher3.setText(7 + "");
        //獎項
        txt_award.getLayoutParams().height = main_height / 9;
        ((FrameLayout.LayoutParams) txt_award.getLayoutParams()).setMargins(screen_width / 7 * 2, paddingTop + (main_height - machine_height) / 2, 0, 0);

    }


    private void initListener() {
        ball.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.e("View", v.toString());
                switch (event.getAction()) {          //判斷觸控的動作

                    case MotionEvent.ACTION_DOWN:// 按下圖片時
                        lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                        lastY = (int) event.getRawY();

                    case MotionEvent.ACTION_MOVE:// 移動圖片時
                        int dy = (int) event.getRawY() - lastY;

                        int l = v.getLeft();
                        int b = v.getBottom() + dy;
                        int r = v.getRight();
                        int t = v.getTop() + dy;
                        //下面判断移动是否超出屏幕
                        /*
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }
                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }
                        */
                        if (t < limit_top) {
                            t = limit_top;
                            b = t + v.getHeight();
                        }
                        if (b > limit_bottom) {
                            b = limit_bottom;
                            t = b - v.getHeight();
                        }
                        v.layout(l, t, r, b);
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
                return true;
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollBarActivity.this.finish();
            }
        });
    }

    private void startRoll() {
        if (left_awardlist.size() <= 0) {
            Toast.makeText(RollBarActivity.this, "此獎項已額滿", Toast.LENGTH_SHORT).show();
            return;
        }
        back.setVisibility(View.INVISIBLE);
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
                            db.updateUserInfo(left_awardlist.get(0), switcher1.getText().toString() + switcher2.getText().toString() + switcher3.getText().toString());
                            Toast.makeText(RollBarActivity.this, "得獎人員:" + switcher1.getText().toString() + switcher2.getText().toString() + switcher3.getText().toString(), Toast.LENGTH_SHORT).show();
                            refreshData();
                            back.setVisibility(View.VISIBLE);
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


    private void refreshData() {
        left_awardlist = db.getItems(award_type);
    }


    @Override
    public void onBackPressed() {
    }
}
