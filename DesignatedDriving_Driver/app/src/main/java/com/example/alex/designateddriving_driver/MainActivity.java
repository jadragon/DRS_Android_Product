package com.example.alex.designateddriving_driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView toolbar_txt_title;
    private Toolbar toolbar_main;
    private ActionBarDrawerToggle actionBarDrawerToggle_main;
    private SQLiteDatabaseHandler db;
    private GlobalVariable gv;
    private Button deposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GlobalVariable) getApplicationContext();
        db = new SQLiteDatabaseHandler(this);
        initToolbar();
        initButton();
    }

    private void initButton() {
        deposit = findViewById(R.id.deposit);
        deposit.setOnClickListener(this);
    }

    protected void initToolbar() {
        //Toolbar 建立
        toolbar_main = findViewById(R.id.toolbar);
        toolbar_txt_title = findViewById(R.id.toolbar_title);
        toolbar_txt_title.setText("全民代駕");
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setTitle(null);
        DrawerLayout drawerLayout_main = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle_main = new ActionBarDrawerToggle(this, drawerLayout_main, R.string.app_open, R.string.app_close);
        drawerLayout_main.addDrawerListener(actionBarDrawerToggle_main);
        actionBarDrawerToggle_main.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle_main.onOptionsItemSelected(item);
    }

    private void initNavigation() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.header_item1:
                        startActivity(new Intent(MainActivity.this, PersonalActivity.class));
                        break;
                    case R.id.header_item2:
                        startActivity(new Intent(MainActivity.this, OrderListActivity.class));
                        break;
                    case R.id.header_item3:
                        startActivity(new Intent(MainActivity.this, MyOrderListActivity.class));
                        break;
                }
            }
        };
        NavigationView navigation_view = findViewById(R.id.navigation_view);
        View header = navigation_view.getHeaderView(0);
        header.findViewById(R.id.header_item1).setOnClickListener(onClickListener);
        header.findViewById(R.id.header_item2).setOnClickListener(onClickListener);
        header.findViewById(R.id.header_item3).setOnClickListener(onClickListener);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item1:
                        Intent intent = new Intent(MainActivity.this, MyDepositActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item2:
                        intent = new Intent(MainActivity.this, MyEvaluationActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item3:
                        intent = new Intent(MainActivity.this, NewsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item4:
                        intent = new Intent(MainActivity.this, ListViewActivity.class);
                        intent.putExtra("type", "rookie");
                        startActivity(intent);
                        return true;
                    case R.id.menu_item5:
                        intent = new Intent(MainActivity.this, ListViewActivity.class);
                        intent.putExtra("type", "about");
                        startActivity(intent);
                        return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deposit:
                startActivity(new Intent(MainActivity.this, DepositActivity.class));
                break;
        }
    }
}
