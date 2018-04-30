package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import library.ResolveJsonData;

public class ShipsWaysRecyclerViewAdapter extends RecyclerView.Adapter<ShipsWaysRecyclerViewAdapter.RecycleHolder> {


    private Context ctx;
    private JSONObject json;
    private View view;
    private ArrayList<Map<String, String>> list;
    private ShipsWaysRecyclerViewAdapter.RecycleHolder recycleHolder;
    private ShipsWaysRecyclerViewAdapter.ClickListener clickListener;
    private int[] imgs = {0, R.drawable.ship_seven_elaven, R.drawable.ship_all_family, R.drawable.ship_hi_life, R.drawable.ship_black_cat, R.drawable.ship_post_office, R.drawable.ship_home_delivery, R.drawable.ship_oversea};
    private String[] texts = {"無", "7-11", "全家", "萊爾富", "黑貓宅配", "郵局", "賣家宅配", "海外配送"};
    private String[] lands = {"無", "本島", "離島", "海外"};

    public ShipsWaysRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        if (json != null)
            list = ResolveJsonData.getShippingArray(json);
        else
            list = new ArrayList<>();
    }


    @Override
    public ShipsWaysRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shipways, parent, false);
        recycleHolder = new ShipsWaysRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

        holder.imageView.setImageResource(imgs[Integer.parseInt(list.get(position).get("logistics"))]);
        holder.name.setText(texts[Integer.parseInt(list.get(position).get("logistics"))]);
        holder.location.setText(lands[Integer.parseInt(list.get(position).get("land"))]);
        holder.price.setText(list.get(position).get("lpay"));
        holder.forfree.setText(list.get(position).get("info"));

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

    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name, location, price, forfree;
        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            imageView = view.findViewById(R.id.shipways_img_logistics);
            name = view.findViewById(R.id.shipways_txt_logistics);
            location = view.findViewById(R.id.shipways_txt_land);
            price = view.findViewById(R.id.shipways_txt_lpay);
            forfree = view.findViewById(R.id.shipways_txt_info);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

        }
    }

    public void setClickListener(ShipsWaysRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list);
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        list = ResolveJsonData.getJSONData(json);
        notifyDataSetChanged();
    }

}
