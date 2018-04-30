package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import library.ResolveJsonData;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_NORMAL = 0;  //说明是不带有header和footer的
    public static final int TYPE_HEADER = 1;  //说明是带有Header的
    public static final int TYPE_FOOTER = 2;  //说明是带有Footer的

    private View mHeaderView;
    private View mFooterView;
    private Context ctx;
    private int layout_width, layout_heigh, had_header = 1;
    private JSONObject json;
    private View view;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;
    private boolean[] isfavorate;
    private ShopRecyclerViewAdapter.RecycleHolder recycleHolder;
    private LinearLayout.LayoutParams layoutParams;
    GlobalVariable gv;
    private ShopRecyclerViewAdapter.ClickListener clickListener;

    public void setmHeaderView(View mHeaderView) {

        this.mHeaderView = mHeaderView;
    }

    public void setmFooterViewView(View mFooterView) {

        this.mFooterView = mFooterView;
    }

    public ShopRecyclerViewAdapter(Context ctx, JSONObject json, int layout_width, int layout_heigh, int had_header) {
        this.ctx = ctx;
        this.had_header = had_header;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        if (json != null)
            list = ResolveJsonData.getJSONData(json);
        else
            list = new ArrayList<>();
        isfavorate = new boolean[list.size()];
        dm = ctx.getResources().getDisplayMetrics();
        gv = (GlobalVariable) ctx.getApplicationContext();
    }

    public ShopRecyclerViewAdapter(Context ctx, JSONObject json, int layout_width, int layout_heigh) {
        this.json = json;
        this.ctx = ctx;
        this.json = json;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        list = ResolveJsonData.getJSONData(json);
        isfavorate = new boolean[list.size()];
        dm = ctx.getResources().getDisplayMetrics();
        gv = (GlobalVariable) ctx.getApplicationContext();
    }

    @Override
    public ShopRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mHeaderView, json);
        }

        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mFooterView, json);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
        recycleHolder = new ShopRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public  void onBindViewHolder(final RecycleHolder holder, final int position) {
        if (list.size() > 0) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                layoutParams = new LinearLayout.LayoutParams(layout_width, layout_heigh);
                //layout
                holder.frameLayout.setLayoutParams(layoutParams);
                holder.item_linear.setLayoutParams(new FrameLayout.LayoutParams((int) (layout_width - 15 * dm.density), (int) (layout_width - 15 * dm.density + 110 * dm.density)));
                //圖片
                resizeImageView(holder.imageView, (int) (layout_width - 15 * dm.density), (int) (layout_width - 15 * dm.density));
                resizeImageView(holder.count0, (int) (layout_width / 3 * 1.3), (int) (layout_width / 3 * 1.3));
                resizeImageView(holder.count1, (int) (layout_width / 3 * 1.3 - 5 * dm.density), (int) (layout_width / 3 * 1.3 - 5 * dm.density));
                resizeImageView(holder.free, (layout_width / 3), (layout_width / 3));
                resizeImageView(holder.freash, (layout_width / 3), (int) (layout_width / 3 * 0.3));
                resizeImageView(holder.hot, (layout_width / 3), (int) (layout_width / 3 * 0.3));
                resizeImageView(holder.limit, (layout_width / 3), (int) (layout_width / 3 * 0.3));
                holder.imageView.setImageBitmap(null);
                ImageLoader.getInstance().displayImage(list.get(position - had_header).get("image"), holder.imageView);
            /*
            if (images[position - 1] != null) {
                holder.imageView.setImageBitmap(images[position - 1]);
            } else {
                ImageLoader.getInstance().loadImage(list.get(position - 1).get("image"), getWholeOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        images[position - 1] = loadedImage;
                        holder.imageView.setImageBitmap(images[position - 1]);
                    }
                });
            }
            */
                holder.free.setVisibility(View.INVISIBLE);
                holder.freash.setVisibility(View.INVISIBLE);
                holder.hot.setVisibility(View.INVISIBLE);
                holder.limit.setVisibility(View.INVISIBLE);
                holder.count0.setVisibility(View.INVISIBLE);
                holder.count1.setVisibility(View.INVISIBLE);
                holder.discount.setVisibility(View.INVISIBLE);
Log.e("GGGGGGGG",list+"");
                //免運
                if (list.get(position - had_header).get("shipping").equals("true"))
                    holder.free.setVisibility(View.VISIBLE);
                //新品
                if (list.get(position - had_header).get("isnew").equals("true"))
                    holder.freash.setVisibility(View.VISIBLE);
                //熱門
                if (list.get(position - had_header).get("ishot").equals("true"))
                    holder.hot.setVisibility(View.VISIBLE);
                //限時
                if (list.get(position - had_header).get("istime").equals("true"))
                    holder.limit.setVisibility(View.VISIBLE);
                //count0+count1
                if (!list.get(position - had_header).get("discount").equals("")) {
                    holder.count0.setVisibility(View.VISIBLE);
                    holder.count1.setVisibility(View.VISIBLE);
                    holder.discount.setVisibility(View.VISIBLE);
                    // holder.discount.setTextSize(dm.heightPixels/dm.widthPixels*18);
                    holder.discount.setText(list.get(position - had_header).get("discount"));
                }
                //原價
                //特價
                if (list.get(position - had_header).get("rprice").equals(list.get(position - had_header).get("rsprice"))) {
                    holder.rprice.setVisibility(View.INVISIBLE);
                    holder.rsprice.setText("$" + getDeciamlString(list.get(position - had_header).get("rsprice")));
                } else {
                    holder.rprice.setPaintFlags(holder.rprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.rprice.setText("$" + getDeciamlString(list.get(position - had_header).get("rprice")));
                    holder.rsprice.setText("$" + getDeciamlString(list.get(position - had_header).get("rsprice")));
                }
                //title
                holder.tv1.setText(list.get(position - had_header).get("title"));

                //score
                switch (list.get(position - had_header).get("score")) {
                    case "1":
                        holder.score.setImageResource(R.drawable.star1);
                        break;
                    case "2":
                        holder.score.setImageResource(R.drawable.star2);
                        break;
                    case "3":
                        holder.score.setImageResource(R.drawable.star3);
                        break;
                    case "4":
                        holder.score.setImageResource(R.drawable.star4);
                        break;
                    case "5":
                        holder.score.setImageResource(R.drawable.star5);
                        break;
                    default:
                        holder.score.setImageResource(R.drawable.star0);
                        break;
                }
                //判斷是否點過最愛
                if (isfavorate[position - had_header])
                    holder.heart.setImageResource(R.drawable.heart_on);
                else
                    holder.heart.setImageResource(R.drawable.heart_off);
                holder.linear_heart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isfavorate[position - had_header]) {
                            holder.heart.setImageResource(R.drawable.heart_off);
                            isfavorate[position - had_header] = false;
                        } else {
                            holder.heart.setImageResource(R.drawable.heart_on);
                            isfavorate[position - had_header] = true;
                        }

                    }
                });
            } else if (getItemViewType(position) == TYPE_HEADER) {
            }
        }

    }


    private  String getDeciamlString(String str) {
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
        if (manager instanceof GridLayoutManager) {
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
        ImageView imageView, count0, count1, free, freash, hot, limit, heart, score;
        LinearLayout frameLayout, linear_heart;
        TextView tv1, rprice, rsprice, discount;
        Context ctx;
        JSONObject json;
        LinearLayout item_linear;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            ButterKnife.bind(this, view);
            this.json = json;
            this.ctx = ctx;
            if (view == mHeaderView) {
                /*
                viewPager = view.findViewById(R.id.adView);
                relativeLayout = view.findViewById(R.id.RelateView);
                relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 19 / 54));
                viewPager.setAdapter(new MyPagerAdapter(view, json1));
                */
            }
            if (view == mFooterView) {
            }
            frameLayout = view.findViewById(R.id.shop_frame);
            item_linear = view.findViewById(R.id.item_linear);
            imageView = view.findViewById(R.id.product_image);
            tv1 = view.findViewById(R.id.product_title);
            count0 = view.findViewById(R.id.count0);
            count1 = view.findViewById(R.id.count1);
            freash = view.findViewById(R.id.freash);
            hot = view.findViewById(R.id.hot);
            limit = view.findViewById(R.id.limit);
            free = view.findViewById(R.id.free);
            rprice = view.findViewById(R.id.rprice);
            rsprice = view.findViewById(R.id.rsprice);
            discount = view.findViewById(R.id.discount);
            linear_heart = view.findViewById(R.id.linear_heart);
            heart = view.findViewById(R.id.heart);
            score = view.findViewById(R.id.score);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != list.size() + 1) {
                Toast.makeText(ctx, "" + list.get(position - had_header).get("title"), Toast.LENGTH_SHORT).show();
            }
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, list);
            }
        }
    }

    public void setClickListener(ShopRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list);
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        list = ResolveJsonData.getJSONData(json);
        isfavorate = new boolean[list.size()];
        notifyDataSetChanged();
    }

    public void recycleBitmaps() {
        mHeaderView = null;
        mFooterView = null;
        ctx = null;
        json = null;
        view = null;
        list = null;
        dm = null;
        isfavorate = null;
        recycleHolder = null;
        layoutParams = null;
        gv = null;
    }
}
