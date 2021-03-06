package com.example.alex.posdemo.adapter.recylclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.Distribution2Pojo;
import library.AnalyzeJSON.APIpojo.DistributionContentPojo;
import library.AnalyzeJSON.Analyze_DistributionInfo;
import library.Component.ToastMessageDialog;

public class DistributionListAdapter extends RecyclerView.Adapter<DistributionListAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<DistributionContentPojo> list;
    ArrayList<ArrayList<Distribution2Pojo>> storeList;
    Analyze_DistributionInfo analyze_distributionInfo;
    int width;

    public DistributionListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        width = (int) (((Activity) ctx).findViewById(R.id.content).getWidth() - 90 * dm.density * 4);
        analyze_distributionInfo = new Analyze_DistributionInfo();
        list = analyze_distributionInfo.getDistributionContent(json);
        storeList = analyze_distributionInfo.getDistribution2Pojo(json);
    }

    @Override
    public DistributionListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         <TextView
         android:layout_width="80dp"
         android:layout_height="match_parent"
         android:gravity="center"
         android:tag="111111"
         android:text="中壢\n大江店"
         android:textColor="@color/black"
         android:textSize="12sp" />
         * */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (80 * dm.density), LinearLayout.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distributionlist, parent, false);
        LinearLayout linearLayout = view.findViewWithTag("title_layout");
        linearLayout.getLayoutParams().width = width;
        linearLayout = view.findViewWithTag("store_layout");
        TextView textView;
        for (int i = 0; i < storeList.get(0).size(); i++) {
            textView = new TextView(ctx);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ctx.getResources().getColor(R.color.black));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setLayoutParams(params);
            textView.setTag("store" + i);
            linearLayout.addView(textView);
        }
        return new DistributionListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));

        holder.brand_title.setText(list.get(position).getBrand_title());
        ImageLoader.getInstance().displayImage(list.get(position).getImg(), holder.img);
        holder.name.setText(list.get(position).getName());
        holder.pcode.setText(list.get(position).getPcode());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.stotal.setText(list.get(position).getStotal());

        for (int i = 0; i < storeList.get(position).size(); i++) {
            holder.stores.get(i).setText(storeList.get(position).get(i).getTotal() + "");
        }
    }

    @Override
    public int getItemCount() {
        // return list.size();
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        ImageView img;
        TextView line, brand_title, name, pcode, color, size, stotal;
        ArrayList<TextView> stores;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("background");
            line = view.findViewWithTag("line");
            brand_title = view.findViewWithTag("brand_title");
            img = view.findViewWithTag("img");
            name = view.findViewWithTag("name");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            stotal = view.findViewWithTag("stotal");
            stores = new ArrayList<>();
            for (int i = 0; i < storeList.get(0).size(); i++) {
                stores.add((TextView) (view.findViewWithTag("store" + i)));
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject json) {
        list = analyze_distributionInfo.getDistributionContent(json);
        storeList = analyze_distributionInfo.getDistribution2Pojo(json);
        notifyDataSetChanged();
    }

    public boolean setFilterMore(JSONObject json) {
        int presize = list.size();
        if (presize > 0) {
            if (json != null && analyze_distributionInfo.getDistributionContent(json).size() > 0) {
                list.addAll(analyze_distributionInfo.getDistributionContent(json));
                storeList.addAll(analyze_distributionInfo.getDistribution2Pojo(json));
                notifyItemInserted(presize + 1);
                //  notifyItemChanged(presize + 1, itemsList.size() + 1);
                return true;
            } else {
                new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).show("已無更多資料");
                return false;
            }
        }
        return false;
    }

    /*
    public int[] getTotal() {
        int[] array = new int[storeList.get(0).size()];
        for (ArrayList<Distribution2Pojo> distribution2PojoArrayList : storeList) {
            for (int i = 0; i < distribution2PojoArrayList.size(); i++) {
                array[i] += distribution2PojoArrayList.get(i).getTotal();
            }
        }
        return array;
    }
    */
}