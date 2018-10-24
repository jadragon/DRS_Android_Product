package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.API.Analyze.Analyze_Restaurant;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.StoreDataPojo;
import com.example.alex.ordersystemdemo.R;
import com.example.alex.ordersystemdemo.StoreDetailActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<StoreDataPojo> list;

    public StoreListAdapter(Context ctx, JSONObject jsonObject) {
        this.ctx = ctx;
        if (jsonObject != null) {
            list = new Analyze_Restaurant().getStore_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }
    }


    @Override
    public StoreListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storerlist, parent, false);
        return new StoreListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.time.setText(list.get(position).getTime());
        holder.address.setText(list.get(position).getAddress());
        if (list.get(position).getD_default().equals("1")) {
            holder.d_default.setVisibility(View.VISIBLE);
        } else {
            holder.d_default.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout content;
        TextView name, time, address;
        ImageView d_default;

        public RecycleHolder(View view) {
            super(view);
            name = view.findViewWithTag("name");
            time = view.findViewWithTag("time");
            address = view.findViewWithTag("address");
            content = view.findViewWithTag("content");
            d_default = view.findViewWithTag("d_default");
            content.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent=new Intent(ctx, StoreDetailActivity.class);
            intent.putExtra("s_id",list.get(position).getS_id());
            ctx.startActivity(intent);
        }

    }

    public void setFilter(JSONObject jsonObject) {
        if (jsonObject != null) {
            list = new Analyze_Restaurant().getStore_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
