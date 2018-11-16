package com.example.alex.lotteryapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

public class AnimationActivity extends AppCompatActivity {
    private Button btnplay;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private int position;
    private Spinner spinner;
    SQLiteDatabaseHandler db;
    boolean back = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        db = new SQLiteDatabaseHandler(this);
        initSpinner();
        btnplay = this.findViewById(R.id.btnplay);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back=false;
                play();
                surfaceView.setBackgroundColor(0);
                btnplay.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
        });
        initMediaPlay();
    }

    private void initMediaPlay() {
        mediaPlayer = new MediaPlayer();
        surfaceView = this.findViewById(R.id.surfaceView);
        // 设置SurfaceView自己不管理的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position > 0) {
                    try {
                        // 开始播放
                        play();
                        // 并直接从指定位置开始播放
                        mediaPlayer.seekTo(position);
                        position = 0;
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    private void play() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            String path = "android.resource://" + getPackageName() + "/" + R.raw.op;
            Uri uri = Uri.parse(path);
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            // 把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            // 播放
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    setResult(100, getIntent().putExtra("type", spinner.getSelectedItem() + ""));
                    finish();
                }
            });
        } catch (Exception e) {
        }
    }


    private void initSpinner() {
        spinner = findViewById(R.id.select_type);
        spinner.setAdapter(new ArrayAdapter<>(this,
                R.layout.spinner_style, db.getTypes()));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        if(back){
            Intent intent = new Intent(AnimationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
