package adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;
    private AddCartRecyclerViewAdapter.RecycleHolder recycleHolder;
    private AddCartRecyclerViewAdapter.ItemSelectListener clickListener;
    private boolean[] ischoice;
    GradientDrawable drawable;
    int color_values;

    public AddCartRecyclerViewAdapter(Context ctx, JSONObject json, int heigh, int color_values) {
        this.ctx = ctx;
        this.color_values = color_values;
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
            list = ResolveJsonData.getPcContentItemArray(json);
            ischoice = new boolean[list.size()];
        } else
            list = new ArrayList<>();
    }


    @Override
    public AddCartRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shopcart, parent, false);
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,(int)(40*dm.density));
        layoutParams.setMargins((int)(10*dm.density),(int)(10*dm.density),(int)(10*dm.density),(int)(10*dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        textView.setBackground(ctx.getResources().getDrawable(R.drawable.shopcart_item_bg));
        recycleHolder = new AddCartRecyclerViewAdapter.RecycleHolder(ctx, textView, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        drawable = (GradientDrawable) holder.text.getBackground();
        // layoutParams.width = (int) ((list.get(position).get("color").trim().length() + list.get(position).get("size").trim().length()) * 15 * dm.density);
        if (!ischoice[position]) {
            drawable.setStroke((int) (1 * dm.density), Color.BLACK);
            holder.text.setTextColor(Color.BLACK);
        } else {
            drawable.setStroke((int) (3 * dm.density), color_values);
            holder.text.setTextColor(color_values);
        }
        //  holder.linearLayout.setLayoutParams(layoutParams);
        holder.text.setText("    "+list.get(position).get("color") + "  " + list.get(position).get("size")+"    ");
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView color, size;
        TextView text;
        Context ctx;
        JSONObject json;
        LinearLayout linearLayout;


        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            //color = view.findViewById(R.id.shopcart_txt_color);
            // size = view.findViewById(R.id.shopcart_txt_size);
            text = (TextView) view;
            drawable = (GradientDrawable) text.getBackground();
            //linearLayout = view.findViewById(R.id.shopcart_linearlayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (!ischoice[position]) {
                for (int i = 0; i < ischoice.length; i++) {
                    ischoice[i] = false;
                }
                ischoice[position] = true;
                if (clickListener != null)
                    clickListener.ItemSelected(view, position, list);
            } else {
                ischoice[position] = false;
                if (clickListener != null)
                    clickListener.ItemCancelSelect(view, position, list);
            }

            notifyDataSetChanged();

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
