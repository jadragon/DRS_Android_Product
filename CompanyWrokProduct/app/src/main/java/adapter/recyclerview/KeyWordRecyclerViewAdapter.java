package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.SearchResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyWordRecyclerViewAdapter extends RecyclerView.Adapter<KeyWordRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private Context ctx;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;

    public KeyWordRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        /*
        list = new ArrayList<>();
        list.add("熱門關鍵字");
        list.add("黑人問號?");
        list.add("我覺得可以");
        list.add("菜頭貴");
        list.add("嚇到吃手手");
        list.add("啊不就好棒棒");
        list.add("老司機");
        list.add("用愛發電");
        list.add("刷一波6");
        list.add("把你Bang不見");
        list.add("垃圾不分藍綠");
        list.add("這我一定吉");
        list.add("8+9");
        list.add("少時不讀書，長大當記者");
        list.add("87分不能再高了");
        list.add("倫家4女森欸");
        list.add("╰(⊙-⊙)╮佛心公司╭(⊙-⊙)╯佛心公司");
        list.add("94狂");
        list.add("查水表");
        list.add("假的，哎呀我的眼睛業障重啊！");
        list.add("滿滿的大！平！台！");
        list.add("你在大聲什麼啦 ");
        list.add("嚇死寶寶了");
        */
        if (json != null)
            initList(json);
        else
            list = new ArrayList<>();
    }

    private void initList(JSONObject json) {
        list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("title", "熱門關鍵字");
        list.add(map);
        try {
            JSONArray jsonArray = json.getJSONArray("Data");
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                map = new HashMap<>();
                map.put("pname", jsonObject.getString("pname"));
                map.put("title", jsonObject.getString("title"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public KeyWordRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            textView.setTextColor(Color.BLACK);
            return new KeyWordRecyclerViewAdapter.RecycleHolder(ctx, textView);
        }
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        //  layoutParams.setMargins((int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        textView.setPadding((int) (5 * dm.density), (int) (10 * dm.density), (int) (5 * dm.density), (int) (10 * dm.density));
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.WHITE);
        shape.setCornerRadius(10 * dm.density);
        shape.setStroke((int) (1 * dm.density), Color.RED);
        textView.setTextColor(Color.RED);
        textView.setBackgroundDrawable(shape);
        return new KeyWordRecyclerViewAdapter.RecycleHolder(ctx, textView);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText(list.get(position).get("title"));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView color, size;
        TextView text;
        Context ctx;

        public RecycleHolder(Context ctx, View view) {
            super(view);

            this.ctx = ctx;

            text = (TextView) view;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position > 0) {
                Intent intent = new Intent(ctx, SearchResultActivity.class);
                intent.putExtra("keyword", list.get(position).get("title"));
                ctx.startActivity(intent);
            }
        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            initList(json);
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
