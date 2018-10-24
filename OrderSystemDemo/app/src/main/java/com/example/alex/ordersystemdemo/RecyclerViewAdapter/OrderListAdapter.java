package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.API.Analyze.Analyze_Order;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.OrderDataPojo;
import com.example.alex.ordersystemdemo.OrderListDetailActivity;
import com.example.alex.ordersystemdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<OrderDataPojo> list;

    public OrderListAdapter(Context ctx, JSONObject jsonObject) {
        this.ctx = ctx;
        if (jsonObject != null) {
            list = new Analyze_Order().getOrder_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }

    }

    @Override
    public OrderListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
        return new OrderListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.s_name.setText(list.get(position).getS_name());
        holder.f_sum.setText(list.get(position).getF_sum());
        holder.f_content.setText(list.get(position).getF_content());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView s_name, f_sum, f_content;

        public RecycleHolder(View view) {
            super(view);
            s_name = view.findViewWithTag("s_name");
            f_sum = view.findViewWithTag("f_sum");
            f_content = view.findViewWithTag("f_content");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ctx.startActivity(new Intent(ctx, OrderListDetailActivity.class));
        }

    }

    public void setFilter(JSONObject jsonObject) {
        if (jsonObject != null) {
            list = new Analyze_Order().getOrder_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }

        notifyDataSetChanged();

    }
}
