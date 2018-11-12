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

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;

public class ReplyAppreciateActivity extends ToolbarActivity {
    private  MyCommentitemPojo myCommentitemPojo;
    private   EditText replyappreciate_comment;
    private   Button confirm;
    private  GlobalVariable gv;
    private  int type;

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
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        if (type == 0) {
                            return new OrderInfoJsonData().setMyComment(gv.getToken(), myCommentitemPojo.getMoino(), replyappreciate_comment.getText().toString());
                        } else {
                            return new StoreJsonData().setStoreComment(gv.getToken(), myCommentitemPojo.getMoino(), replyappreciate_comment.getText().toString());
                        }
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {

                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            setResult(520, getIntent());
                            finish();
                        } else {
                            new ToastMessageDialog(ReplyAppreciateActivity.this, AnalyzeUtil.getMessage(jsonObject)).confirm();
                        }

                    }
                });

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
