package com.example.alex.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
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
    private int intY1, intY2, intY3;
    private DisplayMetrics dm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //    startActivityForResult(new Intent(this, AnimationActivity.class), 100);
        dm = getResources().getDisplayMetrics();
        initView();
        initY();
        initBall();
    }


    private void initView() {
        findViewById(R.id.logo).getLayoutParams().width = dm.widthPixels / 4;
        findViewById(R.id.logo).getLayoutParams().height = dm.heightPixels / 3;

        findViewById(R.id.machine).getLayoutParams().height = dm.heightPixels / 2;
        findViewById(R.id.machine).setPadding(0,0,dm.widthPixels / 16,0);
        findViewById(R.id.rollbar_layout).getLayoutParams().width = dm.widthPixels / 7;
        findViewById(R.id.hole).getLayoutParams().width=dm.widthPixels / 8;
        findViewById(R.id.hole).getLayoutParams().height=dm.widthPixels / 8;
        bar1 = findViewById(R.id.bar1);
        bar1.getLayoutParams().width=dm.widthPixels/16;
        //  ((LinearLayout.LayoutParams)bar1.getLayoutParams()).setMargins(0,dm.heightPixels / 16,0,0);
        bar2 = findViewById(R.id.bar2);
        bar2.getLayoutParams().width=dm.widthPixels/16;
        //  ((LinearLayout.LayoutParams)bar2.getLayoutParams()).setMargins(0,0,0,dm.heightPixels / 16);
        ball = findViewById(R.id.ball);
        ball.getLayoutParams().width= dm.widthPixels / 7;
        ball.getLayoutParams().height= dm.widthPixels / 7;
        ((FrameLayout.LayoutParams)ball.getLayoutParams()).setMargins(0,dm.heightPixels / 10, dm.widthPixels / 16,0);


        findViewById(R.id.switcher_layout).setPadding(dm.widthPixels / 16,dm.heightPixels / 8, 0,dm.heightPixels / 32);

        switcher1 = findViewById(R.id.switcher1);
        switcher2 = findViewById(R.id.switcher2);
        switcher3 = findViewById(R.id.switcher3);
        switcher1.setText(7 + "");
        switcher2.setText(7 + "");
        switcher3.setText(7 + "");
        txt_award = findViewById(R.id.txt_award);
        ((FrameLayout.LayoutParams)txt_award.getLayoutParams()).setMargins(dm.heightPixels / 2,dm.heightPixels / 4, 0,0);



    }

    private void initY() {
        intY1 = dm.heightPixels/4;
        intY2 =dm.heightPixels/2;
        intY3 = dm.heightPixels/8*7-dm.widthPixels/7;
    }

    private void initBall() {
        ball.setOnTouchListener(new View.OnTouchListener() {
            private float y1, y;    // 原本圖片存在的X,Y軸位置
            private int mx, my; // 圖片被拖曳的X ,Y軸距離長度

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.e("View", v.toString());
                switch (event.getAction()) {          //判斷觸控的動作

                    case MotionEvent.ACTION_DOWN:// 按下圖片時
                        y1 = v.getY();                  //觸控的X軸位置
                        y = event.getY();                  //觸控的Y軸位置

                    case MotionEvent.ACTION_MOVE:// 移動圖片時

                        //getX()：是獲取當前控件(View)的座標

                        //getRawX()：是獲取相對顯示螢幕左上角的座標
                        mx = (int) v.getX();
                        my = (int) (event.getRawY() - y);
                        if (my < y1 || my > intY3) {
                            return false;
                        }
                        v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                        if (my < intY1) {
                            bar1.setVisibility(View.VISIBLE);
                            bar2.setVisibility(View.INVISIBLE);
                        } else if (my > intY1 && my < intY2) {
                            bar1.setVisibility(View.INVISIBLE);
                            bar2.setVisibility(View.INVISIBLE);
                        } else if (my > intY2) {
                            bar2.setVisibility(View.VISIBLE);
                            bar1.setVisibility(View.INVISIBLE);
                        }

                        // Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
                        break;
                    case MotionEvent.ACTION_UP:
                        bar1.setVisibility(View.VISIBLE);
                        bar2.setVisibility(View.INVISIBLE);
                        if (my > intY2) {
                            startRoll();
                        }
                        v.layout(mx, (int) y1, mx + v.getWidth(), (int) (y1 + v.getHeight()));
                        break;
                }
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
