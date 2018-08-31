package com.example.alex.posdemo.adapter.recylclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
    ImageView imageView;

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
                holder.exist.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.brand_gray));
                holder.exist.setText("下架");
            } else {
                holder.cover.setVisibility(View.VISIBLE);
                holder.exist.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.count_green1));
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
        AlertDialog dialog;

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
                        public void onTaskBefore() {

                        }

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == 0) {

                ctx.startActivity(new Intent(ctx, NewBrandActivity.class));
            /*
                View inflate = LayoutInflater.from(ctx).inflate(R.layout.dialog_brand, null);
                dialog = new CustomerDialog().init(ctx, inflate);
                final EditText code = inflate.findViewWithTag("code");
                final EditText name = inflate.findViewWithTag("name");
                imageView = inflate.findViewWithTag("image");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                        intent.setType("image/*");
                        ((FragmentActivity) ctx).startActivityForResult(intent, 200);
                    }
                });
                Button cancel = inflate.findViewWithTag("cancel");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button confirm = inflate.findViewWithTag("confirm");
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!code.getText().toString().equals("") && !name.getText().toString().equals("")) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public void onTaskBefore() {

                                }

                                @Override
                                public JSONObject onTasking(Void... params) {

                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                                    Bitmap bitmap = bitmapDrawable.getBitmap();
                                    return new BrandApi().insert_brand(userInfo.getDu_no(), code.getText().toString(), name.getText().toString(), bitmap);

                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        resetAdapter();
                                    }
                                    dialog.dismiss();
                                    new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                                }
                            });
                        }
                    }
                });
                dialog.show();
                */
            }

        }

    }

    public void resetAdapter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

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

    public void reloadImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}