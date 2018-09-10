package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.NewBrandActivity;
import com.example.alex.posdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.BrandListPojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_BrandInfo;
import library.JsonApi.BrandApi;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.ViewHolder> {

    private Context ctx;
    DisplayMetrics dm;
    ArrayList<BrandListPojo> list;
    BrandApi brandApi;
    Analyze_BrandInfo analyze_brandInfo;
    UserInfo userInfo;

    public BrandListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        userInfo = (UserInfo) ctx.getApplicationContext();
        dm = ctx.getResources().getDisplayMetrics();
        brandApi = new BrandApi();
        analyze_brandInfo = new Analyze_BrandInfo();
        list = new ArrayList<>();
        list.add(new BrandListPojo());
        list.addAll(analyze_brandInfo.getBrand_Data(json));

    }


    @Override
    public BrandListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brandlist, parent, false);
        return new BrandListAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(BrandListAdapter.ViewHolder holder, int position) {

        if (position > 0) {
            holder.add_image.setVisibility(View.GONE);
            holder.add_title.setVisibility(View.GONE);
            holder.exist.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.layout.setVisibility(View.VISIBLE);
            if (list.get(position).getImg().equals("")) {
                holder.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_default));
            } else {
                ImageLoader.getInstance().displayImage(list.get(position).getImg(), holder.image);
            }

            if (list.get(position).isExist()) {
                holder.cover.setVisibility(View.GONE);
                holder.exist.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.corner_gray));
                holder.exist.setText("下架");
            } else {
                holder.cover.setVisibility(View.VISIBLE);
                holder.exist.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.corner_green));
                holder.exist.setText("上架");
            }
            holder.title.setText(list.get(position).getTitle());
            holder.code.setText(list.get(position).getCode());
            holder.c_time.setText(list.get(position).getC_time());
            holder.u_time.setText(list.get(position).getU_time());

        } else {
            holder.cover.setVisibility(View.GONE);
            holder.layout.setVisibility(View.GONE);
            holder.exist.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.add_image.setVisibility(View.VISIBLE);
            holder.add_title.setVisibility(View.VISIBLE);

            holder.add_image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.brand_add));
            holder.add_title.setText("新增品牌");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        ImageView image;
        TextView title, code, c_time, u_time;
        Button exist, edit;
        ImageView add_image;
        TextView add_title;
        View cover;
        Intent intent;

        public ViewHolder(View view) {
            super(view);
            cover = view.findViewWithTag("cover");
            layout = view.findViewWithTag("layout");
            add_image = view.findViewWithTag("add_image");
            add_title = view.findViewWithTag("add_title");
            image = view.findViewWithTag("image");
            title = view.findViewWithTag("title");
            code = view.findViewWithTag("code");
            c_time = view.findViewWithTag("c_time");
            u_time = view.findViewWithTag("u_time");
            exist = view.findViewWithTag("exist");
            exist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            if (list.get(getAdapterPosition()).isExist()) {
                                return new BrandApi().up_down_brand(userInfo.getDu_no(), list.get(getAdapterPosition()).getPb_no(), "1");
                            } else {
                                return new BrandApi().up_down_brand(userInfo.getDu_no(), list.get(getAdapterPosition()).getPb_no(), "0");
                            }
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                resetAdapter();
                            }
                        }
                    });
                }
            });
            edit = view.findViewWithTag("edit");
            edit.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getTag().toString()) {
                case "edit":
                    intent = new Intent(ctx, NewBrandActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("image", list.get(position).getImg());
                    intent.putExtra("pb_no", list.get(position).getPb_no());
                    intent.putExtra("code", list.get(position).getCode());
                    intent.putExtra("title", list.get(position).getTitle());
                    ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("brand").startActivityForResult(intent, 100);
                    break;
                default:
                    if (position == 0) {
                        intent = new Intent(ctx, NewBrandActivity.class);
                        intent.putExtra("type", "insert");
                        ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("brand").startActivityForResult(intent, 100);
                    }
                    break;
            }


        }

    }

    public void resetAdapter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return brandApi.brand_data();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                setFilter(jsonObject);
            }
        });
    }

    public void setFilter(JSONObject json) {
        list = new ArrayList<>();
        list.add(new BrandListPojo());
        list.addAll(analyze_brandInfo.getBrand_Data(json));
        notifyDataSetChanged();
    }


}