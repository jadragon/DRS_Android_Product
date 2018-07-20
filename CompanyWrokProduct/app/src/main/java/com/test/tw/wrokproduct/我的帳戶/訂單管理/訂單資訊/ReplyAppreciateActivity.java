package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MyCommentitemPojo;

import org.json.JSONException;
import org.json.JSONObject;

import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;
import library.JsonDataThread;

public class ReplyAppreciateActivity extends ToolbarActivity {
    MyCommentitemPojo myCommentitemPojo;
    EditText replyappreciate_comment;
    Button confirm;
    GlobalVariable gv;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_appreciate);
        gv = (GlobalVariable) getApplicationContext();
        type = getIntent().getIntExtra("type", 0);
        initToolbar(true, "我的回覆");
        myCommentitemPojo = (MyCommentitemPojo) getIntent().getSerializableExtra("myCommentitemPojo");
        initComponent();
        replyappreciate_comment = findViewById(R.id.replyappreciate_comment);
        confirm = findViewById(R.id.replyappreciate_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JsonDataThread() {
                    @Override
                    public JSONObject getJsonData() {
                        if (type == 0) {
                            return new OrderInfoJsonData().setMyComment(gv.getToken(), myCommentitemPojo.getMoino(), replyappreciate_comment.getText().toString());
                        } else {
                            return new StoreJsonData().setStoreComment(gv.getToken(), myCommentitemPojo.getMoino(), replyappreciate_comment.getText().toString());
                        }
                    }

                    @Override
                    public void runUiThread(JSONObject json) {
                        try {
                            if (json.getBoolean("Success")) {
                                setResult(520, getIntent());
                                finish();
                            } else {
                                new ToastMessageDialog(ReplyAppreciateActivity.this, json.getString("Message")).confirm();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
    }

    private void initComponent() {
        ImageLoader.getInstance().displayImage(myCommentitemPojo.getImg(), ((ImageView) findViewById(R.id.replyappreciate_img)));
        ((TextView) findViewById(R.id.replyappreciate_pname)).setText(myCommentitemPojo.getPname());
        ((TextView) findViewById(R.id.replyappreciate_format)).setText("規格:" + myCommentitemPojo.getColor() + " " + myCommentitemPojo.getSize());
        ((TextView) findViewById(R.id.replyappreciate_comdate)).setText(myCommentitemPojo.getComdate());
    }


}
