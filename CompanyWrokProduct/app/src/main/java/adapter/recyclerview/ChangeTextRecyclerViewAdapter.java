package adapter.recyclerview;

import android.content.Context;
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
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeTextRecyclerViewAdapter extends RecyclerView.Adapter<ChangeTextRecyclerViewAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<String> list;
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

        try {
            JSONArray jsonArray = json.getJSONArray("Data");
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject.getString("title"));
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
        Drawable drawable = ctx.getResources().getDrawable(R.drawable.product);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawable = new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(bitmap, (int) (50 * dm.density), (int) (50 * dm.density), true));
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        return new ChangeTextRecyclerViewAdapter.RecycleHolder(ctx, textView);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText(list.get(position));
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

            text = (TextView) view;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(ctx, list.get(position), Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();

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
