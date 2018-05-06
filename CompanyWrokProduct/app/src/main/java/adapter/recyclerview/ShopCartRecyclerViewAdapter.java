package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.PcContentActivity;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import library.ResolveJsonData;
import pojo.ProductInfoPojo;

public class ShopCartRecyclerViewAdapter extends RecyclerView.Adapter<ShopCartRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    private Context ctx;
    private JSONObject json;
    private View view;
    private ArrayList<ArrayList<Map<String, String>>> alllist;
    private ShopCartRecyclerViewAdapter.RecycleHolder recycleHolder;
    private ShopCartRecyclerViewAdapter.ClickListener clickListener;

    public ShopCartRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;

    }

    @Override
    public ShopCartRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
            return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
        recycleHolder = new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {

        } else if (getItemViewType(position) == TYPE_HEADER) {

        }

    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        if (position == count)
            return TYPE_HEADER;

        for (int i = 0; i < alllist.size(); i++) {
            count += (i + alllist.get(i).size());
            if (position == count)
                return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < alllist.size(); i++) {
            count += alllist.get(i).size();
        }
        return count;

    }

    private String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

    private void resizeImageView(View view, int width, int heigh) {//重構圖片大小
        ViewGroup.LayoutParams params = view.getLayoutParams();  //需import android.view.ViewGroup.LayoutParams;
        params.width = width;
        params.height = heigh;
        view.setLayoutParams(params);
    }

  class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            ButterKnife.bind(this, view);
            this.json = json;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (clickListener != null) {
                clickListener.ItemClicked(view, position, alllist);
            }
        }
    }

    public void setClickListener(ShopCartRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, ArrayList<ArrayList<Map<String, String>>> alllist);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        alllist = ResolveJsonData.getCartItemArray(json);
        notifyDataSetChanged();
    }

}
