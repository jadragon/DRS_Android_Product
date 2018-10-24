package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.API.Analyze.Analyze_Restaurant;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.MenuPojo;
import com.example.alex.ordersystemdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.RecycleHolder> {
    private Context ctx;
    private DisplayMetrics dm;
    private ArrayList<MenuPojo> list;

    public MenuListAdapter(Context ctx, JSONObject jsonObject) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (jsonObject != null) {
            list = new Analyze_Restaurant().getMenu(jsonObject);
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public MenuListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menulist, parent, false);
        return new MenuListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.food.setText(list.get(position).getFood());
        holder.money.setText(list.get(position).getMoney());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView food, money;

        public RecycleHolder(View view) {
            super(view);
            food = view.findViewWithTag("food");
            money = view.findViewWithTag("money");
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject jsonObject) {
        if (jsonObject != null) {
            list = new Analyze_Restaurant().getMenu(jsonObject);
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();

    }
}
