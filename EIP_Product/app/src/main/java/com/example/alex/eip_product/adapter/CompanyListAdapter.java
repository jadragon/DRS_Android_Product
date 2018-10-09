package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.fragment.Fragment_inspect_content;

import java.util.ArrayList;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.RecycleHolder> {
    private ArrayList<String> list;
    private Context ctx;

    public CompanyListAdapter(Context ctx) {
        this.ctx = ctx;
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        list.add("鑫力來");
        list.add("勝萬");
        list.add("澳邦");
        list.add("強生");
        list.add("忠縣順生");
        list.add("華氏");

    }


    @Override
    public CompanyListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, null);
        return new CompanyListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        holder.title.setText(list.get(position));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public RecycleHolder(View view) {
            super(view);
            title = view.findViewWithTag("title");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment_inspect_content fragment_inspect_content=new Fragment_inspect_content();
            Bundle bundle=new Bundle();
            bundle.putString("date",  ((MainActivity)  ctx).getCurrent_date());
            fragment_inspect_content.setArguments(bundle);
            ((MainActivity)  ctx).switchFrament(fragment_inspect_content,"inspect_content");
           // ctx.startActivity(new Intent(ctx, InsepectOrderActivity.class));
        }
    }

}
