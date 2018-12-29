package com.example.alex.eip_product.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.eip_product.R;

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
    private boolean type1, type2, type3, type4;
    private boolean item_clickable;
    private boolean item_addable;

    public KeyWordRecyclerViewAdapter(Context ctx, boolean type1, boolean type2, boolean type3, boolean type4, boolean item_clickable, boolean item_addable) {
        this.ctx = ctx;
        this.type1 = type1;
        this.type2 = type2;
        this.type3 = type3;
        this.type4 = type4;
        this.item_clickable = item_clickable;
        this.item_addable = item_addable;
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
        Map<String, String> map;
        ArrayList<String> arrayList;
        String[] type_description, fail_num;
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ctx);
        //type1
        if (type1) {
            arrayList = db.getFailedDescription(0);
            map = new HashMap<>();
            map.put("type", "" + TYPE_HEADER);
            map.put("description", ctx.getResources().getString(R.string.size));
            list.add(map);
            fail_num = ctx.getResources().getStringArray(R.array.type1);
            type_description = ctx.getResources().getStringArray(R.array.type1_description);
            for (int i = 0; i < type_description.length; i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", fail_num[i]);
                map.put("description", type_description[i]);
                list.add(map);
            }
            //新增
            for (int i = 0; i < arrayList.size(); i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", "A0");
                map.put("description", arrayList.get(i));
                list.add(map);
            }
            if (item_addable) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_OTHER);
                map.put("failed_type", "0");
                map.put("description", ctx.getResources().getString(R.string.other));
                list.add(map);
            }
        }
        //type2
        if (type2) {
            arrayList = db.getFailedDescription(1);
            map = new HashMap<>();
            map.put("type", "" + TYPE_HEADER);
            map.put("description", ctx.getResources().getString(R.string.function));
            list.add(map);
            fail_num = ctx.getResources().getStringArray(R.array.type2);
            type_description = ctx.getResources().getStringArray(R.array.type2_description);
            for (int i = 0; i < type_description.length; i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", fail_num[i]);
                map.put("description", type_description[i]);
                list.add(map);
            }
            //新增
            for (int i = 0; i < arrayList.size(); i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", "B0");
                map.put("description", arrayList.get(i));
                list.add(map);
            }
            if (item_addable) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_OTHER);
                map.put("failed_type", "1");
                map.put("description", ctx.getResources().getString(R.string.other));
                list.add(map);
            }
        }
        //type3
        if (type3) {
            arrayList = db.getFailedDescription(2);
            map = new HashMap<>();
            map.put("type", "" + TYPE_HEADER);
            map.put("description", ctx.getResources().getString(R.string.surface));
            list.add(map);
            fail_num = ctx.getResources().getStringArray(R.array.type3);
            type_description = ctx.getResources().getStringArray(R.array.type3_description);
            for (int i = 0; i < type_description.length; i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", fail_num[i]);
                map.put("description", type_description[i]);
                list.add(map);
            }
            //新增
            for (int i = 0; i < arrayList.size(); i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", "C0");
                map.put("description", arrayList.get(i));
                list.add(map);
            }
            if (item_addable) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_OTHER);
                map.put("failed_type", "2");
                map.put("description", ctx.getResources().getString(R.string.other));
                list.add(map);
            }
        }
        //type4
        if (type4) {
            arrayList = db.getFailedDescription(3);
            map = new HashMap<>();
            map.put("type", "" + TYPE_HEADER);
            map.put("description", ctx.getResources().getString(R.string.packaging));
            list.add(map);
            fail_num = ctx.getResources().getStringArray(R.array.type4);
            type_description = ctx.getResources().getStringArray(R.array.type4_description);
            for (int i = 0; i < type_description.length; i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", fail_num[i]);
                map.put("description", type_description[i]);
                list.add(map);
            }
            //新增
            for (int i = 0; i < arrayList.size(); i++) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_ITEM);
                map.put("select", "0");
                map.put("fail_num", "D0");
                map.put("description", arrayList.get(i));
                list.add(map);
            }
            if (item_addable) {
                map = new HashMap<>();
                map.put("type", "" + TYPE_OTHER);
                map.put("failed_type", "3");
                map.put("description", ctx.getResources().getString(R.string.other));
                list.add(map);
            }
        }
    }

    @Override
    public KeyWordRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            textView.setTextColor(Color.BLACK);
            return new KeyWordRecyclerViewAdapter.RecycleHolder(textView, viewType);
        }

        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.setMargins((int) (20 * dm.density), (int) (20 * dm.density), (int) (20 * dm.density), (int) (20 * dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        textView.setPadding((int) (20 * dm.density), (int) (10 * dm.density), (int) (20 * dm.density), (int) (10 * dm.density));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        if (viewType == TYPE_ITEM) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10 * dm.density);
            shape.setStroke((int) (1 * dm.density), Color.RED);
            shape.setColor(Color.WHITE);
            textView.setTextColor(Color.RED);
            textView.setBackgroundDrawable(shape);
        } else if (viewType == TYPE_OTHER) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10 * dm.density);
            shape.setStroke((int) (1 * dm.density), Color.WHITE);
            shape.setColor(Color.RED);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundDrawable(shape);
        }
        return new KeyWordRecyclerViewAdapter.RecycleHolder(textView, viewType);
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

        public RecycleHolder(View view, int viewType) {
            super(view);
            text = (TextView) view;
            if (item_clickable) {
                itemView.setOnClickListener(this);
            } else if (item_addable && viewType == TYPE_OTHER) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                View customerview = LayoutInflater.from(ctx).inflate(R.layout.add_faild_reason, null);
                builder.setView(customerview);
                final AlertDialog alertDialog = builder.create();
                final EditText editText = customerview.findViewById(R.id.add_failed_reason_edit);
                Button cancel = customerview.findViewById(R.id.add_failed_reason_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                Button confirm = customerview.findViewById(R.id.add_failed_reason_confrim);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editText.getText().toString().equals("")) {
                            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ctx);
                            db.addFailTableItem(Integer.parseInt(list.get(position).get("failed_type")), editText.getText().toString());
                            db.close();
                            setFilter();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
                return;
            }
        }
    }

    public String getSelectItems() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("type").equals("" + TYPE_ITEM) && list.get(i).get("select").equals("1")) {
                builder.append("，" + list.get(i).get("description"));
            }
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    public ArrayList<String> getSelectFailNums() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("type").equals("" + TYPE_ITEM) && list.get(i).get("select").equals("1")) {
                arrayList.add(list.get(i).get("fail_num"));
            }
        }
        return arrayList;
    }

    public ArrayList<String> getSelectFailDescription() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("type").equals("" + TYPE_ITEM) && list.get(i).get("select").equals("1")) {
                arrayList.add(list.get(i).get("description"));
            }
        }
        return arrayList;
    }

    public void setFilter() {
        initList();
        notifyDataSetChanged();
    }

}
