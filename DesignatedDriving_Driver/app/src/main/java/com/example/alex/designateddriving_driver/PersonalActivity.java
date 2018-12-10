package com.example.alex.designateddriving_driver;

import android.os.Bundle;


public class PersonalActivity extends ToolbarActivity {
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar("個人資料", true);
    }

}
