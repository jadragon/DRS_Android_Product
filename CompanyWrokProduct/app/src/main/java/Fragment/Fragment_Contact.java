package Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽.ContactActivity;
import com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽.WriteMailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import adapter.recyclerview.ContactRecyclerAdapter;
import library.GetJsonData.ContactJsonData;
import library.SQLiteDatabaseHandler;

public class Fragment_Contact extends Fragment {
    RecyclerView recyclerView;
    JSONObject json;
    CheckBox contact_allcheck;
    View v;
    ContactRecyclerAdapter adapter;
    int type;
    String token;
    ImageView contact_alldelete, contact_write;

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public void setType(int type) {
        this.type = type;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contact_layout, container, false);
        GlobalVariable gv = (GlobalVariable) getContext().getApplicationContext();
        token = gv.getToken();
        recyclerView = v.findViewById(R.id.contact_recyclerview);
        recyclerView.setHasFixedSize(true);
        //DB
        if (type == 1) {
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getContext());
            byte[] bytes = db.getPhotoImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            db.close();
            adapter = new ContactRecyclerAdapter(getContext(), json, bitmap);
        } else {
            adapter = new ContactRecyclerAdapter(getContext(), json);
        }
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
        recyclerView.addItemDecoration(decoration);
        contact_allcheck = v.findViewById(R.id.contact_allcheck);
        contact_allcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact_allcheck.isChecked()) {
                    adapter.setAllChecked();
                } else {
                    adapter.setAllNotCheck();
                }
            }
        });
        initWrite();
        initDelete();
        return v;
    }

    private void initWrite() {
        contact_write = v.findViewById(R.id.contact_write);
        contact_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WriteMailActivity.class));
            }
        });
    }

    private void initDelete() {
        contact_alldelete = v.findViewById(R.id.contact_alldelete);
        contact_alldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final JSONObject jsonObject = new ContactJsonData().delContact(token, adapter.getCheckedList());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (jsonObject.getBoolean("Success")) {
                                        ((ContactActivity) getContext()).setFilter();
                                        Toast.makeText(getContext(), "刪除成功", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void setFilter() {
        adapter.setFilter(json);
    }
}
