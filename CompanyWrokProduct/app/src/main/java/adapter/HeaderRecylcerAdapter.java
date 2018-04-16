package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import java.util.ArrayList;
import java.util.Map;

public class HeaderRecylcerAdapter extends RecyclerView.Adapter<HeaderRecylcerAdapter.RecycleHolder> {
    private Context ctx;
    View view;
    HeaderRecylcerAdapter.RecycleHolder recycleHolder;
    ArrayList<Map<String, String>> list;

    public HeaderRecylcerAdapter(Context ctx) {
        list = new ArrayList<>();
        this.ctx = ctx;

    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shopheader, parent, false);
        recycleHolder = new HeaderRecylcerAdapter.RecycleHolder(ctx, view);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        holder.tv.setText(ctx.getResources().getStringArray(R.array.shop_header_title)[position]);
    }

    @Override
    public int getItemCount() {
        return ctx.getResources().getStringArray(R.array.shop_header_title).length;
    }

    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        FrameLayout frameLayout;
        TextView tv;
        Context ctx;
        ViewPager viewPager;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            viewPager = ((Activity) ctx).findViewById(R.id.shop_viewpager);
            frameLayout = view.findViewById(R.id.shop_header_layout);
            imageView = view.findViewById(R.id.shop_header_image);
            tv = view.findViewById(R.id.shop_header_text);
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(ctx, "" + position, Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(position);
        }

    }

    public void setFilter() {

    }
}
