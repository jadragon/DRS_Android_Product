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

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragment.Fragment_Contact;
import library.GetJsonData.ContactJsonData;

public class ContactActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager contact_viewpager;
    TabLayout tabLayout;
    List<Fragment> fragmentList;
    String[] mTabtitle = {"收件夾", "寄信備份"};
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        initToolbar();
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        contact_viewpager = findViewById(R.id.contact_viewpager);
        fragmentList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json1 = new ContactJsonData().getContact(token, 0, 1);
                final JSONObject json2 = new ContactJsonData().getContact(token, 1, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment_Contact fragment_contact = new Fragment_Contact();
                        fragment_contact.setJson(json1);
                        fragmentList.add(fragment_contact);
                        fragment_contact = new Fragment_Contact();
                        fragment_contact.setJson(json2);
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
}
