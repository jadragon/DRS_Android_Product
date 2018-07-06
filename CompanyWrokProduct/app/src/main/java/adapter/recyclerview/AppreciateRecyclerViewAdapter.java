package adapter.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.Item;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppreciateRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private Context ctx;
    private List<Item> items;
    GlobalVariable gv;

    public AppreciateRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {
        } else {
        }

        //初始化checkbox
        initItems();
    }

    public void initItems() {
        items = new ArrayList<>();
        Item item;
        item = new Item();
        item.setType(TYPE_HEADER);
        items.add(item);
        for (int i = 0; i < 5; i++) {
            //TYPE_CONTENT
            item = new Item();
            item.setType(TYPE_CONTENT);
            items.add(item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setGravity(Gravity.CENTER);
            return new AppreciateRecyclerViewAdapter.HeaderHolder(ctx, textView);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_appreciate, parent, false);
        return new AppreciateRecyclerViewAdapter.ContentHolder(ctx, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_HEADER) {
                ((HeaderHolder) holder).textView.setText("溫馨提醒,您可再2次修改評價");
            } else if (getItemViewType(position) == TYPE_CONTENT) {

            }
        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
            int type = (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type) {
                case 0:
                    if (getItemViewType(position) == TYPE_HEADER) {

                    } else if (getItemViewType(position) == TYPE_CONTENT) {

                    }
                    break;
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == TYPE_HEADER)
            return TYPE_HEADER;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView textView;

        public HeaderHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            textView = ((TextView) view);
        }

    }

    class ContentHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ImageView[] stars;
        LinearLayout linearLayout;
        float[] starsX;

        public ContentHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            linearLayout = view.findViewById(R.id.appreciate_srars_layout);

            starsX = new float[5];
            stars = new ImageView[5];
            stars[0] = view.findViewById(R.id.appreciate_srar1);
            stars[1] = view.findViewById(R.id.appreciate_srar2);
            stars[2] = view.findViewById(R.id.appreciate_srar3);
            stars[3] = view.findViewById(R.id.appreciate_srar4);
            stars[4] = view.findViewById(R.id.appreciate_srar5);
            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float x = 0;
                    float y = 0;
                    starsX[0] = stars[0].getX();
                    starsX[1] = stars[1].getX();
                    starsX[2] = stars[2].getX();
                    starsX[3] = stars[3].getX();
                    starsX[4] = stars[4].getX();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //当手指按下的时候
                        x = motionEvent.getX();
                        for (int i = starsX.length - 1; i >= 0; i--) {
                            if (x > starsX[i]) {
                                for (int j = i; j >= 0; j--) {
                                    stars[j].setImageResource(R.mipmap.star_on);
                                }
                                for (int j = i + 1; j < starsX.length; j++) {
                                    stars[j].setImageResource(R.mipmap.star_off);
                                }
                                break;
                            }
                        }
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        //当手指离开的时候
                        x = motionEvent.getX();
                        for (int i = starsX.length - 1; i >= 0; i--) {
                            if (x > starsX[i]) {
                                for (int j = i; j >= 0; j--) {
                                    stars[j].setImageResource(R.mipmap.star_on);
                                }
                                for (int j = i + 1; j < starsX.length; j++) {
                                    stars[j].setImageResource(R.mipmap.star_off);
                                }
                                break;
                            }
                        }
                    }
                    Log.e("touch", x + ":" + y);
                    return false;
                }
            });
        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {

        } else {
        }
        initItems();
        notifyDataSetChanged();
    }

}
