package adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.ResolveJsonData;
import library.component.AutoHorizontalTextView;

public class ShopCartRecyclerViewAdapter extends RecyclerView.Adapter<ShopCartRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER1 = 2;
    public static final int TYPE_FOOTER2 = 3;
    public static final int TYPE_FOOTER3 = 4;
    private Context ctx;
    private JSONObject json;
    private View view;
    private DisplayMetrics dm;
    private ArrayList<Map<String, String>> title_list;
    private ArrayList<ArrayList<Map<String, String>>> content_list;
    private ShopCartRecyclerViewAdapter.ClickListener clickListener;
    private ArrayList<Integer> title_position;
    private List<Item> items;
    private Item item;

    public ShopCartRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
            title_list = ResolveJsonData.getCartInformation(json);
            content_list = ResolveJsonData.getCartItemArray(json);
        } else {
            title_list = new ArrayList<>();
            content_list = new ArrayList<>();
        }
        //廠商標題位置
        title_position = new ArrayList<>();
        int count = 0;
        title_position.add(count);
        for (int i = 0; i < content_list.size() - 1; i++) {
            count += (1 + content_list.get(i).size());
            title_position.add(count);
        }
        //初始化checkbox

        items = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            item = new Item();
            items.add(item);
        }
        Log.e("count", items + "");

    }

    @Override
    public ShopCartRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (40 * dm.density));
        params.setMarginStart((int) (70 * dm.density));
        params.setMarginEnd((int) (10 * dm.density));
        //頁面
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_cart_title, parent, false);
            view.setTag("header");
            return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        if (viewType == TYPE_FOOTER1) {
            FrameLayout layout = new FrameLayout(ctx);
            view = new TextView(ctx);
            view.setTag("footer1_total");
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL + Gravity.END);
            layout.addView(view);
            view = new TextView(ctx);
            view.setTag("footer1_title");
            ((TextView) view).setText("小計:");
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL + Gravity.START);
            layout.addView(view);
            layout.setLayoutParams(params);
            layout.setTag("footer1");
            return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, layout, json);
        }
        if (viewType == TYPE_FOOTER2) {
            view = new AutoHorizontalTextView(ctx);
            view.setTag("footer2");
            ((AutoHorizontalTextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            view.setLayoutParams(params);
            ((AutoHorizontalTextView) view).setGravity(Gravity.CENTER_VERTICAL);
            return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        if (viewType == TYPE_FOOTER3) {
            view = new AutoHorizontalTextView(ctx);
            view.setTag("footer3");
            ((AutoHorizontalTextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            view.setLayoutParams(params);
            ((AutoHorizontalTextView) view).setGravity(Gravity.CENTER_VERTICAL);
            ((AutoHorizontalTextView) view).setMaxLines(1);
            return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_cart_content, parent, false);
        params.height = dm.heightPixels / 5;
        params.setMarginStart(0);
        params.setMarginEnd(0);
        view.setLayoutParams(params);
        view.setTag("content");
        resizeImageView(view.findViewById(R.id.viewitem_cart_content_img), dm.heightPixels / 5, dm.heightPixels / 5);
        return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_NORMAL) {
                for (int i = 0; i < title_position.size(); i++) {
                    if (title_position.get(i) < position) {
                        ImageLoader.getInstance().displayImage(content_list.get(i).get(position - (i + 1)).get("img"), holder.viewitem_cart_content_img);
                        holder.viewitem_cart_content_title.setText(content_list.get(i).get(position - (i + 1)).get("pname"));
                        holder.viewitem_cart_content_color.setText(content_list.get(i).get(position - (i + 1)).get("color"));
                        holder.viewitem_cart_content_size.setText(content_list.get(i).get(position - (i + 1)).get("size"));
                        holder.viewitem_cart_content_total.setText(content_list.get(i).get(position - (i + 1)).get("stotal"));
                        holder.viewitem_cart_content_price.setText("$" + getDeciamlString(content_list.get(i).get(position - (i + 1)).get("sprice")));

                        if (items.get(position).isCheck())
                            holder.viewitem_cart_content_checkbox.setChecked(true);
                        else
                            holder.viewitem_cart_content_checkbox.setChecked(false);

                    }
                }
            } else if (getItemViewType(position) == TYPE_HEADER) {
                for (int i = 0; i < title_position.size(); i++)
                    if (title_position.get(i) == position) {
                        ImageLoader.getInstance().displayImage(title_list.get(i).get("simg"), holder.viewitem_cart_title_img);
                        holder.viewitem_cart_title_txt.setText(title_list.get(i).get("sname"));
                        if (items.get(position).isCheck())
                            holder.viewitem_cart_title_checkbox.setChecked(true);
                        else
                            holder.viewitem_cart_title_checkbox.setChecked(false);
                    }

            } else if (getItemViewType(position) == TYPE_FOOTER1) {
                for (int index : title_position) {
                    if (position > index) {
                        position = index;
                        break;
                    }
                }
                holder.footer1_title.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
                holder.footer1_total.setText("$" + getDeciamlString(title_list.get(position).get("subtotal")));
                holder.footer1_total.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (getItemViewType(position) == TYPE_FOOTER2) {
                for (int index : title_position) {
                    if (position > index) {
                        position = index;
                        break;
                    }
                }
                holder.footer2.setText(title_list.get(position).get("discountInfo"));
                holder.footer2.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
            } else if (getItemViewType(position) == TYPE_FOOTER3) {
                for (int index : title_position) {
                    if (position > index) {
                        position = index;
                        break;
                    }
                }
                holder.footer3.setText(title_list.get(position).get("shippingInfo"));
                holder.footer3.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
            }
        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
            int type = (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type) {
                case 0:
                    if (getItemViewType(position) == TYPE_HEADER) {

                        if (items.get(position).isCheck())
                            holder.viewitem_cart_title_checkbox.setChecked(true);
                        else
                            holder.viewitem_cart_title_checkbox.setChecked(false);

                    } else if (getItemViewType(position) == TYPE_NORMAL) {

                        if (items.get(position).isCheck())
                            holder.viewitem_cart_content_checkbox.setChecked(true);
                        else
                            holder.viewitem_cart_content_checkbox.setChecked(false);
                    }
                    break;
            }
        }

    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        //不用了
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < title_position.size(); i++) {
            if (position == title_position.get(i))
                return TYPE_HEADER;
            if (position == title_position.get(i) + content_list.get(i).size() + i + 1)
                return TYPE_FOOTER1;
            if (position == title_position.get(i) + content_list.get(i).size() + i + 2)
                return TYPE_FOOTER2;
            if (position == title_position.get(i) + content_list.get(i).size() + i + 3)
                return TYPE_FOOTER3;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < content_list.size(); i++) {
            count += content_list.get(i).size();
        }
        count += content_list.size() * 4;
        return count;

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

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context ctx;
        JSONObject json;
        CheckBox viewitem_cart_title_checkbox, viewitem_cart_content_checkbox;
        ImageView viewitem_cart_title_img;
        ImageView viewitem_cart_content_img;
        TextView viewitem_cart_title_txt;
        TextView viewitem_cart_content_title, viewitem_cart_content_color, viewitem_cart_content_size, viewitem_cart_content_total, viewitem_cart_content_price;
        TextView footer1_title, footer1_total, footer2, footer3;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            viewitem_cart_content_img = view.findViewById(R.id.viewitem_cart_content_img);
            viewitem_cart_content_title = view.findViewById(R.id.viewitem_cart_content_title);
            viewitem_cart_content_color = view.findViewById(R.id.viewitem_cart_content_color);
            viewitem_cart_content_size = view.findViewById(R.id.viewitem_cart_content_size);
            viewitem_cart_content_total = view.findViewById(R.id.viewitem_cart_content_total);
            viewitem_cart_content_price = view.findViewById(R.id.viewitem_cart_content_price);
            viewitem_cart_content_checkbox = view.findViewById(R.id.viewitem_cart_content_checkbox);
            if (view.getTag().equals("header")) {
                viewitem_cart_title_img = view.findViewById(R.id.viewitem_cart_title_img);
                viewitem_cart_title_txt = view.findViewById(R.id.viewitem_cart_title_txt);
                viewitem_cart_title_checkbox = view.findViewById(R.id.viewitem_cart_title_checkbox);
                viewitem_cart_title_checkbox.setOnClickListener(this);
                viewitem_cart_title_checkbox.setTag("header");
            } else if (view.getTag().equals("footer1")) {
                footer1_title = view.findViewWithTag("footer1_title");
                footer1_total = view.findViewWithTag("footer1_total");
            } else if (view.getTag().equals("footer2")) {
                footer2 = (TextView) view;
            } else if (view.getTag().equals("footer3")) {
                footer3 = (TextView) view;
            } else if (view.getTag().equals("content")) {
                viewitem_cart_content_checkbox.setOnClickListener(this);
                viewitem_cart_content_checkbox.setTag("content");
            }
           // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            for (int index : title_position) {
                if (position >= index) {
                    position = index;
                    break;
                }
            }

            switch (view.getTag() + "") {
                case "header":
                    if (!items.get(position).isCheck()) {
                        position=position+(title_position.indexOf(position));
                        items.get(position).setCheck(true);
                        for (int i = 0; i < content_list.get(title_position.indexOf(getAdapterPosition())).size(); i++) {
                            items.get(position+i+1).setCheck(true);
                            items.get(position+i+1).setPrice(
                                    Integer.parseInt(content_list.get(title_position.indexOf(getAdapterPosition())).get(position+i).get("sprice"))*
                                    Integer.parseInt(content_list.get(title_position.indexOf(getAdapterPosition())).get(position+i).get("stotal")));
                        }
                    } else {
                        items.get(position).setCheck(false);
                        for (int i = 0; i < content_list.get(title_position.indexOf(position)).size(); i++) {
                            items.get(position+i+1).setCheck(false);
                            items.get(position+i+1).setPrice(0);
                        }
                    }
                    notifyItemRangeChanged(position, content_list.get(title_position.indexOf(position)).size() + 1, 0);
                    break;
                case "content":
                    position=getAdapterPosition()-(position+1);
                    if (!items.get(getAdapterPosition()).isCheck()) {
                        items.get(getAdapterPosition()).setCheck(true);
                        viewitem_cart_content_checkbox.setChecked(true);
                        items.get(getAdapterPosition()).setPrice(
                                Integer.parseInt(content_list.get(0).get(position).get("sprice")) *
                                Integer.parseInt(content_list.get(0).get(position).get("stotal")));

                    } else {
                        items.get(getAdapterPosition()).setCheck(false);
                        viewitem_cart_content_checkbox.setChecked(false);
                        items.get(getAdapterPosition()).setPrice(0);
                    }
                    break;
            }
            int count=0;
            for(int i=0;i<items.size();i++){
                count+=items.get(i).getPrice();
            }
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, count);
            }
        }

    }

    public void setClickListener(ShopCartRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, int count);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        content_list = ResolveJsonData.getCartItemArray(json);
        notifyDataSetChanged();
    }

    private class Item {
        boolean check;
        int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
