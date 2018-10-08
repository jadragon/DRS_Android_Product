package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initRecylcerView();
    }






 /*
    private void initRecylcerView() {

        RecyclerView main_recylcerview;
        MainMenuAdapter mainMenuAdapter;
        main_recylcerview = findViewById(R.id.main_recylcerview);
        mainMenuAdapter=new MainMenuAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        int imagewidth=250;
        gridLayoutManager.setSpanCount(dm.widthPixels/imagewidth);
        main_recylcerview.setLayoutManager(gridLayoutManager);
        main_recylcerview.setAdapter(mainMenuAdapter);
    }
*/
}