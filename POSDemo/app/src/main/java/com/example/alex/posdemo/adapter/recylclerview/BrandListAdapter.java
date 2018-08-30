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

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.AlbumListPojo;
import library.AnalyzeJSON.Analyze_AlbumInfo;
import library.JsonApi.AlbumApi;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.ViewHolder> {

    private Context ctx;
    DisplayMetrics dm;
    ArrayList<AlbumListPojo> list;
    AlbumApi albumApi;
    Analyze_AlbumInfo analyze_albumInfo;
    UserInfo userInfo;

    public BrandListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        userInfo = (UserInfo) ctx.getApplicationContext();
        dm = ctx.getResources().getDisplayMetrics();
        albumApi = new AlbumApi();
        analyze_albumInfo = new Analyze_AlbumInfo();
        list = new ArrayList<>();
        list.add(new AlbumListPojo());
        list.addAll(analyze_albumInfo.getAlbum_Data(json));
    }


    @Override
    public BrandListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumlist, parent, false);
        return new BrandListAdapter.ViewHolder(view, viewType);

    }


    @Override
    public void onBindViewHolder(BrandListAdapter.ViewHolder holder, int position) {

        if (position > 0) {
            if (list.get(position).getImg().equals("")) {
                holder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_default));
            } else {
                ImageLoader.getInstance().displayImage(list.get(position).getImg(), holder.photo);
            }

            holder.title.setText(list.get(position).getName());
            holder.count.setText("[" + list.get(position).getCount() + "張相片]");
        } else {
            holder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_add));
            holder.title.setText("新增相簿");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        ImageView photo;
        ImageView edit, delete;
        TextView title, count;


        public ViewHolder(View view, final int position) {
            super(view);
            layout = view.findViewWithTag("layout");
            if (position == 0) {
                layout.setVisibility(View.GONE);
            }
            photo = view.findViewWithTag("photo");
            edit = view.findViewWithTag("edit");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            delete = view.findViewWithTag("delete");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            title = view.findViewWithTag("title");
            count = view.findViewWithTag("count");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();


        }

    }

    public void resetAdapter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return albumApi.album_data();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                setFilter(jsonObject);
            }
        });
    }

    public void setFilter(JSONObject json) {
        list = new ArrayList<>();
        list.add(new AlbumListPojo());
        list.addAll(analyze_albumInfo.getAlbum_Data(json));
        notifyDataSetChanged();
    }


}