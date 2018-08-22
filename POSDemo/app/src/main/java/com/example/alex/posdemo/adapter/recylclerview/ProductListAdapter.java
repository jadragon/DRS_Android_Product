package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.ProductListPojo;
import library.AnalyzeJSON.Analyze_CountInfo;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<ProductListPojo> list;
    Analyze_CountInfo analyze_countInfo;

    public ProductListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        analyze_countInfo = new Analyze_CountInfo();
        list = analyze_countInfo.getSearch_Member_Order(json);
    }

    @Override
    public ProductListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_count_productlist, parent, false);
        return new ProductListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));
        holder.pname.setText(list.get(position).getPname());
        holder.pcode.setText(list.get(position).getPcode());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.fprice.setText(list.get(position).getFprice() + "");
        holder.price.setText(list.get(position).getPrice() + "");
        ArrayList<Integer> arrayList=new ArrayList<>();
        arrayList.add(0);
        for(int i=1;i<list.get(position).getSamount();i++){
            arrayList.add(i);
        }
        holder.samount.setAdapter(new ArrayAdapter(ctx,  android.R.layout.simple_list_item_1,arrayList));

        holder.sum.setText(list.get(position).getSum() + "");
        holder.note.setText(list.get(position).getNote());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        TextView delete, line, pname, pcode, color, size, fprice, price, sum, note;
        Spinner samount;
        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("background");
            delete = view.findViewWithTag("delete");
            line = view.findViewWithTag("line");
            pname = view.findViewWithTag("pname");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            fprice = view.findViewWithTag("fprice");
            price = view.findViewWithTag("price");
            samount = view.findViewWithTag("samount");
            sum = view.findViewWithTag("sum");
            note = view.findViewWithTag("note");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }


    public void setFilter(JSONObject json) {
        list = analyze_countInfo.getSearch_Member_Order(json);
        notifyDataSetChanged();

    }
}