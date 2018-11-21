package com.example.alex.lotteryapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private int position;
    private View select_menu;
    SQLiteDatabaseHandler db;
    boolean start = true;
    boolean back = true;
    private DisplayMetrics dm;
    private String[] types = {"頭獎", "二獎", "三獎", "四獎", "五獎", "六獎"};
    int typePosition;
    private TextView winner1, winner2, winner3, winner4, winner5, winner6;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        dm = getResources().getDisplayMetrics();
        db = new SQLiteDatabaseHandler(this);
        initSelectMenu();
        initMediaPlay();
    }

    private void initSelectMenu() {
        select_menu = findViewById(R.id.select_menu);
        winner1 = findViewById(R.id.winner1);
        winner2 = findViewById(R.id.winner2);
        winner3 = findViewById(R.id.winner3);
        winner4 = findViewById(R.id.winner4);
        winner5 = findViewById(R.id.winner5);
        winner6 = findViewById(R.id.winner6);
        new AsyncTask<Void, Void, ArrayList<ArrayList<String>>>() {
            @Override
            protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {
                return db.getAllWinnerNames();
            }

            @Override
            protected void onPostExecute(ArrayList<ArrayList<String>> datas) {
                winner1.setText(datas.get(0) + "");
                winner2.setText(datas.get(1) + "");
                winner3.setText(datas.get(2) + "");
                winner4.setText(datas.get(3) + "");
                winner5.setText(datas.get(4) + "");
                winner6.setText(datas.get(5) + "");
            }
        }.execute();
        findViewById(R.id.first).setOnClickListener(this);
        findViewById(R.id.secound).setOnClickListener(this);
        findViewById(R.id.third).setOnClickListener(this);
        findViewById(R.id.fourth).setOnClickListener(this);
        findViewById(R.id.fifth).setOnClickListener(this);
        findViewById(R.id.sixth).setOnClickListener(this);
    }

    private void initMediaPlay() {
        surfaceView = this.findViewById(R.id.surfaceView);
        surfaceView.getLayoutParams().height = dm.widthPixels / 16 * 9;
        // 设置SurfaceView自己不管理的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(this);
        // 设置需要播放的视频
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        select_menu.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null)
            thread.interrupt();
        mediaPlayer.reset();
        mediaPlayer.release();
        start = true;
        back = true;
    }

    @Override
    public void onBackPressed() {
        if (back) {
            /*
            Intent intent = new Intent(AnimationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (start) {
            start = false;
            back = false;
            switch (v.getId()) {
                case R.id.first:
                    typePosition = 0;
                    break;
                case R.id.secound:
                    typePosition = 1;
                    break;
                case R.id.third:
                    typePosition = 2;
                    break;
                case R.id.fourth:
                    typePosition = 3;
                    break;
                case R.id.fifth:
                    typePosition = 4;
                    break;
                case R.id.sixth:
                    typePosition = 5;
                    break;
            }

            select_menu.setVisibility(View.INVISIBLE);
            mediaPlayer.start();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AnimationActivity.this, RollBarActivity.class);
                                intent.putExtra("type", types[typePosition]);
                                switch (typePosition) {
                                    case 0:
                                        intent.putExtra("load", 8000);
                                        break;
                                    case 1:
                                        intent.putExtra("load", 6000);
                                        break;
                                    case 2:
                                        intent.putExtra("load", 5000);
                                        break;
                                    case 3:
                                        intent.putExtra("load", 3000);
                                        break;
                                    case 4:
                                        intent.putExtra("load", 1500);
                                        break;
                                    case 5:
                                        intent.putExtra("load", 1000);
                                        break;
                                }

                                startActivityForResult(intent, typePosition);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                winner1.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
            case 1:
                winner2.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
            case 2:
                winner3.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
            case 3:
                winner4.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
            case 4:
                winner5.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
            case 5:
                winner6.setText(db.getWinnerNames(types[requestCode]) + "");
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 把视频画面输出到SurfaceView
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(holder);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String path = "android.resource://" + getPackageName() + "/" + R.raw.op;
            Uri uri = Uri.parse(path);
            try {

                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
