package adapter.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.GetJsonData.GetInformationByPHP;
import library.AnalyzeJSON.ResolveJsonData;
import library.GetJsonData.ShopCartJsonData;

public class CountRecyclerViewAdapter extends RecyclerView.Adapter<CountRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_SHIPWAY = 2;
    public static final int TYPE_DISCOUNT = 3;
    public static final int TYPE_COUNT = 4;
    private Context ctx;
    private JSONObject json;
    private DisplayMetrics dm;
    private ArrayList<Map<String, String>> title_list;
    private ArrayList<ArrayList<Map<String, String>>> content_list;
    private CountRecyclerViewAdapter.ClickListener clickListener;
    private List<Item> items;
    private Item item;
    String token;
    int size;

    public CountRecyclerViewAdapter(Context ctx, JSONObject json, String token) {
        this(ctx, json);
        this.token = token;

    }

    public CountRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {

            title_list = ResolveJsonData.getCountInformation(json);
            content_list = ResolveJsonData.getCountItemArray(json);
        } else {
            title_list = new ArrayList<>();
            content_list = new ArrayList<>();
        }
        //初始化checkbox
        initItems();
    }

    public void initItems() {
        items = new ArrayList<>();
        size = 0;
        //確認是否要footer
        //checkJsonData();
        for (int i = 0; i < title_list.size(); i++) {
            item = new Item(TYPE_HEADER, title_list.get(i).get("sno"), title_list.get(i).get("sname"), title_list.get(i).get("simg"));
            item.setIndex(i);
            items.add(item);
            size++;
            for (int j = 0; j < content_list.get(i).size(); j++) {
                item = new Item(TYPE_CONTENT,
                        content_list.get(i).get(j).get("mcpno"),
                        content_list.get(i).get(j).get("stotal"),
                        content_list.get(i).get(j).get("pname"),
                        content_list.get(i).get(j).get("img"),
                        content_list.get(i).get(j).get("color"),
                        content_list.get(i).get(j).get("size"),
                        content_list.get(i).get(j).get("weight"),
                        content_list.get(i).get(j).get("price"),
                        content_list.get(i).get(j).get("sprice"));
                item.setIndex(i);
                item.countTotal_price();
                items.add(item);
                size++;
            }
            //收費方式
            item = new Item(TYPE_SHIPWAY,
                    title_list.get(i).get("shippingInfo"),
                    title_list.get(i).get("shippingStyle"),
                    title_list.get(i).get("shippingName"),
                    title_list.get(i).get("shippingSname"),
                    title_list.get(i).get("shippingSid"),
                    title_list.get(i).get("shippingAddress"),
                    Integer.parseInt(title_list.get(i).get("shippingPay")),
                    title_list.get(i).get("note"));
            item.setIndex(i);
            items.add(item);
            size++;
            //優惠
            if (!title_list.get(i).get("discountInfo").equals("")) {
                item = new Item(TYPE_DISCOUNT, title_list.get(i).get("discountInfo"), title_list.get(i).get("discount"));
                item.setIndex(i);
                items.add(item);
                size++;
            }
            //結帳
            item = new Item(TYPE_COUNT, Integer.parseInt(title_list.get(i).get("shippingPay")), Integer.parseInt(title_list.get(i).get("subtotal")));
            item.setIndex(i);
            items.add(item);
            size++;
        }
        Log.e("VS", getItemCount() + ":" + size + ":" + items.size());
    }

    @Override
    public CountRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density));
        params.setMargins((int) (10 * dm.density), 0, (int) (30 * dm.density), 0);
        //頁面
        if (viewType == TYPE_HEADER) {
            TextView tv = new TextView(ctx);
            tv.setTag("header");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setBackgroundColor(ctx.getResources().getColor(R.color.default_gray));
            params.setMargins((int) (10 * dm.density), 0, 0, 0);
            params.height = (int) (60 * dm.density);
            tv.setLayoutParams(params);
            return new CountRecyclerViewAdapter.RecycleHolder(ctx, tv, json);
        }
        if (viewType == TYPE_SHIPWAY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_shipways, parent, false);
            view.setTag("shipway");
            return new CountRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        if (viewType == TYPE_DISCOUNT) {
            //layout
            LinearLayout linearLayout = new LinearLayout(ctx);
            params.weight = dm.widthPixels;
            params.height = (int) (50 * dm.density);
            params.setMargins(0, 0, 0, 0);
            linearLayout.setLayoutParams(params);
            linearLayout.setPadding((int) (10 * dm.density), 0, (int) (30 * dm.density), 0);
            linearLayout.setBackgroundColor(ctx.getResources().getColor(R.color.gainsboro));
            //text
            linearLayout.setTag("discount");
            TextView tv = new TextView(ctx);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setMaxLines(1);
            tv.setText("店家優惠:");
            tv.setTag("discount_title");
            linearLayout.addView(tv);
            //text
            tv = new TextView(ctx);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setTag("discount_total");
            tv.setText("0");
            linearLayout.addView(tv);
            return new CountRecyclerViewAdapter.RecycleHolder(ctx, linearLayout, json);
        }
        if (viewType == TYPE_COUNT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_total, parent, false);
            view.setTag("count");
            return new CountRecyclerViewAdapter.RecycleHolder(ctx, view, json);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_content, parent, false);
        params.height = dm.heightPixels / 5;
        params.setMarginStart(0);
        params.setMarginEnd(0);
        view.setLayoutParams(params);
        view.setTag("content");
        resizeImageView(view.findViewById(R.id.viewitem_count_content_img), dm.heightPixels / 5, dm.heightPixels / 5);
        return new CountRecyclerViewAdapter.RecycleHolder(ctx, view, json);

    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_CONTENT) {
                ImageLoader.getInstance().displayImage(items.get(position).getImg(), holder.viewitem_count_content_img);
                holder.viewitem_count_content_title.setText(items.get(position).getPname());
                holder.viewitem_count_content_color.setText(items.get(position).getColor());
                holder.viewitem_count_content_size.setText(items.get(position).getSize());
                holder.viewitem_count_content_total.setText("x" + items.get(position).getStotal());
                holder.viewitem_count_content_price.setText("$" + getDeciamlString(items.get(position).getSprice()));
            } else if (getItemViewType(position) == TYPE_HEADER) {
                holder.viewitem_count_title_txt.setText(items.get(position).getSname());
            } else if (getItemViewType(position) == TYPE_SHIPWAY) {
                holder.viewitem_count_shipways_style.setText(items.get(position).getShippingStyle());
                holder.viewitem_count_shipways_pay.setText("$" + items.get(position).getShippingPay());
                holder.viewitem_count_shipways_address.setText(items.get(position).getShippingAddress());
                holder.viewitem_count_shipways_name.setText(items.get(position).getShippingName());
            } else if (getItemViewType(position) == TYPE_DISCOUNT) {
                holder.discount_title.setText("店家優惠:" + items.get(position).getDiscountInfo());
                holder.discount_title.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
                holder.discount_total.setText("$" + items.get(position).getDiscount());
            } else if (getItemViewType(position) == TYPE_COUNT) {
                holder.viewitem_count_total_shippay.setText("$" + items.get(position).getShippingPay());
                holder.viewitem_count_total_subtotal.setText("$" + items.get(position).getSubtotal());
            }
        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
            int type = (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type) {
                case 0:
                    if (getItemViewType(position) == TYPE_HEADER) {


                    } else if (getItemViewType(position) == TYPE_CONTENT) {

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
        if (items.get(position).getType() == TYPE_HEADER)
            return TYPE_HEADER;
        if (items.get(position).getType() == TYPE_SHIPWAY)
            return TYPE_SHIPWAY;
        if (items.get(position).getType() == TYPE_DISCOUNT)
            return TYPE_DISCOUNT;
        if (items.get(position).getType() == TYPE_COUNT)
            return TYPE_COUNT;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return items.size();

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

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {
        Context ctx;
        JSONObject json;
        ImageView viewitem_count_content_img;
        TextView viewitem_count_title_txt;
        TextView viewitem_count_content_title, viewitem_count_content_color, viewitem_count_content_size, viewitem_count_content_total, viewitem_count_content_price;
        TextView viewitem_count_shipways_style, viewitem_count_shipways_pay, viewitem_count_shipways_address, viewitem_count_shipways_name;
        TextView viewitem_count_total_shippay, viewitem_count_total_subtotal;
        TextView discount_title, discount_total;
        EditText viewitem_count_shipways_note;
        int position;

        public RecycleHolder(final Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            if (view.getTag().equals("header")) {
                viewitem_count_title_txt = (TextView) view;
            } else if (view.getTag().equals("shipway")) {
                viewitem_count_shipways_style = view.findViewById(R.id.viewitem_count_shipways_style);
                viewitem_count_shipways_pay = view.findViewById(R.id.viewitem_count_shipways_pay);
                viewitem_count_shipways_address = view.findViewById(R.id.viewitem_count_shipways_address);
                viewitem_count_shipways_name = view.findViewById(R.id.viewitem_count_shipways_name);
                viewitem_count_shipways_note = view.findViewById(R.id.viewitem_count_shipways_note);
                viewitem_count_shipways_note.setOnFocusChangeListener(this);
                viewitem_count_shipways_note.setTag("note");
                itemView.setTag("shipway");
            } else if (view.getTag().equals("discount")) {
                discount_title = view.findViewWithTag("discount_title");
                discount_total = view.findViewWithTag("discount_total");
            } else if (view.getTag().equals("content")) {
                viewitem_count_content_img = view.findViewById(R.id.viewitem_count_content_img);
                viewitem_count_content_title = view.findViewById(R.id.viewitem_count_content_title);
                viewitem_count_content_color = view.findViewById(R.id.viewitem_count_content_color);
                viewitem_count_content_size = view.findViewById(R.id.viewitem_count_content_size);
                viewitem_count_content_total = view.findViewById(R.id.viewitem_count_content_total);
                viewitem_count_content_price = view.findViewById(R.id.viewitem_count_content_price);
            } else if (view.getTag().equals("count")) {
                viewitem_count_total_shippay = view.findViewById(R.id.viewitem_count_total_shippay);
                viewitem_count_total_subtotal = view.findViewById(R.id.viewitem_count_total_subtotal);
            }
            itemView.setFocusableInTouchMode(true);
            itemView.setFocusable(true);
        }


        @Override
        public void onClick(final View view) {
            position = getAdapterPosition();
            switch (view.getTag() + "") {
                case "shipway"://當shipway點擊時
                    break;

            }
        }


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getType() == TYPE_HEADER && getAdapterPosition() > i) {
                    position = i;
                    break;
                }
            }
            if (!hasFocus) {
                InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(viewitem_count_shipways_note.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean success = new ShopCartJsonData().setStoreNote(token, items.get(position).getSno(), viewitem_count_shipways_note.getText() != null ? viewitem_count_shipways_note.getText().toString() : "").getBoolean("Success");
                            if (success) {
                                new android.os.Handler(ctx.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctx, viewitem_count_shipways_note.getText(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

            }
        }
    }

    private void updatePrice() {
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            count += items.get(i).getTotal_price();
        }
        clickListener.ItemClicked(count);
    }

    public int showPrice() {
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            count += items.get(i).getTotal_price();
        }
        return count;
    }

    public void setClickListener(CountRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(int count);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        if (ResolveJsonData.getCartInformation(json) != null) {
            title_list = ResolveJsonData.getCartInformation(json);
            content_list = ResolveJsonData.getCartItemArray(json);
        }
        initItems();
        notifyDataSetChanged();
    }

    private class Item {
        private int type;
        private int index;


        //=======header
        private String sno;
        private String sname;
        private String simg;
        //=======footer
        private int subtotal;
        private String discountInfo;
        private String discount;
        private String shippingInfo;


        private String shippingStyle;
        private String shippingName;
        private String shippingSname;
        private String shippingSid;
        private String shippingAddress;
        private int shippingPay;
        private String note;

        //=======content
        private int total_price;
        private String mcpno;
        private String stotal;
        private String pname;
        private String img;
        private String color;
        private String size;
        private String weight;
        private String price;
        private String sprice;

        public Item() {
        }

        public Item(int type) {
            this.type = type;
        }

        /**
         * header
         */
        public Item(int type, String sno, String sname, String simg) {
            this.type = type;
            this.sno = sno;
            this.sname = sname;
            this.simg = simg;
        }

        /**
         * shipway
         */
        public Item(int type, String shippingInfo, String shippingStyle, String shippingName, String shippingSname, String shippingSid, String shippingAddress, int shippingPay, String note) {
            this.type = type;
            this.shippingInfo = shippingInfo;
            this.shippingStyle = shippingStyle;
            this.shippingName = shippingName;
            this.shippingSname = shippingSname;
            this.shippingSid = shippingSid;
            this.shippingAddress = shippingAddress;
            this.shippingPay = shippingPay;
            this.note = note;
        }

        /**
         * discount
         */
        public Item(int type, String discountInfo, String discount) {
            this.type = type;
            this.discountInfo = discountInfo;
            this.discount = discount;
        }

        /**
         * count
         */
        public Item(int type, int shippingPay, int subtotal) {
            this.type = type;
            this.shippingPay = shippingPay;
            this.subtotal = subtotal;
        }


        /**
         * content
         */
        public Item(int type, String mcpno, String stotal, String pname, String img, String color, String size, String weight, String price, String sprice) {
            this.type = type;
            this.mcpno = mcpno;
            this.stotal = stotal;
            this.pname = pname;
            this.img = img;
            this.color = color;
            this.size = size;
            this.weight = weight;
            this.price = price;
            this.sprice = sprice;
        }

        public String getSno() {
            return sno;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public int getSubtotal() {
            return subtotal;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setSubtotal(int subtotal) {
            this.subtotal = subtotal;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getShippingStyle() {
            return shippingStyle;
        }

        public void setShippingStyle(String shippingStyle) {
            this.shippingStyle = shippingStyle;
        }

        public String getShippingName() {
            return shippingName;
        }

        public void setShippingName(String shippingName) {
            this.shippingName = shippingName;
        }

        public String getShippingSname() {
            return shippingSname;
        }

        public void setShippingSname(String shippingSname) {
            this.shippingSname = shippingSname;
        }

        public String getShippingSid() {
            return shippingSid;
        }

        public void setShippingSid(String shippingSid) {
            this.shippingSid = shippingSid;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public int getShippingPay() {
            return shippingPay;
        }

        public void setShippingPay(int shippingPay) {
            this.shippingPay = shippingPay;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDiscountInfo() {
            return discountInfo;
        }

        public void setDiscountInfo(String discountInfo) {
            this.discountInfo = discountInfo;
        }

        public String getShippingInfo() {
            return shippingInfo;
        }

        public void setShippingInfo(String shippingInfo) {
            this.shippingInfo = shippingInfo;
        }

        public String getMcpno() {
            return mcpno;
        }

        public void setMcpno(String mcpno) {
            this.mcpno = mcpno;
        }

        public String getStotal() {
            return stotal;
        }

        public void setStotal(String stotal) {
            this.stotal = stotal;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int countTotal_price() {
            total_price = Integer.parseInt(stotal) * Integer.parseInt(sprice);
            return total_price;
        }


    }

}
