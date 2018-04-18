package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import library.ResolveJsonData;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private View mHeaderView;
    private View mFooterView;
    private Context ctx;
    int type;
    int layout_width, layout_heigh;
    JSONObject json1,json2;
    View view;
    ArrayList<Map<String, String>> list;
    DisplayMetrics dm;
    Bitmap[] images;
    ShopRecyclerViewAdapter.RecycleHolder recycleHolder;
    LinearLayout.LayoutParams layoutParams;

    public void setmHeaderView(View mHeaderView) {

        this.mHeaderView = mHeaderView;
    }

    public ShopRecyclerViewAdapter(Context ctx, JSONObject json1,JSONObject json2, int layout_width, int layout_heigh, int type) {
        this.json1 = json1;
        this.json2 = json2;
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.type = type;
        list = ResolveJsonData.getJSONData1(json2);
        images = new Bitmap[list.size()];
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public ShopRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mHeaderView, json2);
        }
/*
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new RecycleHolder(mFooterView,ctx,arrayList);
        }
*/
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
        recycleHolder = new ShopRecyclerViewAdapter.RecycleHolder(ctx, view, json2);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            layoutParams = new LinearLayout.LayoutParams(layout_width, layout_heigh);
            if (type == 0) {
                layoutParams.setMargins(0, 0, (int) (10 * dm.density), 0);
            } else if (type == 1) {
                layoutParams.setMargins(0, 0, 0, 0);
                holder.tv1.setTextColor(ctx.getResources().getColor(R.color.colorBlack));
                holder.tv1.setBackgroundColor(ctx.getResources().getColor(R.color.colorInvisible));
            } else if (type == 2) {
                layoutParams.setMargins(0, 0, (int) (5 * dm.density), 0);
                holder.tv1.setVisibility(View.INVISIBLE);
            }
            holder. rprice.setPaintFlags(holder. rprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder. rsprice.setText( "$"+getString(  holder.rsprice.getText().toString()));
            holder.frameLayout.setLayoutParams(layoutParams);
            holder.item_linear.setLayoutParams(new FrameLayout.LayoutParams((int)(layout_width-15* dm.density), ViewGroup.LayoutParams.FILL_PARENT));
            holder.imageView.setImageBitmap(null);
            resizeImageView(holder.imageView,(int)(layout_width-15* dm.density),(int)(layout_width-15 * dm.density));
            resizeImageView(holder.count0,(int)(layout_width/3*1.3),(int)(layout_width/3*1.3));
            resizeImageView(holder.count1,(int)(layout_width/3*1.3-5 * dm.density),(int)(layout_width/3*1.3-5 * dm.density));
            resizeImageView(holder.free,(layout_width/3),(layout_width/3));
            resizeImageView(holder.freash,(layout_width/3),(int)(layout_width/3*0.3));
            resizeImageView(holder.hot,(layout_width/3),(int)(layout_width/3*0.3));
            resizeImageView(holder.limit,(layout_width/3),(int)(layout_width/3*0.3));

            if (images[position-1] != null) {
                holder.imageView.setImageBitmap(images[position-1]);
            } else {

                ImageLoader.getInstance().loadImage(list.get(position-1).get("image"), new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        images[position-1] = loadedImage;
                        holder.imageView.setImageBitmap(loadedImage);

                    }

                });

            }

            holder.tv1.setText(list.get(position-1).get("title"));
        }else if (getItemViewType(position) == TYPE_HEADER) {
        }
    }
    private static String getString(String str){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }
    private void resizeImageView(ImageView imageView,int width,int heigh){//重構圖片大小
        ViewGroup.LayoutParams params = imageView.getLayoutParams();  //需import android.view.ViewGroup.LayoutParams;
        params.width = width;
        params.height =heigh;
        imageView.setLayoutParams(params);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return list.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return list.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return list.size() + 1;
        } else {
            return list.size() + 2;
        }
    }
    //將header獨立出來
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView,count0,count1,free,freash,hot,limit;
        LinearLayout frameLayout;
        TextView tv1,rprice,rsprice;
        Context ctx;
        JSONObject json;
        ViewPager viewPager;
        LinearLayout item_linear;
        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            ButterKnife.bind(this, view);
            this.json = json;
            this.ctx = ctx;
            if (view == mHeaderView) {
                viewPager =view.findViewById(R.id.adView);
                viewPager.setAdapter(new MyPagerAdapter(view,json1));
            }
            if (view == mFooterView) {

            }
            frameLayout = view.findViewById(R.id.shop_frame);
            item_linear=view.findViewById(R.id.item_linear);
            imageView = view.findViewById(R.id.product_image);
            tv1 = view.findViewById(R.id.product_title);
            count0=view.findViewById(R.id.count0);
            count1=view.findViewById(R.id.count1);
            freash=view.findViewById(R.id.freash);
            hot=view.findViewById(R.id.hot);
            limit=view.findViewById(R.id.limit);
            free=view.findViewById(R.id.free);
            rprice=view.findViewById(R.id.rprice);
            rsprice=view.findViewById(R.id.rsprice);
//            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(ctx, "" + list.get(position).get("title"), Toast.LENGTH_SHORT).show();
        }

    }

    public void setFilter(JSONObject json) {
        this.json2 = json;
        list = ResolveJsonData.getJSONData(json);
        images = new Bitmap[list.size()];
        notifyDataSetChanged();

    }

}
