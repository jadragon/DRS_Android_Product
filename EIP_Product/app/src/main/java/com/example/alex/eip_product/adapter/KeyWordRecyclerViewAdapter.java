package com.example.alex.eip_product.adapter;

import android.content.Context;
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
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.SQLiteDatabaseHandler;

public class KeyWordRecyclerViewAdapter extends RecyclerView.Adapter<KeyWordRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_OTHER = 2;
    private Context ctx;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;

    public KeyWordRecyclerViewAdapter(Context ctx) {
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
        initList();

    }

    private void initList() {
        list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("type", "" + TYPE_HEADER);
        map.put("description", "尺寸");
        list.add(map);
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ctx);
        ArrayList<String> arrayList = db.getFailedDescription(0);
        for (int i = 0; i < arrayList.size(); i++) {
            map = new HashMap<>();
            map.put("type", "" + TYPE_ITEM);
            map.put("select", "0");
            map.put("description", arrayList.get(i));
            list.add(map);
        }
        map = new HashMap<>();
        map.put("type", "" + TYPE_OTHER);
        map.put("description", "其他");
        list.add(map);
        map = new HashMap<>();
        map.put("type", "" + TYPE_HEADER);
        map.put("description", "功能");
        list.add(map);
        arrayList = db.getFailedDescription(1);
        for (int i = 0; i < arrayList.size(); i++) {
            map = new HashMap<>();
            map.put("type", "" + TYPE_ITEM);
            map.put("select", "0");
            map.put("description", arrayList.get(i));
            list.add(map);
        }
        map = new HashMap<>();
        map.put("type", "" + TYPE_OTHER);
        map.put("description", "其他");
        list.add(map);
        map = new HashMap<>();
        map.put("type", "" + TYPE_HEADER);
        map.put("description", "表面");
        list.add(map);
        arrayList = db.getFailedDescription(2);
        for (int i = 0; i < arrayList.size(); i++) {
            map = new HashMap<>();
            map.put("type", "" + TYPE_ITEM);
            map.put("select", "0");
            map.put("description", arrayList.get(i));
            list.add(map);
        }
        map = new HashMap<>();
        map.put("type", "" + TYPE_OTHER);
        map.put("description", "其他");
        list.add(map);
        map = new HashMap<>();
        map.put("type", "" + TYPE_HEADER);
        map.put("description", "包裝");
        list.add(map);
        arrayList = db.getFailedDescription(3);
        for (int i = 0; i < arrayList.size(); i++) {
            map = new HashMap<>();
            map.put("type", "" + TYPE_ITEM);
            map.put("select", "0");
            map.put("description", arrayList.get(i));
            list.add(map);
        }
        map = new HashMap<>();
        map.put("type", "" + TYPE_OTHER);
        map.put("description", "其他");
        list.add(map);
    }

    @Override
    public KeyWordRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textView.setTextColor(Color.BLACK);
            return new KeyWordRecyclerViewAdapter.RecycleHolder(ctx, textView);
        }
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.setMargins((int) (20 * dm.density), (int) (20 * dm.density), (int) (20 * dm.density), (int) (20 * dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        textView.setPadding((int) (20 * dm.density), (int) (20* dm.density), (int) (20 * dm.density), (int) (20 * dm.density));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10 * dm.density);
        shape.setStroke((int) (1 * dm.density), Color.RED);
        shape.setColor(Color.WHITE);
        textView.setTextColor(Color.RED);
        textView.setBackgroundDrawable(shape);
        return new KeyWordRecyclerViewAdapter.RecycleHolder(ctx, textView);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

        holder.text.setText(list.get(position).get("description"));
        if (getItemViewType(position) == TYPE_ITEM) {
            ((GradientDrawable) holder.text.getBackground()).setColor(Color.GREEN);
            if (list.get(position).get("select").equals("1")) {
                ((GradientDrawable) holder.text.getBackground()).setColor(Color.GREEN);
            } else {
                ((GradientDrawable) holder.text.getBackground()).setColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).get("type").equals("" + TYPE_HEADER))
            return TYPE_HEADER;
        if (list.get(position).get("type").equals("" + TYPE_OTHER))
            return TYPE_OTHER;
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
            if (KeyWordRecyclerViewAdapter.this.getItemViewType(position) == TYPE_ITEM) {

                if (list.get(position).get("select").equals("0")) {
                    list.get(position).put("select", "1");
                } else {
                    list.get(position).put("select", "0");
                }
                notifyDataSetChanged();
                return;
            }

            if (KeyWordRecyclerViewAdapter.this.getItemViewType(position) == TYPE_OTHER) {
                Toast.makeText(ctx, "其他", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            initList();
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
