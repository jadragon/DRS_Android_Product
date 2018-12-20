package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import tw.com.lccnet.app.designateddriving.RecyclerAdapter.CountListAdapter;

public class CallNow5_CountActivity extends ToolbarActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CountListAdapter countListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow5_count);
        initToolbar("結帳", false);
        initRecylcerView();
    }


    private void initRecylcerView() {
        swipeRefreshLayout = findViewById(R.id.include_swipe_refresh);
        swipeRefreshLayout.setEnabled(false);
        RecyclerView recyclerView = findViewById(R.id.include_recyclerview);
        countListAdapter = new CountListAdapter(this);
        recyclerView.setAdapter(countListAdapter);
    }

    @Override
    public void onBackPressed() {
    }
}
