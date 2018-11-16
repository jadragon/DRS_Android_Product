package com.example.alex.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView txt_total;
    TextView switcher1, switcher2, switcher3, switcher4;
    private Wheel wheel1, wheel2, wheel3, wheel4;
    private Thread tread;
    // private boolean isStarted, one, two, three;
    public static final Random RANDOM = new Random();
    View bar1, bar2, ball;
    int intY1, intY2, intY3;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = getResources().getDisplayMetrics();
        initY();
        initBall();
        switcher1 = findViewById(R.id.switcher1);
        switcher2 = findViewById(R.id.switcher2);
        switcher3 = findViewById(R.id.switcher3);
        switcher4 = findViewById(R.id.switcher4);
        txt_total = findViewById(R.id.total_count);

        switcher1.setText(7 + "");
        switcher2.setText(7 + "");
        switcher3.setText(7 + "");
        switcher4.setText(7 + "");
    }



    private void initY() {
        intY1 = (int) (180 * dm.density + 80 * dm.density);
        intY2 = intY1 + (int) (140 * dm.density);
        intY3 = intY2 + (int) (80 * dm.density);
    }

    private void initBall() {
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        ball = findViewById(R.id.ball);
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
                        if (my < y1 || my > intY3) {
                            return false;
                        }
                        v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                        // Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
                        break;
                    case MotionEvent.ACTION_UP:
                        if (my > intY2) {
                            startRoll();
                        }
                        bar1.setVisibility(View.VISIBLE);
                        bar2.setVisibility(View.INVISIBLE);
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
        wheel4 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switcher4.setText(img + "");
                    }
                });
            }
        }, randomLong(50, 100), 0);
        wheel1.start();
        wheel2.start();
        wheel3.start();
        wheel4.start();
        tread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();
                    wheel4.stopWheel();
                    Thread.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_total.setText("得獎人員:" + switcher1.getText().toString() + switcher2.getText().toString() + switcher3.getText().toString() + switcher4.getText().toString());
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
