package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.SearchResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeTextRecyclerViewAdapter extends RecyclerView.Adapter<ChangeTextRecyclerViewAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;

    public ChangeTextRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
            initList(json);
        } else {
            list = new ArrayList<>();
        }

    }

    private void initList(JSONObject json) {
        list = new ArrayList<>();
        Map<String, String> map;
        try {
            JSONArray jsonArray = json.getJSONArray("Data");
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                map = new HashMap<>();
                map.put("img", jsonObject.getString("img"));
                map.put("title", jsonObject.getString("title"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ChangeTextRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        //  layoutParams.setMargins((int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        textView.setPadding((int) (5 * dm.density), (int) (10 * dm.density), (int) (5 * dm.density), (int) (10 * dm.density));
        textView.setTextColor(Color.BLACK);
        textView.setTag("textview");
            LinearLayout linearLayout=new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);
        View view=new View(ctx);
        view.setBackgroundResource(R.color.gainsboro);
        layoutParams.height=1;
        view.setLayoutParams(layoutParams);
        linearLayout.addView(view);
/*
        Drawable drawable = ctx.getResources().getDrawable(R.drawable.product);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawable = new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(bitmap, (int) (50 * dm.density), (int) (50 * dm.density), true));
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        */
        return new ChangeTextRecyclerViewAdapter.RecycleHolder(ctx, linearLayout);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText(list.get(position).get("title"));

        ImageLoader.getInstance().loadImage(list.get(position).get("img"), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(loadedImage, (int) (50 * dm.density), (int) (50 * dm.density), true));
                holder.text.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView color, size;
        TextView text;
        Context ctx;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            text =  view.findViewWithTag("textview");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
                Intent intent = new Intent(ctx, SearchResultActivity.class);
                intent.putExtra("keyword", list.get(position).get("title"));
                ctx.startActivity(intent);
        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            initList(json);
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
