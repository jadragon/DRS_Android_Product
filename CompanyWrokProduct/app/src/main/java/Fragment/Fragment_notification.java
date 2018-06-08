package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.ShopCartActivity;

import Util.StringUtil;

public class Fragment_notification extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    View v;
    String token;
    GlobalVariable gv;
    TextView notification_txt_activity, notification_txt_chaw, notification_txt_store, notification_txt_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        gv = (GlobalVariable) getActivity().getApplicationContext();
        initToolbar();
        token = gv.getToken();
        initTextView();
        v.findViewById(R.id.n1).setOnClickListener(this);
        v.findViewById(R.id.n2).setOnClickListener(this);
        v.findViewById(R.id.n3).setOnClickListener(this);
        v.findViewById(R.id.n4).setOnClickListener(this);
        return v;
    }

    private void initTextView() {
        notification_txt_activity = v.findViewById(R.id.notification_txt_activity);
        notification_txt_activity.setText(Html.fromHtml(StringUtil.htmlFormat("再天將會有一個大活動",30+"","d40000",2)));
        notification_txt_chaw = v.findViewById(R.id.notification_txt_chaw);
        notification_txt_chaw.setText(Html.fromHtml(StringUtil.htmlFormat("您尚有則新活動",5+"","d40000",3)));
        notification_txt_store = v.findViewById(R.id.notification_txt_store);
        notification_txt_store.setText(Html.fromHtml(StringUtil.htmlFormat("您尚有則新活動","10000000000000000","d40000",3)));
        notification_txt_list = v.findViewById(R.id.notification_txt_list);
        notification_txt_list.setText(Html.fromHtml(StringUtil.htmlFormat("您尚有則新活動",12+"","d40000",3)));
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = v.findViewById(R.id.include_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        ((TextView) v.findViewById(R.id.include_toolbar_title)).setText("通知");

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.pc_content_menu, menu);
        menu.getItem(1).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //會員區
        if (item.getItemId() == R.id.pccontent_menu_shopcart) {
            if (gv.getToken() != null)
                startActivity(new Intent(getActivity(), ShopCartActivity.class));
            else
                Toast.makeText(getContext(), "請先做登入動作", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            //  setFilter();
            //网络数据刷新
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.n1:
                break;
            case R.id.n2:
                break;
            case R.id.n3:
                break;
            case R.id.n4:
                break;
        }

    }
}
