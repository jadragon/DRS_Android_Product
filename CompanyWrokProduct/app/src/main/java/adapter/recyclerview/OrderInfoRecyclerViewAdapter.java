package adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.CommunityActivity;
import com.test.tw.wrokproduct.CountActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.AppreciateActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderInfoActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderInfoDetailActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderPayDetailActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.ReturnAndRefundActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.Item;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderFooterPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderHeaderPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Util.StringUtil;
import library.AnalyzeJSON.AnalyzeOrderInfo;
import library.Component.ToastMessageDialog;
import library.GetJsonData.HelpCenterJsonData;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.ReCountJsonData;
import library.JsonDataThread;

public class OrderInfoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_HEADER = 0;
    public final int TYPE_CONTENT = 1;
    public final int TYPE_INFO = 2;
    public final int TYPE_FOOTER = 3;
    Context ctx;
    List<Item> items;
    ArrayList<MemberOrderHeaderPojo> memberOrderHeaderPojoArrayList;
    ArrayList<ArrayList<MemberOrderContentPojo>> contentPojoArrayList;
    ArrayList<MemberOrderFooterPojo> memberOrderFooterPojoArrayList;
    int index;
    GlobalVariable gv;

    public OrderInfoRecyclerViewAdapter(Context ctx, JSONObject json, int index) {
        this.ctx = ctx;
        this.index = index;
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {
            memberOrderHeaderPojoArrayList = AnalyzeOrderInfo.getMemberOrderHeader(json);
            contentPojoArrayList = AnalyzeOrderInfo.getMemberOrderContent(json);
            memberOrderFooterPojoArrayList = AnalyzeOrderInfo.getMemberOrderFooter(json);
        } else {
            memberOrderHeaderPojoArrayList = new ArrayList<>();
            contentPojoArrayList = new ArrayList<>();
            memberOrderFooterPojoArrayList = new ArrayList<>();
        }

        //初始化checkbox
        initItems();
    }

    void initItems() {
        items = new ArrayList<>();
        Item item;
        for (int i = 0; i < memberOrderHeaderPojoArrayList.size(); i++) {
            item = memberOrderHeaderPojoArrayList.get(i);
            item.setType(TYPE_HEADER);
            items.add(item);
            //TYPE_CONTENT
            for (int j = 0; j < contentPojoArrayList.get(i).size(); j++) {
                item = contentPojoArrayList.get(i).get(j);
                item.setType(TYPE_CONTENT);
                items.add(item);
            }
            //TYPE_INFO
            item = memberOrderFooterPojoArrayList.get(i);
            item.setType(TYPE_INFO);
            items.add(item);

            //TYPE_FOOTER
            item = new Item();
            item.setType(TYPE_FOOTER);
            items.add(item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_header, parent, false);
            return new OrderInfoRecyclerViewAdapter.HeaderHolder(ctx, view);
        }
        if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_content, parent, false);
            return new OrderInfoRecyclerViewAdapter.ContentHolder(ctx, view);
        }
        if (viewType == TYPE_INFO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_listinfo, parent, false);
            return new OrderInfoRecyclerViewAdapter.InfoHolder(ctx, view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_footer, parent, false);
        return new OrderInfoRecyclerViewAdapter.FooterHolder(ctx, view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_HEADER) {
                ((HeaderHolder) holder).odate.setText(((MemberOrderHeaderPojo) items.get(position)).getOdate());
                ((HeaderHolder) holder).ordernum.setText(((MemberOrderHeaderPojo) items.get(position)).getOrdernum());
                ((HeaderHolder) holder).sname.setText(((MemberOrderHeaderPojo) items.get(position)).getSname());
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                ((ContentHolder) holder).pname.setText(((MemberOrderContentPojo) items.get(position)).getPname());
                ImageLoader.getInstance().displayImage(((MemberOrderContentPojo) items.get(position)).getPimg(), ((ContentHolder) holder).pimg);
                ((ContentHolder) holder).color.setText(((MemberOrderContentPojo) items.get(position)).getColor());
                ((ContentHolder) holder).size.setText(((MemberOrderContentPojo) items.get(position)).getSize());
                ((ContentHolder) holder).oiname.setText(((MemberOrderContentPojo) items.get(position)).getOiname());
                ((ContentHolder) holder).oiname.setTextColor(Color.parseColor(((MemberOrderContentPojo) items.get(position)).getOicolor()));
                ((ContentHolder) holder).price.setText("$" + StringUtil.getDeciamlString(((MemberOrderContentPojo) items.get(position)).getPrice()));
                ((ContentHolder) holder).price.setPaintFlags(((ContentHolder) holder).price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((ContentHolder) holder).sprice.setText("$" + StringUtil.getDeciamlString(((MemberOrderContentPojo) items.get(position)).getSprice()));
                ((ContentHolder) holder).stotal.setText("X" + ((MemberOrderContentPojo) items.get(position)).getStotal());

            } else if (getItemViewType(position) == TYPE_INFO) {
                ((InfoHolder) holder).ttotal.setText(((MemberOrderFooterPojo) items.get(position)).getTtotal() + "");
                ((InfoHolder) holder).tprice.setText("$" + StringUtil.getDeciamlString(((MemberOrderFooterPojo) items.get(position)).getTprice()));
                ((InfoHolder) holder).oname.setText(((MemberOrderFooterPojo) items.get(position)).getOname());
                ((InfoHolder) holder).pinfo.setText(((MemberOrderFooterPojo) items.get(position)).getPinfo());
                switch (index) {
                    case 0:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.GONE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.GONE);
                        break;
                    case 1:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.GONE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        break;
                    case 2:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.GONE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        break;
                    case 3:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.GONE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        break;
                    case 4:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.GONE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        break;
                    case 5:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.VISIBLE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        showPnameAndIname((InfoHolder) holder, position);
                        break;
                    case 6:
                        ((InfoHolder) holder).orderinfo_payinfo_layout.setVisibility(View.VISIBLE);
                        ((InfoHolder) holder).orderinfo_tradeprocess_layout.setVisibility(View.VISIBLE);
                        showPnameAndLpname((InfoHolder) holder, position);
                        showPnameAndIname((InfoHolder) holder, position);
                        break;
                }


            } else if (getItemViewType(position) == TYPE_FOOTER) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getOstatus() == 0) {
                            ((FooterHolder) holder).button2.setEnabled(true);
                            reShapeButton(((FooterHolder) holder).button2, R.color.red);
                        } else {
                            ((FooterHolder) holder).button2.setEnabled(false);
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                        }
                        break;
                    case 2:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getLstatus() == 2) {
                            ((FooterHolder) holder).button2.setEnabled(true);
                            reShapeButton(((FooterHolder) holder).button2, R.color.shipway_blue_light);
                        } else {
                            ((FooterHolder) holder).button2.setEnabled(false);
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                        }
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getPstatus() == 1) {
                            ((FooterHolder) holder).button3.setVisibility(View.VISIBLE);
                        } else {
                            ((FooterHolder) holder).button3.setVisibility(View.GONE);
                        }
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getLchk() == 1) {
                            reShapeButton(((FooterHolder) holder).button1, R.color.orange);
                            ((FooterHolder) holder).button1.setEnabled(true);
                        } else {
                            reShapeButton(((FooterHolder) holder).button1, R.color.gainsboro);
                            ((FooterHolder) holder).button1.setEnabled(false);
                        }
                        break;
                    case 3:


                        break;
                    case 4:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 0) {
                            ((FooterHolder) holder).button2.setVisibility(View.VISIBLE);
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                            ((FooterHolder) holder).button2.setText("等待賣家處理中");
                            ((FooterHolder) holder).button2.setEnabled(false);
                            ((FooterHolder) holder).button3.setVisibility(View.GONE);
                        } else if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 1) {
                            ((FooterHolder) holder).button2.setVisibility(View.VISIBLE);
                            reShapeButton(((FooterHolder) holder).button2, R.color.mediumpurple);
                            ((FooterHolder) holder).button2.setEnabled(true);
                            ((FooterHolder) holder).button2.setText("輸入物流編號");
                            ((FooterHolder) holder).button3.setVisibility(View.GONE);
                        } else {
                            ((FooterHolder) holder).button3.setVisibility(View.VISIBLE);
                            ((FooterHolder) holder).button2.setVisibility(View.GONE);
                        }
                        break;
                    case 5:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getCpchk() == 0) {
                            reShapeButton(((FooterHolder) holder).button2, R.color.gray);
                            ((FooterHolder) holder).button2.setEnabled(true);
                        } else {
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                            ((FooterHolder) holder).button2.setEnabled(false);
                        }
                        break;
                    case 6:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getCpchk() == 0) {
                            reShapeButton(((FooterHolder) holder).button2, R.color.gray);
                            ((FooterHolder) holder).button2.setEnabled(true);
                        } else {
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                            ((FooterHolder) holder).button2.setEnabled(false);
                        }
                        break;
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

    void showPnameAndLpname(InfoHolder infoHolder, int position) {
        infoHolder.pname.setText(((MemberOrderFooterPojo) items.get(position)).getPname());
        infoHolder.pname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getPcolor()));
        infoHolder.lpname.setText(((MemberOrderFooterPojo) items.get(position)).getLpname());
        infoHolder.lpname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getLcolor()));
    }

    void showPnameAndIname(InfoHolder infoHolder, int position) {
        infoHolder.iname.setText(((MemberOrderFooterPojo) items.get(position)).getIname());
        infoHolder.iname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getIcolor()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == TYPE_HEADER)
            return TYPE_HEADER;
        if (items.get(position).getType() == TYPE_CONTENT)
            return TYPE_CONTENT;
        if (items.get(position).getType() == TYPE_INFO)
            return TYPE_INFO;
        return TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView odate;
        TextView ordernum;
        TextView sname;

        public HeaderHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            odate = view.findViewById(R.id.orderinfo_header_odate);
            ordernum = view.findViewById(R.id.orderinfo_header_ordernum);
            sname = view.findViewById(R.id.orderinfo_header_sname);
        }

    }

    class ContentHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ImageView pimg;
        TextView pname, color, size, oiname, price, sprice, stotal;

        public ContentHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            pimg = view.findViewById(R.id.orderinfo_content_pimg);
            pname = view.findViewById(R.id.orderinfo_content_pname);
            color = view.findViewById(R.id.orderinfo_content_color);
            size = view.findViewById(R.id.orderinfo_content_size);
            oiname = view.findViewById(R.id.orderinfo_content_oiname);
            price = view.findViewById(R.id.orderinfo_content_price);
            sprice = view.findViewById(R.id.orderinfo_content_sprice);
            stotal = view.findViewById(R.id.orderinfo_content_stotal);
        }
    }

    class InfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context ctx;
        TextView ttotal, tprice, oname, pinfo;
        TextView pname, lpname, iname;
        FrameLayout oname_layout, pinfo_layout;
        LinearLayout orderinfo_payinfo_layout, orderinfo_tradeprocess_layout;

        public InfoHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            ttotal = view.findViewById(R.id.orderinfo_listinfo_ttotal);
            tprice = view.findViewById(R.id.orderinfo_listinfo_tprice);
            oname = view.findViewById(R.id.orderinfo_listinfo_oname);
            pinfo = view.findViewById(R.id.orderinfo_listinfo_pinfo);
            pname = view.findViewById(R.id.orderinfo_listinfo_pname);
            lpname = view.findViewById(R.id.orderinfo_listinfo_lpname);
            iname = view.findViewById(R.id.orderinfo_listinfo_iname);
            oname_layout = view.findViewById(R.id.orderinfo_listinfo_oname_layout);
            oname_layout.setOnClickListener(this);
            orderinfo_payinfo_layout = view.findViewById(R.id.orderinfo_payinfo_layout);
            orderinfo_tradeprocess_layout = view.findViewById(R.id.orderinfo_tradeprocess_layout);
            pinfo_layout = view.findViewById(R.id.orderinfo_listinfo_pinfo_layout);
            pinfo_layout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.orderinfo_listinfo_oname_layout:
                    intent = new Intent(ctx, OrderInfoDetailActivity.class);
                    intent.putExtra("token", gv.getToken());
                    intent.putExtra("mono", ((MemberOrderFooterPojo) items.get(getAdapterPosition())).getMono());
                    ctx.startActivity(intent);
                    break;
                case R.id.orderinfo_listinfo_pinfo_layout:
                    intent = new Intent(ctx, OrderPayDetailActivity.class);
                    intent.putExtra("mono", ((MemberOrderFooterPojo) items.get(getAdapterPosition())).getMono());
                    ctx.startActivity(intent);
                    break;
            }
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context ctx;
        Button button1, button2, button3;
        JSONObject jsonObject;
        ToastMessageDialog toastMessageDialog;

        public FooterHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            toastMessageDialog = new ToastMessageDialog(ctx);
            initFooterButton(view);
        }

        private void initFooterButton(View view) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(8);
            button1 = view.findViewById(R.id.orderinfo_footer_btn1);
            button2 = view.findViewById(R.id.orderinfo_footer_btn2);
            button3 = view.findViewById(R.id.orderinfo_footer_btn3);
            switch (index) {
                case 0:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.GONE);
                    button1.setText("取消訂單");
                    button2.setText("重新結帳");
                    reShapeButton(button1, R.color.gray);
                    reShapeButton(button2, R.color.red);
                    break;
                case 1:
                    button1.setVisibility(View.INVISIBLE);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.INVISIBLE);
                    button2.setText("申請取消");
                    break;
                case 2:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setOnClickListener(this);
                    button1.setText("延長收貨");
                    button2.setText("確認收貨");
                    button3.setText("申請取消");
                    reShapeButton(button3, R.color.purple);
                    break;
                case 3:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.GONE);
                    button1.setText("給予評價");
                    button2.setText("申請退換貨");
                    reShapeButton(button1, R.color.shipway_green_dark);
                    reShapeButton(button2, R.color.mediumpurple);
                    break;
                case 4:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setOnClickListener(this);
                    button1.setText("退貨單");
                    button2.setText("輸入物流編號");
                    button3.setText("查看物流編號");
                    reShapeButton(button1, R.color.colorPrimaryDark);
                    reShapeButton(button3, R.color.mediumpurple);
                    break;
                case 5:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.GONE);
                    button1.setText("給予評價");
                    button2.setText("投訴賣家");
                    reShapeButton(button1, R.color.shipway_green_dark);
                    reShapeButton(button2, R.color.gray);
                    break;
                case 6:
                    button1.setVisibility(View.INVISIBLE);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.INVISIBLE);
                    button2.setText("投訴賣家");
                    reShapeButton(button2, R.color.gray);
                    break;
            }
        }


        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition() - 1;
            Intent intent;
            switch (view.getId()) {
                case R.id.orderinfo_footer_btn1:
                    switch (index) {
                        case 0:
                            toastMessageDialog.setTitleText("取消訂單");
                            toastMessageDialog.showCheck(true, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().cancelMOrder(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(0, 6);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();

                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 1:

                            break;
                        case 2:
                            toastMessageDialog.setTitleText("延長收貨");
                            toastMessageDialog.setMessageText("");
                            toastMessageDialog.showCheck(false, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().extendReceipt(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono());
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(2);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 3:
                            intent = new Intent(ctx, AppreciateActivity.class);
                            intent.putExtra("token", gv.getToken());
                            intent.putExtra("type", "0");
                            intent.putExtra("mono", ((MemberOrderFooterPojo) (items.get(position))).getMono());
                            ctx.startActivity(intent);
                            break;
                        case 4:

                            break;
                        case 5:
                            intent = new Intent(ctx, AppreciateActivity.class);
                            intent.putExtra("token", gv.getToken());
                            intent.putExtra("type", "0");
                            intent.putExtra("mono", ((MemberOrderFooterPojo) (items.get(position))).getMono());
                            ctx.startActivity(intent);
                            break;
                        case 6:

                            break;
                    }
                    break;
                case R.id.orderinfo_footer_btn2:
                    switch (index) {
                        case 0:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final JSONObject jsonObject = new ReCountJsonData().goCheckout(ReCountJsonData.RECOUNT, gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono());
                                    new Handler(ctx.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (jsonObject.getBoolean("Success")) {
                                                    Intent intent;
                                                    intent = new Intent(ctx, CountActivity.class);
                                                    intent.putExtra("count_type", ReCountJsonData.RECOUNT);
                                                    ((AppCompatActivity) ctx).startActivityForResult(intent, 110);
                                                } else {
                                                    Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }).start();

                            break;
                        case 1:
                            toastMessageDialog.setTitleText("申請取消訂單");
                            toastMessageDialog.showCheck(true, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().applyCancel(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(1);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();

                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 2:
                            toastMessageDialog.setTitleText("確認收貨");
                            toastMessageDialog.showCheck(false, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().confirmReceipt(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono());
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(2, 3);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 3:
                            intent = new Intent(ctx, ReturnAndRefundActivity.class);
                            intent.putExtra("mono", ((MemberOrderFooterPojo) (items.get(position))).getMono());
                            ((AppCompatActivity) ctx).startActivityForResult(intent, 113);
                            break;
                        case 4:
                            toastMessageDialog.setTitleText("輸入物流編號");
                            toastMessageDialog.setOtherButtonText("了解更多");
                            toastMessageDialog.showOhter(true, new ToastMessageDialog.OtherClickListener() {
                                @Override
                                public void confirmClicked(Dialog dialog, View view, final String note) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new OrderInfoJsonData().applyReturnLnum(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((OrderInfoActivity) ctx).setFilterByIndex(4);
                                                }
                                                Toast.makeText(ctx, json.getString("Message"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    dialog.dismiss();
                                }

                                @Override
                                public void otherClicked(Dialog dialog, View view) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new HelpCenterJsonData().getCitem("ynvNAzmDHyZXuL3rIqktCw==");
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            Intent intent = new Intent(ctx, CommunityActivity.class);

                                            try {
                                                intent.putExtra("title", json.getJSONObject("Data").getString("title"));
                                                intent.putExtra("html", json.getJSONObject("Data").getString("content"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            ctx.startActivity(intent);
                                        }
                                    }.start();
                                }
                            });


                            break;
                        case 5:
                            toastMessageDialog.setTitleText("投訴賣家");
                            toastMessageDialog.showCheck(true, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().complaintStore(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(5);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 6:
                            toastMessageDialog.setTitleText("投訴賣家");
                            toastMessageDialog.showCheck(true, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().complaintStore(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(6);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }

                            });
                            break;

                    }
                    break;
                case R.id.orderinfo_footer_btn3:
                    switch (index) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            toastMessageDialog.setTitleText("申請取消訂單");
                            toastMessageDialog.showCheck(true, new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonObject = new OrderInfoJsonData().applyCancel(gv.getToken(), ((MemberOrderFooterPojo) (items.get(position))).getMono(), note);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        if (jsonObject.getBoolean("Success")) {
                                                            ((OrderInfoActivity) ctx).setFilterByIndex(2);
                                                        }
                                                        Toast.makeText(ctx, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();

                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 3:
                            break;
                        case 4:
                            toastMessageDialog.setTitleText("查看物流編號");
                            toastMessageDialog.setMessageText(((MemberOrderFooterPojo) items.get(position)).getAlnum());
                            toastMessageDialog.confirm();
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                    }

                    break;
            }

        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            memberOrderHeaderPojoArrayList = AnalyzeOrderInfo.getMemberOrderHeader(json);
            contentPojoArrayList = AnalyzeOrderInfo.getMemberOrderContent(json);
            memberOrderFooterPojoArrayList = AnalyzeOrderInfo.getMemberOrderFooter(json);
        } else {
            memberOrderHeaderPojoArrayList = new ArrayList<>();
            contentPojoArrayList = new ArrayList<>();
            memberOrderFooterPojoArrayList = new ArrayList<>();
        }
        initItems();
        notifyDataSetChanged();
    }

    public boolean setFilterMore(JSONObject json) {
        int presize = items.size();
        if (json != null && AnalyzeOrderInfo.getMemberOrderHeader(json).size() > 0) {
            memberOrderHeaderPojoArrayList.addAll(AnalyzeOrderInfo.getMemberOrderHeader(json));
            contentPojoArrayList.addAll(AnalyzeOrderInfo.getMemberOrderContent(json));
            memberOrderFooterPojoArrayList.addAll(AnalyzeOrderInfo.getMemberOrderFooter(json));
            initItems();
            notifyItemInserted(presize + 1);
            //notifyItemChanged(presize + 1, items.size()+1);
            return true;
        } else {
            new ToastMessageDialog(ctx, "沒有更多了").show();
            return false;
        }

    }

    private void reShapeButton(Button button, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setColor(ctx.getResources().getColor(color));
        button.setBackground(shape);
    }

}
