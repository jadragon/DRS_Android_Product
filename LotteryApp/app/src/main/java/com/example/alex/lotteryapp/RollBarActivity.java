package com.example.alex.lotteryapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.util.ArrayList;
import java.util.Random;

public class RollBarActivity extends AppCompatActivity {
    private TextView title, txt_award, txt_left;
    private TextView switcher1, switcher2, switcher3;
    private Wheel wheel1, wheel2, wheel3;
    private Thread tread;
    // private boolean isStarted, one, two, three;
    public static final Random RANDOM = new Random();
    private SQLiteDatabaseHandler db;
    private View bar1, bar2, ball;
    private int bar1_visible_height, bar2_visible_height, limit_top, limit_bottom;
    private int status_height;

    private ArrayList<String> left_awardlist;
    boolean back = true;
    private static int LoadingTime;
    private MediaPlayer mediaPlayer;
    private int award;
    private int stage;
    private String[] types = {"頭獎", "二獎", "三獎", "四獎", "五獎", "六獎"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollbar);
        db = new SQLiteDatabaseHandler(this);
        stage = getIntent().getIntExtra("stage", 0);
        initMediaPlay();
        initView();
        initAwardType();
        initListener();
    }

    private void initMediaPlay() {
        mediaPlayer = new MediaPlayer();
        String path = "android.resource://" + getPackageName() + "/" + R.raw.music1;
        Uri uri = Uri.parse(path);
        try {
            // mHandler = new Handler();
            mediaPlayer.setDataSource(RollBarActivity.this, uri);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    private void initAwardType() {
        award = 5;
        left_awardlist = db.getItems(award, stage);
        while (left_awardlist.size() <= 0) {
            if (award == 0) {
                break;
            }
            award--;
            left_awardlist = db.getItems(award, stage);
        }

        switch (award) {
            case 0:
                LoadingTime = 8000;
                break;
            case 1:
                LoadingTime = 5000;
                break;
            case 2:
                LoadingTime = 5000;
                break;
            case 3:
                LoadingTime = 5000;
                break;
            case 4:
                LoadingTime = 5000;
                break;
            case 5:
                LoadingTime = 5000;
                break;

        }
        if (award == 0 && left_awardlist.size() == 0) {
            title.setText("");
            txt_award.setText("本階段已抽選完畢");
            txt_left.setText("");
        } else {
            title.setText(types[award]);
            txt_award.setText("獎項:" + db.getGift(award));
            txt_left.setText("剩餘名額" + left_awardlist.size());
        }

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
        int hole_lenth = screen_width / 12;

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

        //標題
        title = findViewById(R.id.title);
        params = title.getLayoutParams();
        params.height = logo_height / 3;
        ((FrameLayout.LayoutParams) params).topMargin = paddingTop;
        //機台
        params = findViewById(R.id.machine).getLayoutParams();
        params.height = machine_height;
        findViewById(R.id.machine).setPadding(0, 0, bar_marginRight, 0);
        ((FrameLayout.LayoutParams) params).topMargin = paddingTop + (main_height - machine_height) / 2;
        // findViewById(R.id.rollbar_layout).getLayoutParams().width = screen_width / 7;

        //拉桿洞
        params = findViewById(R.id.hole).getLayoutParams();
        params.width = hole_lenth;
        params.height = hole_lenth * 2;

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
        txt_award = findViewById(R.id.txt_award);
        params = txt_award.getLayoutParams();
        params.height = main_height / 9;
        ((FrameLayout.LayoutParams) params).setMargins(screen_width / 7 * 2, paddingTop + (main_height - machine_height) / 2, 0, 0);
        //獎項
        txt_left = findViewById(R.id.txt_left);
        params = txt_left.getLayoutParams();
        params.height = main_height / 18;
        ((FrameLayout.LayoutParams) params).setMargins(0, paddingTop + (main_height - machine_height) / 2 + main_height / 18, screen_width / 7 * 2, 0);
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
                        if (wheel1 != null) {
                            wheel1.stopWheel();
                            wheel2.stopWheel();
                            wheel3.stopWheel();
                        }
                        if (tread != null) {
                            tread.interrupt();
                        }
                        break;
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

    }

    private void startRoll() {
        if (left_awardlist.size() <= 0) {
            Toast.makeText(RollBarActivity.this, "此獎項已額滿", Toast.LENGTH_SHORT).show();
            return;
        }
        back = false;

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
                    Thread.sleep(LoadingTime);
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();
                    ArrayList<String> currentWinner = db.getCurrentWinners();
                    Thread.sleep(100);
                    int number;
                    if (stage != 5) {
                        number = randomInt();
                        while (number == 400 || currentWinner.indexOf(number + "") != -1) {
                            number = randomInt();
                        }
                    } else {
                        number = 400;
                    }
                    db.updateUserInfo(left_awardlist.get(0), number + "");
                    final String nowWinner = String.format("%03d", number);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switcher1.setText(nowWinner.charAt(0) + "");
                            switcher2.setText(nowWinner.charAt(1) + "");
                            switcher3.setText(nowWinner.charAt(2) + "");
                            initAwardType();
                            back = true;
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

    public static int randomInt() {
        return RANDOM.nextInt(999);
    }

    @Override
    public void onBackPressed() {
        if (back) {
            RollBarActivity.this.finish();
        }
    }
}
