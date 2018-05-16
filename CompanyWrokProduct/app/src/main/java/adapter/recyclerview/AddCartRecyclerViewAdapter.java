package adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import library.AnalyzeJSON.ResolveJsonData;

public class AddCartRecyclerViewAdapter extends RecyclerView.Adapter<AddCartRecyclerViewAdapter.RecycleHolder> {


    private Context ctx;
    private JSONObject json;
    private View view;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;
    private FrameLayout.LayoutParams layoutParams;
    private AddCartRecyclerViewAdapter.RecycleHolder recycleHolder;
    private AddCartRecyclerViewAdapter.ItemSelectListener clickListener;
    private boolean[] ischoice;
    GradientDrawable drawable;
    int color_values;

    public AddCartRecyclerViewAdapter(Context ctx, JSONObject json, int heigh, int color_values) {
        this.ctx = ctx;
        this.color_values = color_values;
        dm = ctx.getResources().getDisplayMetrics();
        int lenth = (int) (dm.widthPixels / 3 - 20 * dm.density);
        layoutParams = new FrameLayout.LayoutParams(lenth, (int) (heigh - 50 * dm.density) / 4);
        layoutParams.setMargins((int) (10 * dm.density), 0, (int) (10 * dm.density), (int) (10 * dm.density));
        if (json != null) {
            list = ResolveJsonData.getPcContentItemArray(json);
            ischoice = new boolean[list.size()];
        } else
            list = new ArrayList<>();
    }


    @Override
    public AddCartRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shopcart, parent, false);
        recycleHolder = new AddCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        drawable = (GradientDrawable) holder.linearLayout.getBackground();
        if (!ischoice[position])
            drawable.setStroke((int) (1 * dm.density), Color.BLACK);
        else
            drawable.setStroke((int) (3 * dm.density), color_values);
        holder.linearLayout.setLayoutParams(layoutParams);
        holder.color.setText(list.get(position).get("color"));
        holder.size.setText(list.get(position).get("size"));

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


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView color, size;
        Context ctx;
        JSONObject json;
        LinearLayout linearLayout;


        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            color = view.findViewById(R.id.shopcart_txt_color);
            size = view.findViewById(R.id.shopcart_txt_size);
            linearLayout = view.findViewById(R.id.shopcart_linearlayout);
            drawable = (GradientDrawable) linearLayout.getBackground();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (!ischoice[position]) {
                drawable.setStroke((int) (3 * dm.density), color_values);
                color.setTextColor(color_values);
                size.setTextColor(color_values);
                if (clickListener != null)
                    clickListener.ItemSelected(view, position, list);

            } else {
                drawable.setStroke((int) (1 * dm.density), Color.BLACK);
                color.setTextColor(Color.BLACK);
                size.setTextColor(Color.BLACK);
                if (clickListener != null)
                    clickListener.ItemCancelSelect(view, position, list);
            }
            ischoice[position] = !ischoice[position];

        }
    }

    public void setItemSelectListener(AddCartRecyclerViewAdapter.ItemSelectListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemSelectListener {
        void ItemSelected(View view, int postion, ArrayList<Map<String, String>> list);

        void ItemCancelSelect(View view, int postion, ArrayList<Map<String, String>> list);
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        list = ResolveJsonData.getJSONData(json);
        notifyDataSetChanged();
    }

}
