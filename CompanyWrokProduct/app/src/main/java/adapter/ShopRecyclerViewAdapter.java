package adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.test.tw.wrokproduct.GlobalVariable;
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
    private int layout_width, layout_heigh;
    private JSONObject json1, json2;
    private View view;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;
    private Bitmap[] images;
    private boolean[] isfavorate;
    private ShopRecyclerViewAdapter.RecycleHolder recycleHolder;
    private LinearLayout.LayoutParams layoutParams;
    private ImageLoader loader;
    GlobalVariable gv;

    public void setmHeaderView(View mHeaderView) {

        this.mHeaderView = mHeaderView;
    }

    public void setmFooterViewView(View mFooterView) {

        this.mFooterView = mFooterView;
    }

    public ShopRecyclerViewAdapter(Context ctx, JSONObject json1, JSONObject json2, int layout_width, int layout_heigh) {
        this.json1 = json1;
        this.json2 = json2;
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        loader = ImageLoader.getInstance();
        list = ResolveJsonData.getJSONData(json2);
        Log.e("IPLIST", "" + list);
        isfavorate = new boolean[list.size()];
        images = new Bitmap[list.size()];
        dm = ctx.getResources().getDisplayMetrics();
        gv = (GlobalVariable) ctx.getApplicationContext();
    }

    @Override
    public ShopRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mHeaderView, json2);
        }

        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mFooterView, json2);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
        recycleHolder = new ShopRecyclerViewAdapter.RecycleHolder(ctx, view, json2);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
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
            holder.free.setVisibility(View.INVISIBLE);
            holder.freash.setVisibility(View.INVISIBLE);
            holder.hot.setVisibility(View.INVISIBLE);
            holder.limit.setVisibility(View.INVISIBLE);
            holder.count0.setVisibility(View.INVISIBLE);
            holder.count1.setVisibility(View.INVISIBLE);
            holder.discount.setVisibility(View.INVISIBLE);
            //免運
            if (list != null) {
                if (list.get(position - 1).get("shipping").equals("true"))
                    holder.free.setVisibility(View.VISIBLE);
                //新品
                if (list.get(position - 1).get("isnew").equals("true"))
                    holder.freash.setVisibility(View.VISIBLE);
                //熱門
                if (list.get(position - 1).get("ishot").equals("true"))
                    holder.hot.setVisibility(View.VISIBLE);
                //限時
                if (list.get(position - 1).get("istime").equals("true"))
                    holder.limit.setVisibility(View.VISIBLE);
                //count0+count1
                if (!list.get(position - 1).get("discount").equals("")) {
                    holder.count0.setVisibility(View.VISIBLE);
                    holder.count1.setVisibility(View.VISIBLE);
                    holder.discount.setVisibility(View.VISIBLE);
                    // holder.discount.setTextSize(dm.heightPixels/dm.widthPixels*18);
                    holder.discount.setText(list.get(position - 1).get("discount"));
                }
                //原價
                //特價
                if (list.get(position - 1).get("rprice").equals(list.get(position - 1).get("rsprice"))) {
                    holder.rprice.setVisibility(View.INVISIBLE);
                    holder.rsprice.setText("$" + getString(list.get(position - 1).get("rsprice")));
                } else {
                    holder.rprice.setPaintFlags(holder.rprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.rprice.setText("$" + getString(list.get(position - 1).get("rprice")));
                    holder.rsprice.setText("$" + getString(list.get(position - 1).get("rsprice")));
                }
                //title
                holder.tv1.setText(list.get(position - 1).get("title"));

                //score
                switch (list.get(position - 1).get("score")) {
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
                if (isfavorate[position - 1])
                    holder.heart.setImageResource(R.drawable.heart_on);
                else
                    holder.heart.setImageResource(R.drawable.heart_off);
                holder.linear_heart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isfavorate[position - 1]) {
                            holder.heart.setImageResource(R.drawable.heart_off);
                            isfavorate[position - 1] = false;
                        } else {
                            holder.heart.setImageResource(R.drawable.heart_on);
                            isfavorate[position - 1] = true;
                        }

                    }
                });
            } else if (getItemViewType(position) == TYPE_HEADER) {
            }
        }
    }

    private DisplayImageOptions getWholeOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
                //.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                // .showImageOnFail(R.drawable.error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成

        return options;
    }

    private static String getString(String str) {
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
                Toast.makeText(ctx, "" + list.get(position - 1).get("title"), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void setFilter(JSONObject json) {
        this.json2 = json;
        list = ResolveJsonData.getJSONData(json);
        isfavorate = new boolean[list.size()];
        notifyDataSetChanged();
    }

}
