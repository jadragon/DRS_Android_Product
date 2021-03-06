package com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.Fragment.Fragment_Contact;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.GetJsonData.ContactJsonData;

public class ContactActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager contact_viewpager;
    private TabLayout tabLayout;
    private List<Fragment_Contact> fragmentList;
    private String[] mTabtitle = {"收件夾", "寄信備份"};
    private JSONObject json1, json2;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        gv = ((GlobalVariable) getApplicationContext());
        initToolbar();
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        contact_viewpager = findViewById(R.id.contact_viewpager);
        fragmentList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new ContactJsonData().getContact(gv.getToken(), 0, 1);
                json2 = new ContactJsonData().getContact(gv.getToken(), 1, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment_Contact fragment_contact = new Fragment_Contact();
                        fragment_contact.setJson(json1);
                        fragment_contact.setType(0);
                        fragmentList.add(fragment_contact);
                        fragment_contact = new Fragment_Contact();
                        fragment_contact.setJson(json2);
                        fragment_contact.setType(1);
                        fragmentList.add(fragment_contact);
                        contact_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                            @Override
                            public Fragment getItem(int position) {
                                return fragmentList.get(position);
                            }

                            @Override
                            public int getCount() {
                                return fragmentList.size();
                            }

                            @Override
                            public CharSequence getPageTitle(int position) {
                                return mTabtitle[position];
                            }
                        });
                        tabLayout = findViewById(R.id.contact_tablayout);
                    }
                });
            }
        }).start();

    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("聯絡劦譽");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setFilter();
    }

    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new ContactJsonData().getContact(gv.getToken(), 0, 1);
                json2 = new ContactJsonData().getContact(gv.getToken(), 1, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragmentList.get(0).setJson(json1);
                        fragmentList.get(0).setFilter();
                        fragmentList.get(1).setJson(json2);
                        fragmentList.get(1).setFilter();
                    }
                });
            }
        }).start();
    }
}
