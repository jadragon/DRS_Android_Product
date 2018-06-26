package adapter.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.PayWayActivity;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.ShipWayActivity;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ReCountJsonData;

public class ReCountRecyclerViewAdapter extends RecyclerView.Adapter<ReCountRecyclerViewAdapter.RecycleHolder> {
    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_SHIPWAY = 2;
    public static final int TYPE_DISCOUNT = 3;
    public static final int TYPE_COUNT = 4;
    public static final int TYPE_PAY = 5;
    private Context ctx;
    private DisplayMetrics dm;
    private ArrayList<Map<String, String>> title_list, footer_coupon_list, footer_pay_list, footer_invoice_list;
    private ArrayList<ArrayList<Map<String, String>>> content_list;
    private List<Item> items;
    private FooterItem footerItem;
    private Item item;
    String token;
    String[] invoiceType;
    int count_type;

    public ReCountRecyclerViewAdapter(Context ctx, JSONObject json, int count_type) {
        this.ctx = ctx;
        this.count_type = count_type;
        token = ((GlobalVariable) ctx.getApplicationContext()).getToken();
        invoiceType = ctx.getResources().getStringArray(R.array.invoice_type);
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
            title_list = AnalyzeShopCart.getCheckoutData(json);
            content_list = AnalyzeShopCart.getCheckoutItemArray(json);
            footer_coupon_list = AnalyzeShopCart.getCheckoutCoupon(json);
            footer_pay_list = AnalyzeShopCart.getCheckoutPay(json);
            footer_invoice_list = AnalyzeShopCart.getCheckoutInvoice(json);
        } else {
            title_list = new ArrayList<>();
            content_list = new ArrayList<>();
            footer_coupon_list = new ArrayList<>();
            footer_pay_list = new ArrayList<>();
            footer_invoice_list = new ArrayList<>();
        }

        //初始化checkbox
        initItems();
        initFooterItem();
    }

    private void initFooterItem() {
        for (int i = 0; i < footer_pay_list.size(); i++) {
            footerItem = new FooterItem(footer_coupon_list.get(i).get("moprno"), footer_coupon_list.get(i).get("mcoupon"), Float.parseFloat(footer_coupon_list.get(i).get("mdiscount")),
                    footer_pay_list.get(i).get("opay"), footer_pay_list.get(i).get("pterms"), Float.parseFloat(footer_pay_list.get(i).get("xmoney")), Float.parseFloat(footer_pay_list.get(i).get("ymoney")),
                    Float.parseFloat(footer_pay_list.get(i).get("ewallet")), footer_pay_list.get(i).get("rpay"),
                    Integer.parseInt(footer_invoice_list.get(i).get("invoice")), footer_invoice_list.get(i).get("ctitle"), footer_invoice_list.get(i).get("vat")
            );
        }
    }

    public void initItems() {
        items = new ArrayList<>();

        //確認是否要footer
        //checkJsonData();
        for (int i = 0; i < title_list.size(); i++) {
            item = new Item(TYPE_HEADER, title_list.get(i).get("sno"), title_list.get(i).get("sname"), title_list.get(i).get("simg"));
            item.setIndex(i);
            items.add(item);
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
            }
            //收費方式
            item = new Item(TYPE_SHIPWAY,
                    title_list.get(i).get("shippingInfo"),
                    title_list.get(i).get("shippingStyle"),
                    title_list.get(i).get("shippingName"),
                    title_list.get(i).get("shippingSname"),
                    title_list.get(i).get("shippingSid"),
                    title_list.get(i).get("shippingAddress"),
                    title_list.get(i).get("shippingPay"),
                    title_list.get(i).get("note"));
            item.setIndex(i);
            items.add(item);
            //優惠
            if (!title_list.get(i).get("discountInfo").equals("")) {
                item = new Item(TYPE_DISCOUNT, title_list.get(i).get("discountInfo"), title_list.get(i).get("discount"));
                item.setIndex(i);
                items.add(item);
            }
            //結帳
            item = new Item(TYPE_COUNT);
            item.setShippingPay(title_list.get(i).get("shippingPay"));
            item.setSubtotal(title_list.get(i).get("subtotal"));
            item.setIndex(i);
            items.add(item);
        }
    }

    @Override
    public ReCountRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density));
        params.setMargins((int) (10 * dm.density), 0, (int) (30 * dm.density), 0);
        //頁面
        if (viewType == TYPE_HEADER) {
            TextView tv = new TextView(ctx);
            tv.setTag("header");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setBackgroundColor(ctx.getResources().getColor(R.color.default_gray));
            params.setMargins(0, 0, 0, 0);
            tv.setPadding((int) (dm.density * 10), 0, 0, 0);
            params.height = (int) (60 * dm.density);
            tv.setLayoutParams(params);
            return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, tv);
        }
        if (viewType == TYPE_SHIPWAY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_shipways, parent, false);
            view.setTag("shipway");
            return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, view);
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
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setMaxLines(1);
            tv.setTextColor(ctx.getResources().getColor(R.color.black));
            tv.setText("店家優惠:");
            tv.setTag("discount_title");
            linearLayout.addView(tv);
            //text
            tv = new TextView(ctx);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setTag("discount_total");
            tv.setText("0");
            linearLayout.addView(tv);
            return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, linearLayout);
        }
        if (viewType == TYPE_COUNT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_total, parent, false);
            view.setTag("count");
            return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, view);
        }
        if (viewType == TYPE_PAY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_paydetail, parent, false);
            view.setTag("pay");
            return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_count_content, parent, false);
        params.height = (int) (dm.heightPixels / 5.5);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        view.setLayoutParams(params);
        view.setTag("content");
        resizeImageView(view.findViewById(R.id.viewitem_count_content_img), (int) (dm.heightPixels / 5.5), (int) (dm.heightPixels / 5.5));
        return new ReCountRecyclerViewAdapter.RecycleHolder(ctx, view);

    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_CONTENT) {
                ImageLoader.getInstance().displayImage(items.get(position).getImg(), holder.viewitem_count_content_img);
                holder.viewitem_count_content_title.setText(items.get(position).getPname());
                holder.viewitem_count_content_color.setText(items.get(position).getColor());
                holder.viewitem_count_content_size.setText(items.get(position).getSize());
                holder.viewitem_count_content_total.setText("x" + getDeciamlString(items.get(position).getStotal()));
                holder.viewitem_count_content_price.setText("$" + getDeciamlString(items.get(position).getSprice()));
            } else if (getItemViewType(position) == TYPE_HEADER) {
                holder.viewitem_count_title_txt.setText(items.get(position).getSname());
            } else if (getItemViewType(position) == TYPE_SHIPWAY) {
                holder.viewitem_count_shipways_style.setText(items.get(position).getShippingStyle());
                holder.viewitem_count_shipways_pay.setText("$" + getDeciamlString(items.get(position).getShippingPay()));
                holder.viewitem_count_shipways_address.setText(items.get(position).getShippingAddress());
                holder.viewitem_count_shipways_name.setText(items.get(position).getShippingName());
            } else if (getItemViewType(position) == TYPE_DISCOUNT) {
                holder.discount_title.setText("店家優惠:" + items.get(position).getDiscountInfo());
                holder.discount_total.setText("$" + getDeciamlString(items.get(position).getDiscount()));
            } else if (getItemViewType(position) == TYPE_COUNT) {
                holder.viewitem_count_total_shippay.setText("$" + getDeciamlString(items.get(position).getShippingPay()));
                holder.viewitem_count_total_subtotal.setText("$" + getDeciamlString(items.get(position).getSubtotal()));
            } else if (getItemViewType(position) == TYPE_PAY) {
                if (footerItem != null) {
                    if (footerItem.getMdiscount() != 0)
                        holder.viewitem_count_coupon_mdiscount.setText((int) footerItem.getMdiscount() + "");
                    else
                        holder.viewitem_count_frame_mdiscount.setVisibility(View.GONE);
                    holder.viewitem_count_pay_opay.setText("$" + getDeciamlString(footerItem.getOpay()));
                    holder.viewitem_count_pay_pterms.setText(footerItem.getPterms());
                    if (footerItem.getXmoney() != 0) {
                        holder.viewitem_count_pay_xmoney.setText((int) footerItem.getXmoney() + "");
                        holder.viewitem_count_frame_xmoney.setVisibility(View.VISIBLE);
                    } else
                        holder.viewitem_count_frame_xmoney.setVisibility(View.GONE);
                    if (footerItem.getYmoney() != 0) {
                        holder.viewitem_count_pay_ymoney.setText((int) footerItem.getYmoney() + "");
                        holder.viewitem_count_frame_ymoney.setVisibility(View.VISIBLE);
                    } else
                        holder.viewitem_count_frame_ymoney.setVisibility(View.GONE);
                    if (footerItem.getEwallet() != 0) {
                        holder.viewitem_count_pay_ewallet.setText((int) footerItem.getEwallet() + "");
                        holder.viewitem_count_frame_ewallet.setVisibility(View.VISIBLE);
                    } else
                        holder.viewitem_count_frame_ewallet.setVisibility(View.GONE);
                    holder.viewitem_count_pay_rpay.setText("$" + getDeciamlString(footerItem.getRpay()) + "");
                    holder.viewitem_count_invoice_invoice.setText(invoiceType[footerItem.getInvoice()]);
                    holder.viewitem_count_invoice_ctitle.setText(footerItem.getCtitle() + "");
                    holder.viewitem_count_invoice_vat.setText(footerItem.getVat());
                }
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
        if (position == getItemCount() - 1)
            return TYPE_PAY;
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
        return items.size() + 1;

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
        ImageView viewitem_count_content_img;
        TextView viewitem_count_title_txt,
                viewitem_count_content_title, viewitem_count_content_color, viewitem_count_content_size, viewitem_count_content_total, viewitem_count_content_price,
                viewitem_count_shipways_style, viewitem_count_shipways_pay, viewitem_count_shipways_address, viewitem_count_shipways_name,
                viewitem_count_total_shippay, viewitem_count_total_subtotal,
                discount_title, discount_total,
                viewitem_count_coupon_mdiscount,
                viewitem_count_pay_opay, viewitem_count_pay_pterms, viewitem_count_pay_xmoney, viewitem_count_pay_ymoney, viewitem_count_pay_ewallet, viewitem_count_pay_rpay,
                viewitem_count_invoice_invoice, viewitem_count_invoice_ctitle, viewitem_count_invoice_vat;
        EditText viewitem_count_shipways_note;
        LinearLayout viewitem_count_shipways_goto, viewitem_count_linear;
        FrameLayout viewitem_count_frame_mdiscount, viewitem_count_frame_xmoney, viewitem_count_frame_ymoney, viewitem_count_frame_ewallet;
        int position;
        NumberPicker numberPicker;
        AlertDialog alertDialog;
        AlertDialog.Builder builder;

        public RecycleHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            if (view.getTag().equals("header")) {
                viewitem_count_title_txt = (TextView) view;
            } else if (view.getTag().equals("shipway")) {
                viewitem_count_shipways_goto = view.findViewById(R.id.viewitem_count_shipways_goto);
                viewitem_count_shipways_goto.setOnClickListener(this);
                viewitem_count_shipways_goto.setTag("goto");
                viewitem_count_shipways_style = view.findViewById(R.id.viewitem_count_shipways_style);
                viewitem_count_shipways_pay = view.findViewById(R.id.viewitem_count_shipways_pay);
                viewitem_count_shipways_address = view.findViewById(R.id.viewitem_count_shipways_address);
                viewitem_count_shipways_name = view.findViewById(R.id.viewitem_count_shipways_name);
                viewitem_count_shipways_note = view.findViewById(R.id.viewitem_count_shipways_note);
                viewitem_count_shipways_note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                   /*
                               InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(viewitem_count_shipways_note.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                     */
                                new ReCountJsonData().setStoreNote(count_type, token, items.get(position).getSno(), viewitem_count_shipways_note.getText() != null ? viewitem_count_shipways_note.getText().toString() : "");
                            }
                        }).start();
                    }
                });
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
            } else if (view.getTag().equals("pay")) {
                viewitem_count_frame_mdiscount = view.findViewById(R.id.viewitem_count_frame_mdiscount);
                viewitem_count_coupon_mdiscount = view.findViewById(R.id.viewitem_count_coupon_mdiscount);
                viewitem_count_pay_opay = view.findViewById(R.id.viewitem_count_pay_opay);
                viewitem_count_pay_pterms = view.findViewById(R.id.viewitem_count_pay_pterms);
                viewitem_count_linear = view.findViewById(R.id.viewitem_count_linear);
                viewitem_count_linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ctx, PayWayActivity.class);
                        intent.putExtra("count_type", count_type);
                        ctx.startActivity(intent);
                    }
                });
                viewitem_count_frame_xmoney = view.findViewById(R.id.viewitem_count_frame_xmoney);
                viewitem_count_frame_ymoney = view.findViewById(R.id.viewitem_count_frame_ymoney);
                viewitem_count_frame_ewallet = view.findViewById(R.id.viewitem_count_frame_ewallet);
                viewitem_count_pay_xmoney = view.findViewById(R.id.viewitem_count_pay_xmoney);
                viewitem_count_pay_ymoney = view.findViewById(R.id.viewitem_count_pay_ymoney);
                viewitem_count_pay_ewallet = view.findViewById(R.id.viewitem_count_pay_ewallet);
                viewitem_count_pay_rpay = view.findViewById(R.id.viewitem_count_pay_rpay);
                //===============================彈出視窗正解
                numberPicker = new NumberPicker(ctx);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(invoiceType.length - 1);
                numberPicker.setDisplayedValues(invoiceType);
                numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldone, int newone) {
                        viewitem_count_invoice_invoice.setText(invoiceType[newone]);
                        footerItem.setInvoice(newone);
                    }
                });
                builder = new AlertDialog.Builder(ctx);
                builder.setPositiveButton("完成", null);
                builder.setView(numberPicker);
                alertDialog = builder.create();  //创建对话框
                viewitem_count_invoice_invoice = view.findViewById(R.id.viewitem_count_invoice_invoice);
                viewitem_count_invoice_invoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewitem_count_invoice_invoice.setText(invoiceType[numberPicker.getValue()]);
                        alertDialog.show();
                    }
                });
                //===============================彈出視窗正解
                viewitem_count_invoice_ctitle = view.findViewById(R.id.viewitem_count_invoice_ctitle);
                viewitem_count_invoice_ctitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        footerItem.setCtitle(editable.toString());
                    }
                });
                viewitem_count_invoice_vat = view.findViewById(R.id.viewitem_count_invoice_vat);
                viewitem_count_invoice_vat.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        footerItem.setVat(editable.toString());
                    }
                });
            }
            itemView.setFocusableInTouchMode(true);
            itemView.setFocusable(true);
        }

        private int getHeaderPosition() {
            int index = 0;
            for (int i = items.size() - 1; i >= 0; i--) {
                if (items.get(i).getType() == TYPE_HEADER && getAdapterPosition() > i) {
                    index = i;
                    break;
                }
            }
            return index;
        }

        @Override
        public void onClick(final View view) {
            position = getHeaderPosition();

            switch (view.getTag() + "") {
                case "goto"://當shipway點擊時
                    Intent intent = new Intent(ctx, ShipWayActivity.class);
                    intent.putExtra("sno", items.get(position).getSno());
                    intent.putExtra("count_type", count_type);
                    ctx.startActivity(intent);
                    break;

            }
        }

    }

    public int getInvoice() {
        return footerItem.getInvoice();
    }

    public String getCtitle() {
        return footerItem.getCtitle();
    }

    public String getVat() {
        return footerItem.getVat();
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            title_list = AnalyzeShopCart.getCheckoutData(json);
            content_list = AnalyzeShopCart.getCheckoutItemArray(json);
            footer_coupon_list = AnalyzeShopCart.getCheckoutCoupon(json);
            footer_pay_list = AnalyzeShopCart.getCheckoutPay(json);
            footer_invoice_list = AnalyzeShopCart.getCheckoutInvoice(json);
        } else {
            title_list = new ArrayList<>();
            content_list = new ArrayList<>();
            footer_coupon_list = new ArrayList<>();
            footer_pay_list = new ArrayList<>();
            footer_invoice_list = new ArrayList<>();
        }
        initItems();
        initFooterItem();
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
        private String subtotal;
        private String discountInfo;
        private String discount;
        private String shippingInfo;

        private String shippingStyle;
        private String shippingName;
        private String shippingSname;
        private String shippingSid;
        private String shippingAddress;
        private String shippingPay;
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
        public Item(int type, String shippingInfo, String shippingStyle, String shippingName, String shippingSname, String shippingSid, String shippingAddress, String shippingPay, String note) {
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

        public String getSubtotal() {
            return subtotal;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setSubtotal(String subtotal) {
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

        public String getShippingPay() {
            return shippingPay;
        }

        public void setShippingPay(String shippingPay) {
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


    private class FooterItem {
        String moprno;
        String mcoupon;
        float mdiscount;
        String opay;
        String pterms;
        float xmoney;
        float ymoney;
        float ewallet;
        String rpay;
        int invoice;
        String ctitle;
        String vat;

        public FooterItem() {
        }

        public FooterItem(String moprno, String mcoupon, float mdiscount, String opay, String pterms, float xmoney, float ymoney, float ewallet, String rpay, int invoice, String ctitle, String vat) {
            this.moprno = moprno;
            this.mcoupon = mcoupon;
            this.mdiscount = mdiscount;
            this.opay = opay;
            this.pterms = pterms;
            this.xmoney = xmoney;
            this.ymoney = ymoney;
            this.ewallet = ewallet;
            this.rpay = rpay;
            this.invoice = invoice;
            this.ctitle = ctitle;
            this.vat = vat;
        }

        public String getMoprno() {
            return moprno;
        }

        public void setMoprno(String moprno) {
            this.moprno = moprno;
        }

        public String getMcoupon() {
            return mcoupon;
        }

        public void setMcoupon(String mcoupon) {
            this.mcoupon = mcoupon;
        }

        public float getMdiscount() {
            return mdiscount;
        }

        public void setMdiscount(float mdiscount) {
            this.mdiscount = mdiscount;
        }

        public String getOpay() {
            return opay;
        }

        public void setOpay(String opay) {
            this.opay = opay;
        }

        public String getPterms() {
            return pterms;
        }

        public void setPterms(String pterms) {
            this.pterms = pterms;
        }

        public float getXmoney() {
            return xmoney;
        }

        public void setXmoney(float xmoney) {
            this.xmoney = xmoney;
        }

        public float getYmoney() {
            return ymoney;
        }

        public void setYmoney(float ymoney) {
            this.ymoney = ymoney;
        }

        public float getEwallet() {
            return ewallet;
        }

        public void setEwallet(float ewallet) {
            this.ewallet = ewallet;
        }

        public String getRpay() {
            return rpay;
        }

        public void setRpay(String rpay) {
            this.rpay = rpay;
        }

        public int getInvoice() {
            return invoice;
        }

        public void setInvoice(int invoice) {
            this.invoice = invoice;
        }

        public String getCtitle() {
            return ctitle;
        }

        public void setCtitle(String ctitle) {
            this.ctitle = ctitle;
        }

        public String getVat() {
            return vat;
        }

        public void setVat(String vat) {
            this.vat = vat;
        }
    }

}
