package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.alex.eip_product.adapter.MainMenuAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView main_recylcerview;
    MainMenuAdapter mainMenuAdapter;
    private DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = getResources().getDisplayMetrics();
        initRecylcerView();
    }

    private void initRecylcerView() {
        main_recylcerview = findViewById(R.id.main_recylcerview);
        mainMenuAdapter=new MainMenuAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        int imagewidth=129;
        gridLayoutManager.setSpanCount(dm.widthPixels/imagewidth);
        main_recylcerview.setLayoutManager(gridLayoutManager);
        main_recylcerview.setAdapter(mainMenuAdapter);
    }

}