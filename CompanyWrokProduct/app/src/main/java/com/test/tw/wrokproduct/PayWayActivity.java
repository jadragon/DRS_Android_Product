package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ShopCartJsonData;

public class PayWayActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnFocusChangeListener, View.OnClickListener {
    Toolbar toolbar;
    TextView toolbar_title;
    String token;
    TextView payway_activity_txt_opay, payway_activity_txt_xmoney, payway_activity_txt_ymoney, payway_activity_txt_ewallet,
            payway_activity_txt_pname1, payway_activity_txt_info1, payway_activity_txt_pname2, payway_activity_txt_info2,
            payway_activity_txt_total;
    EditText payway_activity_edit_ekeyin, payway_activity_edit_xkeyin, payway_activity_edit_ykeyin;
    JSONObject json;
    ArrayList<Map<String, String>> data_list, pay_list;
    int xtrans, ytrans;
    int opay, xmoney, xkeyin, ymoney, ykeyin, ewallet, ekeyin, total;
    LinearLayout choice1, choice2;
    ImageView payway_activity_txt_isused1, payway_activity_txt_isused2;
    Animation animation;
    String pno;
Button payway_activity_btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payway);
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        gv.setToken("I0JN9@_fTxybt/YuH1j1Ceg==");
        token = gv.getToken();
        initID();
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getMemberPayments(token);
                data_list = AnalyzeShopCart.getMemberPaymentsData(json);
                pay_list = AnalyzeShopCart.getMemberPaymentsPay(json);
                xtrans = Integer.parseInt(pay_list.get(0).get("xtrans"));
                ytrans = Integer.parseInt(pay_list.get(0).get("ytrans"));
                opay = Integer.parseInt(pay_list.get(0).get("opay"));
                xmoney = Integer.parseInt(pay_list.get(0).get("xmoney"));
                ymoney = Integer.parseInt(pay_list.get(0).get("ymoney"));
                ewallet = Integer.parseInt(pay_list.get(0).get("ewallet"));
                ekeyin = Integer.parseInt(pay_list.get(0).get("ekeyin"));
                xkeyin = Integer.parseInt(pay_list.get(0).get("xkeyin"));
                ykeyin = Integer.parseInt(pay_list.get(0).get("ykeyin"));

                total = (opay - (xkeyin / xtrans) - (ykeyin / ytrans) - ekeyin);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        payway_activity_txt_isused1.setTag(data_list.get(0).get("pno"));
                        payway_activity_txt_isused2.setTag(data_list.get(1).get("pno"));
                        if(data_list.get(0).get("isused").equals("1")){
                            payway_activity_txt_isused1.setSelected(true);
                        } else if(data_list.get(1).get("isused").equals("1")){
                            payway_activity_txt_isused2.setSelected(true);
                        }
                        initText();
                        setAnimation();
                    }
                });
            }
        }).start();
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("付款方式");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initID() {
        //opay
        payway_activity_txt_opay = findViewById(R.id.payway_activity_txt_opay);
        //xmoney
        payway_activity_txt_xmoney = findViewById(R.id.payway_activity_txt_xmoney);
        //ymoney
        payway_activity_txt_ymoney = findViewById(R.id.payway_activity_txt_ymoney);
        //ewallet
        payway_activity_txt_ewallet = findViewById(R.id.payway_activity_txt_ewallet);
        //pname1
        payway_activity_txt_pname1 = findViewById(R.id.payway_activity_txt_pname1);
        //info1
        payway_activity_txt_info1 = findViewById(R.id.payway_activity_txt_info1);
        //pname2
        payway_activity_txt_pname2 = findViewById(R.id.payway_activity_txt_pname2);
        //info2
        payway_activity_txt_info2 = findViewById(R.id.payway_activity_txt_info2);
        //xkeyin
        payway_activity_edit_xkeyin = findViewById(R.id.payway_activity_edit_xkeyin);
        payway_activity_edit_xkeyin.setOnFocusChangeListener(this);
        payway_activity_edit_xkeyin.setOnEditorActionListener(this);
        //ykeyin
        payway_activity_edit_ykeyin = findViewById(R.id.payway_activity_edit_ykeyin);
        payway_activity_edit_ykeyin.setOnFocusChangeListener(this);
        payway_activity_edit_ykeyin.setOnEditorActionListener(this);
        //ekeyin
        payway_activity_edit_ekeyin = findViewById(R.id.payway_activity_edit_ekeyin);
        payway_activity_edit_ekeyin.setOnFocusChangeListener(this);
        payway_activity_edit_ekeyin.setOnEditorActionListener(this);

        //choice1
        choice1 = findViewById(R.id.choice1);
        //choice2
        choice2 = findViewById(R.id.choice2);
        //
        payway_activity_txt_isused1 = findViewById(R.id.payway_activity_txt_isused1);
        //
        payway_activity_txt_isused2 = findViewById(R.id.payway_activity_txt_isused2);
        //Total
        payway_activity_txt_total = findViewById(R.id.payway_activity_txt_total);
        payway_activity_txt_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = Integer.parseInt(editable.toString());
                if (number <= 0) {
                    choice1.setVisibility(View.INVISIBLE);
                    choice2.setVisibility(View.INVISIBLE);
                    pno = data_list.get(data_list.size() - 1).get("pno");

                } else {
                    choice1.setVisibility(View.VISIBLE);
                    choice2.setVisibility(View.VISIBLE);
                    if (payway_activity_txt_isused1.isSelected()) {
                        pno = payway_activity_txt_isused1.getTag().toString();
                    } else if (payway_activity_txt_isused2.isSelected()) {
                        pno = payway_activity_txt_isused2.getTag().toString();
                    } else {
                        pno = data_list.get(data_list.size() - 1).get("pno");
                    }
                }
            }
        });
        payway_activity_btn_confirm=findViewById(R.id.payway_activity_btn_confirm);
        payway_activity_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ShopCartJsonData().setMemberPayment(token,xkeyin,ykeyin,ekeyin,pno);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                }).start();

                Toast.makeText(PayWayActivity.this, pno, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initText() {
        payway_activity_txt_opay.setText(  ""+opay);
        payway_activity_txt_xmoney.setText("(餘額:"+xmoney + "點)");
        payway_activity_txt_ymoney.setText("(餘額:"+ymoney  + "點)");
        payway_activity_txt_ewallet.setText("(餘額:"+ewallet + "點)");
        payway_activity_txt_pname1.setText(data_list.get(0).get("pname") + "");
        payway_activity_txt_info1.setText(data_list.get(0).get("info") + "");
        payway_activity_txt_pname2.setText(data_list.get(1).get("pname") + "");
        payway_activity_txt_info2.setText(data_list.get(1).get("info") + "");
        payway_activity_edit_ekeyin.setText(ekeyin + "");
        payway_activity_edit_xkeyin.setText(xkeyin + "");
        payway_activity_edit_ykeyin.setText(ykeyin + "");
        payway_activity_txt_total.setText(""+total);
    }

    private void setAnimation() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_enter_anim_right);
        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choice1:
                if (!payway_activity_txt_isused1.isSelected()) {
                    choice1.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            payway_activity_txt_isused1.setSelected(true);
                            pno=payway_activity_txt_isused1.getTag().toString();
                        }
                    });
                    choice2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_enter_anim_left));
                    payway_activity_txt_isused2.setSelected(false);

                }
                break;
            case R.id.choice2:
                if (!payway_activity_txt_isused2.isSelected()) {
                    choice2.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            payway_activity_txt_isused2.setSelected(true);
                            pno=payway_activity_txt_isused2.getTag().toString();
                        }
                    });
                    choice1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_enter_anim_left));
                    payway_activity_txt_isused1.setSelected(false);

                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        CountTotal(textView);
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            TextView textView = ((TextView) view);
            CountTotal(textView);
        }
    }

    public void CountTotal(TextView textView) {
        int number = textView.getText().toString().equals("") ? 0 : Integer.parseInt(textView.getText().toString());
        switch (textView.getId()) {
            case R.id.payway_activity_edit_xkeyin:
                if (number <= xmoney && (opay - ((number - number % xtrans) / xtrans) - (ykeyin / ytrans) - ekeyin) >= 0) {
                    xkeyin = number - number % xtrans;
                    textView.setText(xkeyin + "");
                    total = opay - (xkeyin / xtrans) - (ykeyin / ytrans) - ekeyin;
                    payway_activity_txt_total.setText(total + "");
                } else {
                    textView.setText(xkeyin + "");
                }
                break;
            case R.id.payway_activity_edit_ykeyin:
                if (number <= ymoney && (opay - (xkeyin / xtrans) - ((number - number % ytrans) / ytrans) - ekeyin) >= 0) {
                    ykeyin = number - number % ytrans;
                    textView.setText(ykeyin + "");
                    total = opay - (xkeyin / xtrans) - (ykeyin / ytrans) - ekeyin;
                    payway_activity_txt_total.setText(total + "");
                } else {
                    textView.setText(ykeyin + "");
                }
                break;
            case R.id.payway_activity_edit_ekeyin:
                if (number <= ewallet && (opay - (xkeyin / xtrans) - (ykeyin / ytrans) - number) >= 0) {
                    ekeyin = number;
                    textView.setText(ekeyin + "");
                    total = opay - (xkeyin / xtrans) - (ykeyin / ytrans) - ekeyin;
                    payway_activity_txt_total.setText(total + "");
                } else {
                    textView.setText(ekeyin + "");
                }
                break;
        }
    }

}
