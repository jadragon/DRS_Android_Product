package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Paint;
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
import com.test.tw.wrokproduct.LoginActivity;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.商品.PcContentActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import butterknife.ButterKnife;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.ResolveJsonData;
import library.Component.ToastMessageDialog;
import library.GetJsonData.ProductJsonData;
import library.ItemTouchListencer;
import pojo.ProductInfoPojo;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.RecycleHolder> implements ItemTouchListencer {
    public final int TYPE_NORMAL = 0;  //说明是不带有header和footer的
    public final int TYPE_HEADER = 1;  //说明是带有Header的
    public final int TYPE_FOOTER = 2;  //说明是带有Footer的
    //type
    public final static int HIDE_BANNER = 0;
    public final static int SHOW_BANNER = 1;
    public final static int FAVORATE = -1;
    public final static int BROWSE = -2;
    private View mHeaderView;
    private Context ctx;
    private int layout_width, layout_heigh, had_header = 0;
    private JSONObject json;
    private View view;
    private ArrayList<ProductInfoPojo> list;
    private DisplayMetrics dm;
    private GlobalVariable gv;
    private TypedArray stars;
    private ToastMessageDialog toastMessageDialog;

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }


    public ShopRecyclerViewAdapter(Context ctx, JSONObject json, int had_header) {
        this.ctx = ctx;
        toastMessageDialog = new ToastMessageDialog(ctx);
        dm = ctx.getResources().getDisplayMetrics();
        gv = ((GlobalVariable) ctx.getApplicationContext());
        stars = ctx.getResources().obtainTypedArray(R.array.small_stars);
        if (had_header > FAVORATE)
            this.had_header = had_header;
        this.layout_width = (int) ((dm.widthPixels - 10 * dm.density) / (float) 2);
        this.layout_heigh = (int) (this.layout_width + (110 * dm.density));
        if (json != null)
            list = ResolveJsonData.getJSONData(json);
        else
            list = new ArrayList<>();
    }


    @Override
    public ShopRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, mHeaderView, json, viewType);
        }
        if (viewType == TYPE_FOOTER) {
            View footer = new View(ctx);
            footer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
            return new ShopRecyclerViewAdapter.RecycleHolder(ctx, footer, json, viewType);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shop, parent, false);
        return new ShopRecyclerViewAdapter.RecycleHolder(ctx, view, json, viewType);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        if (list.size() > 0) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                holder.imageView.setImageBitmap(null);
                ImageLoader.getInstance().displayImage(list.get(position - had_header).getImage(), holder.imageView);
                holder.free.setVisibility(View.INVISIBLE);
                holder.freash.setVisibility(View.INVISIBLE);
                holder.hot.setVisibility(View.INVISIBLE);
                holder.limit.setVisibility(View.INVISIBLE);
                holder.count0.setVisibility(View.INVISIBLE);
                holder.count1.setVisibility(View.INVISIBLE);
                holder.discount.setVisibility(View.INVISIBLE);
                //免運
                if (list.get(position - had_header).getShipping().equals("true"))
                    holder.free.setVisibility(View.VISIBLE);
                //新品
                if (list.get(position - had_header).getIsnew().equals("true"))
                    holder.freash.setVisibility(View.VISIBLE);
                //熱門
                if (list.get(position - had_header).getIshot().equals("true"))
                    holder.hot.setVisibility(View.VISIBLE);
                //限時
                if (list.get(position - had_header).getIstime().equals("true"))
                    holder.limit.setVisibility(View.VISIBLE);
                //count0+count1
                if (!list.get(position - had_header).getDiscount().equals("")) {
                    holder.count0.setVisibility(View.VISIBLE);
                    holder.count1.setVisibility(View.VISIBLE);
                    holder.discount.setVisibility(View.VISIBLE);
                    holder.discount.setText(list.get(position - had_header).getDiscount());
                }
                //原價
                holder.rprice.setVisibility(View.VISIBLE);
                holder.rsprice.setVisibility(View.VISIBLE);
                //特價
                if (list.get(position - had_header).getRprice().equals(list.get(position - had_header).getRsprice())) {
                    holder.rprice.setVisibility(View.INVISIBLE);
                    holder.rsprice.setText("$" + list.get(position - had_header).getRsprice());
                } else {
                    holder.rprice.setPaintFlags(holder.rprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.rprice.setText("$" + list.get(position - had_header).getRprice());
                    holder.rsprice.setText("$" + list.get(position - had_header).getRsprice());
                }
                //title
                holder.tv1.setText(list.get(position - had_header).getTitle());

                //score

                holder.score.setImageResource(stars.getResourceId(list.get(position - had_header).getScore(), 0));

                //判斷是否點過最愛
                if (list.get(position - had_header).getFavorite())
                    holder.heart.setImageResource(R.drawable.heart_on);
                else
                    holder.heart.setImageResource(R.drawable.heart_off);
            }
        }

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
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
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

    @Override
    public void itemMove(int startIndex, int endIndex) {
        if (startIndex == 0 || endIndex == 0) {
            return;
        }
        Collections.swap(list, startIndex, endIndex);
        notifyItemMoved(startIndex, endIndex);
    }

    @Override
    public void itemSwipe(int direction) {

    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, count0, count1, free, freash, hot, limit, heart, score;
        LinearLayout linear_heart;
        TextView tv1, rprice, rsprice, discount;
        Context ctx;
        JSONObject json;
        LinearLayout item_linear;

        public RecycleHolder(Context ctx, View view, JSONObject json, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            this.json = json;
            this.ctx = ctx;
            if (viewType == TYPE_NORMAL) {
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
                linear_heart.setOnClickListener(this);
                heart = view.findViewById(R.id.heart);
                score = view.findViewById(R.id.score);
                itemView.setOnClickListener(this);
                //
                //layout
                view.setLayoutParams(new LinearLayout.LayoutParams(layout_width, layout_heigh));
                item_linear.setLayoutParams(new FrameLayout.LayoutParams((int) (layout_width - 15 * dm.density), (int) (layout_width - 15 * dm.density + 110 * dm.density)));
                //圖片
                resizeImageView(imageView, (int) (layout_width - 15 * dm.density), (int) (layout_width - 15 * dm.density));
                resizeImageView(count0, (int) (layout_width / 3 * 1.3), (int) (layout_width / 3 * 1.3));
                resizeImageView(count1, (int) (layout_width / 3 * 1.3 - 5 * dm.density), (int) (layout_width / 3 * 1.3 - 5 * dm.density));
                resizeImageView(discount, (int) (layout_width / 3 * 1.3 - 5 * dm.density) / 2, (int) (layout_width / 3 * 1.3 - 5 * dm.density) / 2);
                resizeImageView(free, (layout_width / 3), (layout_width / 3));
                resizeImageView(freash, (layout_width / 3), (int) (layout_width / 3 * 0.3));
                resizeImageView(hot, (layout_width / 3), (int) (layout_width / 3 * 0.3));
                resizeImageView(limit, (layout_width / 3), (int) (layout_width / 3 * 0.3));
            }
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition() - had_header;
            switch (view.getId()) {
                case R.id.linear_heart:
                    if (gv.getToken() != null) {

                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public JSONObject onTasking(Void... params) {
                                if (list.get(position).getFavorite()) {
                                    return new ProductJsonData().delFavoriteProduct(gv.getToken(), list.get(position).getPno());
                                } else {
                                    return new ProductJsonData().setFavorite(gv.getToken(), list.get(position).getPno());
                                }
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {

                                    if (list.get(position).getFavorite()) {
                                        list.get(position).setFavorite(false);
                                        heart.setImageResource(R.drawable.heart_off);


                                    } else {
                                        list.get(position).setFavorite(true);
                                        heart.setImageResource(R.drawable.heart_on);
                                    }
                                } else {
                                    toastMessageDialog.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                                    toastMessageDialog.confirm();
                                }

                            }
                        });
                    } else {
                        ctx.startActivity(new Intent(ctx, LoginActivity.class));
                    }
                    break;
                default:
                    if (list.size() > position) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new ProductJsonData().clickProduct(gv.getToken(), list.get(position).getPno());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {

                                }
                            }
                        });

                        Intent intent = new Intent(ctx, PcContentActivity.class);
                        intent.putExtra("pno", list.get(position).getPno());
                        intent.putExtra("title", list.get(position).getTitle());
                        ctx.startActivity(intent);
                    }
            }

        }
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        if (json != null) {
            list = ResolveJsonData.getJSONData(json);

        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public boolean setFilterMore(JSONObject json) {
        int presize = list.size();
        if (presize > 0) {
            if (json != null && ResolveJsonData.getJSONData(json).size() > 0) {
                list.addAll(ResolveJsonData.getJSONData(json));
                notifyItemInserted(presize + 1);
                //  notifyItemChanged(presize + 1, itemsList.size() + 1);
                return true;
            } else {
                new ToastMessageDialog(ctx, "沒有更多了").show();
                return false;
            }
        }
        return false;
    }

    public void recycleBitmaps() {
        mHeaderView = null;
        ctx = null;
        json = null;
        view = null;
        list = null;
        dm = null;
    }

}
