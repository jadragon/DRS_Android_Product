package com.example.alex.lotteryapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.util.Random;

public class LotteryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnplay, btnstop, btnpause;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private int position;
    private SQLiteDatabaseHandler db;
    private String[] member = {"0    |    1    |    2    |    5", "2    |    5    |    4    |    7", "1    |    2    |    6    |    7", "6    |    8    |    9    |    7", "0    |    6    |    7    |    8", "1    |    2    |    8    |    7", "6    |    8    |    3    |    1", "2    |    5    |    8    |    9", "4    |    6    |    3    |    7", "1    |    2    |    8    |    5", "5    |    6    |    8    |    9", "3    |    4    |    5    |    8", "8    |    2    |    5    |    6", "2    |    5    |    7    |    8", "0    |    5    |    7    |    8", "9    |    2    |    1    |    2"};
    Random random = new Random();
    TextView number;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        db = new SQLiteDatabaseHandler(this);
        btnplay = this.findViewById(R.id.btnplay);
        btnstop = this.findViewById(R.id.btnstop);
        btnpause = this.findViewById(R.id.btnpause);

        btnstop.setOnClickListener(this);
        btnplay.setOnClickListener(this);
        btnpause.setOnClickListener(this);

        number = findViewById(R.id.number);
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
        surfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplay:
                play();
                number.setText((member[random.nextInt(member.length)]+""));
                break;

            case R.id.btnpause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                break;

            case R.id.btnstop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        // 先判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            // 如果正在播放我们就先保存这个播放位置
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
        super.onPause();
    }

    private void play() {
        try {
            surfaceView.setVisibility(View.VISIBLE);
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            String path = "android.resource://" + getPackageName() + "/" + R.raw.boom;
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
                    surfaceView.setVisibility(View.INVISIBLE);
                }
            });
            Toast.makeText(this, "开始播放！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
