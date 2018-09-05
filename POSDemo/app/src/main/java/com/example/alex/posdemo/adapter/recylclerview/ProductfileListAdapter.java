package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.ProductfilePojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_ProductfileInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.ProductfileApi;

public class ProductfileListAdapter extends RecyclerView.Adapter<ProductfileListAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<ProductfilePojo> list;
    Analyze_ProductfileInfo analyze_productfileInfo;
    ProductfileApi productfileApi;
    private UserInfo userInfo;

    public ProductfileListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        userInfo = (UserInfo) ctx.getApplicationContext();
        productfileApi = new ProductfileApi();
        analyze_productfileInfo = new Analyze_ProductfileInfo();
        list = analyze_productfileInfo.getProduct_filing(json);
    }

    @Override
    public ProductfileListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productfilelist, parent, false);
        return new ProductfileListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        holder.line.setText("" + (position + 1));
        ImageLoader.getInstance().displayImage(list.get(position).getImg(), holder.img);
        holder.name.setText(list.get(position).getName());
        holder.pcode.setText(list.get(position).getPcode());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.title.setText(list.get(position).getTitle());
        holder.brand_title.setText(list.get(position).getBrand_title());
        holder.cost.setText(list.get(position).getCost() + "");
        holder.fprice.setText(list.get(position).getFprice() + "");
        holder.price.setText(list.get(position).getPrice() + "");
        holder.stotal.setText(list.get(position).getStotal() + "");
        holder.u_time.setText(list.get(position).getU_time());
        holder.expired_date.setText(list.get(position).getExpired_date());

        if (list.get(position).isExist()) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.button1.setText("修改");
            holder.button1.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.brand_orange));
            holder.button2.setText("下架");
            holder.button2.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.count_gray1));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.home_gray2));
            holder.button1.setText("檢視");
            holder.button1.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.productfile_blue));
            holder.button2.setText("上架");
            holder.button2.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.count_green1));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        ImageView img;
        TextView line, name, pcode, color, size, title, brand_title, cost, fprice, price, stotal, u_time, expired_date;
        Button button1, button2;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("background");
            line = view.findViewWithTag("line");
            img = view.findViewWithTag("img");
            name = view.findViewWithTag("name");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            title = view.findViewWithTag("title");
            brand_title = view.findViewWithTag("brand_title");
            cost = view.findViewWithTag("cost");
            fprice = view.findViewWithTag("fprice");
            price = view.findViewWithTag("price");
            stotal = view.findViewWithTag("stotal");
            u_time = view.findViewWithTag("u_time");
            expired_date = view.findViewWithTag("expired_date");
            button1 = view.findViewWithTag("button1");
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getAdapterPosition()).isExist()) {

                    } else {

                    }
                }
            });

            button2 = view.findViewWithTag("button2");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUporDown(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    private void setUporDown(final int position) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                if (list.get(position).isExist()) {
                    return productfileApi.up_down_product(list.get(position).getP_no(), list.get(position).getPi_no(), userInfo.getDu_no(), 1);
                } else {
                    return productfileApi.up_down_product(list.get(position).getP_no(), list.get(position).getPi_no(), userInfo.getDu_no(), 0);
                }
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    restItemData(position);
                }
            }
        });
    }

    private void restItemData(final int position) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                Log.e("gg", list.get(position).getName() + "\n" + list.get(position).getPcode() + "\n" + list.get(position).getTitle());
                return productfileApi.product_filing("sl87fy2O9oevXUDAxG9l9Q==,XgH1q1g@_Cl0ay0/M9ST@_vQ==,mSdI5KxZJPcbsnzuUJUQnA==,bWg9NFf4nSTZAqt1AVrZ9A==,a42LeDpQoCqr43FmcC@_Dpw==,ZbsLf8P0GQmGXu3w/NVHCg==,bUhx03Gk6LAH8dZ8NkiNog==",
                        list.get(position).getName(), list.get(position).getPcode(), list.get(position).getTitle(), 0);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {

                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    ArrayList<ProductfilePojo> arrayList = analyze_productfileInfo.getProduct_filing(jsonObject);
                    if (arrayList.size() == 1) {
                        list.set(position, arrayList.get(0));
                        notifyItemChanged(position);
                    }
                }
            }
        });
    }

    public void setFilter(JSONObject json) {
        list = analyze_productfileInfo.getProduct_filing(json);
        notifyDataSetChanged();
    }

    public boolean setFilterMore(JSONObject json) {
        int presize = list.size();
        if (presize > 0) {
            if (json != null && analyze_productfileInfo.getProduct_filing(json).size() > 0) {
                list.addAll(analyze_productfileInfo.getProduct_filing(json));
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