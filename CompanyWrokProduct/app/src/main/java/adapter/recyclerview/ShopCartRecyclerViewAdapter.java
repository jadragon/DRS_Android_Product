package adapter.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.Component.AutoHorizontalTextView;
import library.Component.ToastMessageDialog;
import library.GetJsonData.ShopCartJsonData;

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
    private ShopCartRecyclerViewAdapter.DataChangeListener dataChangeListener;
    private List<Item> items;
    private Item item;
    GlobalVariable gv;
    int size;
    String mvip = "1";
    ToastMessageDialog toastMessageDialog;

    public void setMvip(String mvip) {
        this.mvip = mvip;
    }

    public ShopCartRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;
        toastMessageDialog = new ToastMessageDialog(ctx);
        dm = ctx.getResources().getDisplayMetrics();
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {

            title_list = AnalyzeShopCart.getCartInformation(json);
            content_list = AnalyzeShopCart.getCartItemArray(json);
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
            item.setCheck(true);
            item.setIndex(i);
            items.add(item);
            size++;
            for (int j = 0; j < content_list.get(i).size(); j++) {
                item = new Item(TYPE_NORMAL,
                        content_list.get(i).get(j).get("morno"),
                        content_list.get(i).get(j).get("stotal"),
                        content_list.get(i).get(j).get("pname"),
                        content_list.get(i).get(j).get("img"),
                        content_list.get(i).get(j).get("color"),
                        content_list.get(i).get(j).get("size"),
                        content_list.get(i).get(j).get("weight"),
                        content_list.get(i).get(j).get("price"),
                        content_list.get(i).get(j).get("sprice"),
                        content_list.get(i).get(j).get("mtotal"));
                item.setCheck(true);
                item.setIndex(i);
                item.countTotal_price();
                items.add(item);
                size++;
            }
            item = new Item(TYPE_FOOTER1, title_list.get(i).get("subtotal"));
            item.setIndex(i);
            items.add(item);
            size++;
            if (!title_list.get(i).get("discountInfo").equals("")) {
                item = new Item(TYPE_FOOTER2, title_list.get(i).get("discountInfo"));
                item.setIndex(i);
                items.add(item);
                size++;
            }
            if (!title_list.get(i).get("shippingInfo").equals("")) {
                item = new Item(TYPE_FOOTER3, title_list.get(i).get("shippingInfo"));
                item.setIndex(i);
                items.add(item);
                size++;
            }
        }

    }

    @Override
    public ShopCartRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density));
        params.setMargins((int) (55 * dm.density), 0, (int) (30 * dm.density), 0);
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
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD);
            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL + Gravity.END);
            layout.addView(view);
            view = new TextView(ctx);
            view.setTag("footer1_title");
            ((TextView) view).setText("小計:");
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
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
        params.height = (int) (dm.heightPixels / 5.5);
        params.setMargins(0,0,0,0);
        view.setLayoutParams(params);
        view.setTag("content");
        resizeImageView(view.findViewById(R.id.viewitem_cart_content_img), (int) (dm.heightPixels / 5.5), (int) (dm.heightPixels / 5.5));
        return new ShopCartRecyclerViewAdapter.RecycleHolder(ctx, view, json);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_NORMAL) {
                ImageLoader.getInstance().displayImage(items.get(position).getImg(), holder.viewitem_cart_content_img);
                holder.viewitem_cart_content_title.setText(items.get(position).getPname());
                holder.viewitem_cart_content_color.setText(items.get(position).getColor());
                holder.viewitem_cart_content_size.setText(items.get(position).getSize());
                holder.viewitem_cart_content_total.setText(items.get(position).getStotal());
                holder.viewitem_cart_content_price.setText("$" + getDeciamlString(items.get(position).getSprice()));
                if (items.get(position).isCheck())
                    holder.viewitem_cart_content_checkbox.setChecked(true);
                else
                    holder.viewitem_cart_content_checkbox.setChecked(false);
            } else if (getItemViewType(position) == TYPE_HEADER) {
                ImageLoader.getInstance().displayImage(items.get(position).getSimg(), holder.viewitem_cart_title_img);
                holder.viewitem_cart_title_txt.setText(items.get(position).getSname());
                if (items.get(position).isCheck())
                    holder.viewitem_cart_title_checkbox.setChecked(true);
                else
                    holder.viewitem_cart_title_checkbox.setChecked(false);

            } else if (getItemViewType(position) == TYPE_FOOTER1) {
                holder.footer1_title.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
                holder.footer1_total.setText("$" + getDeciamlString(items.get(position).getSubtotal()));
                holder.footer1_total.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (getItemViewType(position) == TYPE_FOOTER2) {
                holder.footer2.setText("店家優惠:" + items.get(position).getDiscountInfo());
                holder.footer2.setTextColor(ctx.getResources().getColor(android.R.color.tertiary_text_dark));
            } else if (getItemViewType(position) == TYPE_FOOTER3) {
                holder.footer3.setText(items.get(position).getShippingInfo());
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
        if (items.get(position).getType() == TYPE_HEADER)
            return TYPE_HEADER;
        if (items.get(position).getType() == TYPE_FOOTER1)
            return TYPE_FOOTER1;
        if (items.get(position).getType() == TYPE_FOOTER2)
            return TYPE_FOOTER2;
        if (items.get(position).getType() == TYPE_FOOTER3)
            return TYPE_FOOTER3;
        return TYPE_NORMAL;
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

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Context ctx;
        JSONObject json;
        CheckBox viewitem_cart_title_checkbox, viewitem_cart_content_checkbox;
        ImageView viewitem_cart_title_img;
        ImageView viewitem_cart_content_img;
        TextView viewitem_cart_title_txt;
        TextView viewitem_cart_content_title, viewitem_cart_content_color, viewitem_cart_content_size, viewitem_cart_content_total, viewitem_cart_content_price;
        TextView footer1_title, footer1_total, footer2, footer3;
        Button viewitem_cart_content_increase, viewitem_cart_content_decrease;
        int position;
        AlertDialog.Builder builder;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            itemView.setOnLongClickListener(this);
            if (view.getTag().equals("header")) {
                viewitem_cart_title_img = view.findViewById(R.id.viewitem_cart_title_img);
                viewitem_cart_title_txt = view.findViewById(R.id.viewitem_cart_title_txt);
                viewitem_cart_title_checkbox = view.findViewById(R.id.viewitem_cart_title_checkbox);
                viewitem_cart_title_checkbox.setOnClickListener(this);
                viewitem_cart_title_checkbox.setTag("header");
                itemView.setTag("header");
            } else if (view.getTag().equals("footer1")) {
                footer1_title = view.findViewWithTag("footer1_title");
                footer1_total = view.findViewWithTag("footer1_total");
                itemView.setTag("footer1");
            } else if (view.getTag().equals("footer2")) {
                footer2 = (TextView) view;
                itemView.setTag("footer2");
            } else if (view.getTag().equals("footer3")) {
                footer3 = (TextView) view;
                itemView.setTag("footer3");
            } else if (view.getTag().equals("content")) {
                viewitem_cart_content_img = view.findViewById(R.id.viewitem_cart_content_img);
                viewitem_cart_content_title = view.findViewById(R.id.viewitem_cart_content_title);
                viewitem_cart_content_color = view.findViewById(R.id.viewitem_cart_content_color);
                viewitem_cart_content_size = view.findViewById(R.id.viewitem_cart_content_size);
                viewitem_cart_content_total = view.findViewById(R.id.viewitem_cart_content_total);
                viewitem_cart_content_price = view.findViewById(R.id.viewitem_cart_content_price);
                viewitem_cart_content_checkbox = view.findViewById(R.id.viewitem_cart_content_checkbox);
                viewitem_cart_content_increase = view.findViewById(R.id.viewitem_cart_content_increase);
                viewitem_cart_content_decrease = view.findViewById(R.id.viewitem_cart_content_decrease);
                viewitem_cart_content_increase.setOnClickListener(this);
                viewitem_cart_content_increase.setTag("increase");
                viewitem_cart_content_decrease.setOnClickListener(this);
                viewitem_cart_content_decrease.setTag("decrease");
                viewitem_cart_content_checkbox.setOnClickListener(this);
                viewitem_cart_content_checkbox.setTag("content");
                itemView.setTag("content");
            }

        }

        @Override
        public boolean onLongClick(View v) {
            position = getAdapterPosition();
            switch (v.getTag() + "") {
                case "content":
                    builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("是否要取消此商品?").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final JSONObject jsonObject = new ShopCartJsonData().delCartProduct(gv.getToken(), items.get(position).getMorno());
                                        json = new ShopCartJsonData().getCart(gv.getToken(), mvip);
                                        if (jsonObject.getBoolean("Success")) {
                                            new Handler(ctx.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setFilter(json);
                                                    try {
                                                        toastMessageDialog.setMessageText(jsonObject.getString("Message"));
                                                        toastMessageDialog.confirm();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    updatePrice();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    //填充对话框的布局
                    /*
                    builder .setItems(new String[]{"刪除", "取消"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    break;
                                case 1:
                                    builder.dismiss();
                                    break;
                            }
                        }
                    });
                    */
                    AlertDialog dialog = builder.create();
                    //点击dialog之外的区域禁止取消dialog
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    break;
                case "header":
                    break;
                case "footer1":
                    break;
                case "footer2":
                    break;
                case "footer3":
                    break;

            }
            return false;
        }

        @Override
        public void onClick(final View view) {
            position = getAdapterPosition();
            switch (view.getTag() + "") {
                case "header"://當header點擊時
                    if (!items.get(position).isCheck()) {//當header為非選取狀態
                        items.get(position).setCheck(true);
                        for (int i = 1; i < content_list.get(items.get(position).getIndex()).size() + 1; i++) {
                            items.get(position + i).setCheck(true);
                            items.get(position + i).countTotal_price();
                        }
                    } else {//當header為選取狀態
                        items.get(position).setCheck(false);
                        for (int i = 1; i < content_list.get(items.get(position).getIndex()).size() + 1; i++) {
                            items.get(position + i).setCheck(false);
                            items.get(position + i).setTotal_price(0);
                        }
                    }
                    notifyItemRangeChanged(position, content_list.get(items.get(position).getIndex()).size() + 1, 0);
                    updatePrice();
                    break;
                case "content"://當content點擊時
                    if (!items.get(position).isCheck()) {//當content為非選取狀態
                        for (int i = items.size() - 1; i >= 0; i--) {
                            if (items.get(i).getType() == TYPE_HEADER && position > i) {
                                position = i;
                                break;
                            }
                        }
                        //判斷當前header的子itemu全選時，header轉為選取狀態
                        items.get(position).setCheck(true);
                        items.get(getAdapterPosition()).setCheck(true);
                        for (int i = 1; i < content_list.get(items.get(position).getIndex()).size() + 1; i++) {
                            if (!items.get(position + i).isCheck()) {
                                items.get(position).setCheck(false);
                                break;
                            }
                        }
                        notifyItemChanged(position, 0);

                        viewitem_cart_content_checkbox.setChecked(true);
                        items.get(getAdapterPosition()).countTotal_price();
                    } else {//當content為選取狀態
                        //header
                        for (int i = items.size() - 1; i >= 0; i--) {
                            if (items.get(i).getType() == TYPE_HEADER && position > i) {
                                position = i;
                                break;
                            }
                        }
                        items.get(position).setCheck(false);
                        notifyItemChanged(position, 0);
                        //content
                        items.get(getAdapterPosition()).setCheck(false);
                        viewitem_cart_content_checkbox.setChecked(false);
                        items.get(getAdapterPosition()).setTotal_price(0);

                    }
                    updatePrice();
                    break;
                case "increase"://當increase點擊時
                    if (position != -1 && Integer.parseInt(items.get(position).getStotal()) < Integer.parseInt(items.get(position).getMtotal())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int total = Integer.parseInt(items.get(position).getStotal()) + 1;
                                new ShopCartJsonData().addCartProduct(gv.getToken(), items.get(position).getMorno(),
                                        total);
                                json = new ShopCartJsonData().getCart(gv.getToken(), mvip);
                                new Handler(ctx.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < items.size(); i++) {
                                            items.get(i).setCheck(false);
                                            items.get(i).setTotal_price(0);
                                        }
                                        setFilter(json);

                                        updatePrice();
                                    }
                                });
                            }
                        }).start();
                    }

                    break;
                case "decrease"://當decrease點擊時
                    if (position != -1 && Integer.parseInt(items.get(position).getStotal()) > 1) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int total = Integer.parseInt(items.get(position).getStotal()) - 1;
                                new ShopCartJsonData().addCartProduct(gv.getToken(), items.get(position).getMorno(),
                                        total);
                                json = new ShopCartJsonData().getCart(gv.getToken(), mvip);
                                new Handler(ctx.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < items.size(); i++) {
                                            items.get(i).setCheck(false);
                                            items.get(i).setTotal_price(0);
                                        }
                                        setFilter(json);
                                        updatePrice();
                                    }
                                });

                            }
                        }).start();
                    }

                    break;
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

    public String showMornoString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCheck() && items.get(i).getMorno() != null) {
                if (stringBuilder.length() != 0)
                    stringBuilder.append(",");
                stringBuilder.append(items.get(i).getMorno());
            }
        }
        return stringBuilder.toString();
    }

    public void setClickListener(ShopCartRecyclerViewAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDataChangeListener(ShopCartRecyclerViewAdapter.DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public interface ClickListener {
        void ItemClicked(int count);
    }

    public interface DataChangeListener {
        void ItemClicked();
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        if (AnalyzeShopCart.getCartInformation(json) != null) {
            title_list = AnalyzeShopCart.getCartInformation(json);
            content_list = AnalyzeShopCart.getCartItemArray(json);
        }
        initItems();
        notifyDataSetChanged();
        if (dataChangeListener != null) {
            dataChangeListener.ItemClicked();
        }
    }

    private class Item {
        int type;
        int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        //=======header
        String sno;
        String sname;
        String simg;
        //=======footer
        String subtotal;
        String discountInfo;
        String shippingInfo;
        //=======content
        boolean check;
        int total_price;
        String morno;
        String stotal;
        String pname;
        String img;
        String color;
        String size;
        String weight;
        String price;
        String sprice;
        String mtotal;

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
         * footer
         */
        public Item(int type, String footer) {
            this.type = type;
            switch (type) {
                case TYPE_FOOTER1:
                    this.subtotal = footer;
                    break;
                case TYPE_FOOTER2:
                    this.discountInfo = footer;
                    break;
                case TYPE_FOOTER3:
                    this.shippingInfo = footer;
                    break;
            }
        }

        /**
         * content
         */
        public Item(int type, String morno, String stotal, String pname, String img, String color, String size, String weight, String price, String sprice, String mtotal) {
            this.type = type;
            this.morno = morno;
            this.stotal = stotal;
            this.pname = pname;
            this.img = img;
            this.color = color;
            this.size = size;
            this.weight = weight;
            this.price = price;
            this.sprice = sprice;
            this.mtotal = mtotal;
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

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
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

        public String getMorno() {
            return morno;
        }

        public void setMorno(String morno) {
            this.morno = morno;
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

        public String getMtotal() {
            return mtotal;
        }

        public void setMtotal(String mtotal) {
            this.mtotal = mtotal;
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

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public int countTotal_price() {
            total_price = Integer.parseInt(stotal) * Integer.parseInt(sprice);
            return total_price;
        }

    }

}
