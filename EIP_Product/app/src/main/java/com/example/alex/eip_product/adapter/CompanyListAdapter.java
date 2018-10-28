package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.GlobalVariable;
import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.fragment.Fragment_inspect_content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.RecycleHolder> {
    private ArrayList<Map<String, String>> list;
    private Context ctx;
    private GlobalVariable gv;

    public CompanyListAdapter(Context ctx) {
        this.ctx = ctx;
        gv = (GlobalVariable) ctx.getApplicationContext();
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("title", "鑫力來");
        map.put("company", "驗貨人員1");
        list.add(map);
        map = new HashMap<>();
        map.put("title", "勝萬");
        map.put("company", "驗貨人員1");
        list.add(map);
        map = new HashMap<>();
        map.put("title", "澳邦");
        map.put("company", "");
        list.add(map);
        map = new HashMap<>();
        map.put("title", "強生");
        map.put("company", "");
        list.add(map);
        map = new HashMap<>();
        map.put("title", "忠縣順生");
        map.put("company", "驗貨人員1");
        list.add(map);
        map = new HashMap<>();
        map.put("title", "華氏");
        map.put("company", "");
        list.add(map);

    }

    @Override
    public CompanyListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_companyitem, null);
        return new CompanyListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        holder.title.setText(list.get(position).get("title"));
        if (list.get(position).get("company").equals("")) {
            holder.company.setVisibility(View.INVISIBLE);
        } else {
            holder.company.setVisibility(View.VISIBLE);
            holder.company.setText(list.get(position).get("company"));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, company;

        public RecycleHolder(View view) {
            super(view);
            title = view.findViewWithTag("title");
            company = view.findViewWithTag("company");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment_inspect_content = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("inspect_content");
            if (fragment_inspect_content == null) {//判斷Fragment_inspect_content是否已存在
                fragment_inspect_content = new Fragment_inspect_content();
            }
            Bundle bundle = new Bundle();
            bundle.putString("name", list.get(getAdapterPosition()).get("title"));
            bundle.putString("date", gv.getCurrent_date());
            fragment_inspect_content.setArguments(bundle);
            ((MainActivity) ctx).switchFrament(fragment_inspect_content, "inspect_content");
            // ctx.startActivity(new Intent(ctx, InsepectOrderActivity.class));
        }
    }

}
