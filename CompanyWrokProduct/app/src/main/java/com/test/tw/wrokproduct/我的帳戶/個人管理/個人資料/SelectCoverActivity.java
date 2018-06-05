package com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import java.util.ArrayList;
import java.util.List;

import library.SQLiteDatabaseHandler;

public class SelectCoverActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    List<ImageView> imgList;
    private DisplayMetrics dm;
    SQLiteDatabaseHandler db;
    String[] bg = {"斜魚格調", "冷冽性格", "插圖時間", "紫炫幾何", "漫遊星球", "繽紛生活"};
    int[] ag = {R.drawable.member_bg2, R.drawable.member_bg1, R.drawable.member_bg3, R.drawable.member_bg4, R.drawable.member_bg5, R.drawable.member_bg6};
    int coverbg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgList = new ArrayList<>();
        dm = getResources().getDisplayMetrics();
        db = new SQLiteDatabaseHandler(getApplicationContext());
        setContentView(R.layout.activity_select_cover);
        initToolbar();
        initImageView();
        try{
            coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
        }catch (Exception e){
            coverbg=0;
        }

        for (int i = 0; i < bg.length; i++) {
            if (ag[i]==coverbg) {
                imgList.get(i).setImageResource(R.drawable.select_coverbg);
                break;
            }
        }
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("修改封面");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initImageView() {
        int truewidth = (int) (dm.widthPixels - 30 * dm.density);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(truewidth, truewidth / 3);
        imgList.add((ImageView) findViewById(R.id.select_1));
        imgList.add((ImageView) findViewById(R.id.select_2));
        imgList.add((ImageView) findViewById(R.id.select_3));
        imgList.add((ImageView) findViewById(R.id.select_4));
        imgList.add((ImageView) findViewById(R.id.select_5));
        imgList.add((ImageView) findViewById(R.id.select_6));
        for (ImageView imageView : imgList) {
            imageView.setLayoutParams(params);
            imageView.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        int position = imgList.indexOf(view);
        for (ImageView imageView : imgList) {
            imageView.setImageResource(0);
        }
        imgList.get(position).setImageResource(R.drawable.select_coverbg);
        db.updateBackground(ag[position] + "");
        Log.e("bg", db.getMemberDetail().get("background") + "");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
