package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

import library.AnalyzeJSON.APIpojo.StockDataPojo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.Component.ToastMessageDialog;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<StockDataPojo> list;
    Analyze_StockInfo analyze_stockInfo;

    public StockListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        analyze_stockInfo = new Analyze_StockInfo();
        list = analyze_stockInfo.getStock_data(json);
    }

    @Override
    public StockListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stocklist, parent, false);
        return new StockListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));
        holder.store.setText(list.get(position).getStore());
        ImageLoader.getInstance().displayImage(list.get(position).getImg(), holder.img);
        holder.brand_title.setText(list.get(position).getBrand_title());
        holder.name.setText(list.get(position).getName());
        holder.pcode.setText(list.get(position).getPcode());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.fprice.setText(list.get(position).getFprice());
        holder.price.setText(list.get(position).getPrice());
        holder.total.setText(list.get(position).getTotal());
        holder.stotal.setText(list.get(position).getStotal());
        holder.flaw_total.setText(list.get(position).getFlaw_total());
    }

    @Override
    public int getItemCount() {
        // return list.size();
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        ImageView img;
        TextView line, store, brand_title, name, pcode, color, size, fprice, price, total, stotal, flaw_total;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("background");
            line = view.findViewWithTag("line");
            store = view.findViewWithTag("store");
            img = view.findViewWithTag("img");
            brand_title = view.findViewWithTag("brand_title");
            name = view.findViewWithTag("name");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            fprice = view.findViewWithTag("fprice");
            price = view.findViewWithTag("price");
            total = view.findViewWithTag("total");
            stotal = view.findViewWithTag("stotal");
            flaw_total = view.findViewWithTag("flaw_total");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject json) {
        list = new Analyze_StockInfo().getStock_data(json);
        notifyDataSetChanged();
    }

    public boolean setFilterMore(JSONObject json) {
        int presize = list.size();
        if (presize > 0) {
            if (json != null && analyze_stockInfo.getStock_data(json).size() > 0) {
                list.addAll(analyze_stockInfo.getStock_data(json));
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
}