package adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.商家管理.商品訂單.ProductOrderActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.AppreciateActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderInfoDetailActivity;
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
import library.GetJsonData.StoreJsonData;
import library.JsonDataThread;

public class ProductOrderRecyclerViewAdapter extends OrderInfoRecyclerViewAdapter {
    String token;

    public ProductOrderRecyclerViewAdapter(Context ctx, JSONObject json, int index) {
        super(ctx, json, index);
    }

    public void setToken(String token) {
        this.token = token;
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
            return new InfoHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_footer, parent, false);
        return new FooterHolder(view);

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
                showPnameAndLpnameAndIname((InfoHolder) holder, position);
            } else if (getItemViewType(position) == TYPE_FOOTER) {

                switch (index) {

                    case 0:

                        break;
                    case 1:
                        if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 0) {
                            ((FooterHolder) holder).button1.setText("不同意退換貨");
                            ((FooterHolder) holder).button2.setText("同意退換貨");
                            ((FooterHolder) holder).button1.setVisibility(View.VISIBLE);
                            ((FooterHolder) holder).button1.setEnabled(true);
                            ((FooterHolder) holder).button2.setEnabled(true);
                            reShapeButton(((FooterHolder) holder).button1, R.color.gray);
                            reShapeButton(((FooterHolder) holder).button2, R.color.red);
                        } else if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 1) {
                            ((FooterHolder) holder).button1.setText("確認收件");
                            ((FooterHolder) holder).button2.setText("查看物流編號");
                            ((FooterHolder) holder).button1.setVisibility(View.VISIBLE);
                            ((FooterHolder) holder).button1.setEnabled(false);
                            ((FooterHolder) holder).button2.setEnabled(false);
                            reShapeButton(((FooterHolder) holder).button1, R.color.gainsboro);
                            reShapeButton(((FooterHolder) holder).button2, R.color.gainsboro);
                        } else if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 2) {
                            ((FooterHolder) holder).button1.setText("確認收件");
                            ((FooterHolder) holder).button2.setText("查看物流編號");
                            ((FooterHolder) holder).button1.setVisibility(View.VISIBLE);
                            ((FooterHolder) holder).button1.setEnabled(true);
                            ((FooterHolder) holder).button2.setEnabled(true);
                            reShapeButton(((FooterHolder) holder).button1, R.color.shipway_blue_light);
                            reShapeButton(((FooterHolder) holder).button2, R.color.mediumpurple);
                        } else if (((MemberOrderFooterPojo) items.get(position - 1)).getAlchk() == 3) {
                            ((FooterHolder) holder).button2.setText("查看物流編號");
                            ((FooterHolder) holder).button1.setVisibility(View.GONE);
                            ((FooterHolder) holder).button2.setEnabled(true);
                            reShapeButton(((FooterHolder) holder).button2, R.color.mediumpurple);
                        }
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:


                        break;
                    case 5:

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

                    case 7:
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

    void showPnameAndLpnameAndIname(InfoHolder infoHolder, int position) {
        infoHolder.pname.setText(((MemberOrderFooterPojo) items.get(position)).getPname());
        infoHolder.pname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getPcolor()));
        infoHolder.lpname.setText(((MemberOrderFooterPojo) items.get(position)).getLpname());
        infoHolder.lpname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getLcolor()));
        infoHolder.iname.setText(((MemberOrderFooterPojo) items.get(position)).getIname());
        infoHolder.iname.setTextColor(Color.parseColor(((MemberOrderFooterPojo) items.get(position)).getIcolor()));
    }


    class InfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ttotal, tprice, oname, pinfo;
        TextView pname, lpname, iname;
        FrameLayout oname_layout, pinfo_layout, orderinfo_payinfo_layout;
        LinearLayout orderinfo_tradeprocess_layout;

        public InfoHolder(View view) {
            super(view);
            ttotal = view.findViewById(R.id.orderinfo_listinfo_ttotal);
            tprice = view.findViewById(R.id.orderinfo_listinfo_tprice);
            oname = view.findViewById(R.id.orderinfo_listinfo_oname);
            pinfo = view.findViewById(R.id.orderinfo_listinfo_pinfo);
            pname = view.findViewById(R.id.orderinfo_listinfo_pname2);
            lpname = view.findViewById(R.id.orderinfo_listinfo_lpname);
            iname = view.findViewById(R.id.orderinfo_listinfo_iname);
            oname_layout = view.findViewById(R.id.orderinfo_listinfo_oname_layout);
            oname_layout.setOnClickListener(this);
            orderinfo_payinfo_layout = view.findViewById(R.id.orderinfo_logistics_layout2);
            orderinfo_tradeprocess_layout = view.findViewById(R.id.orderinfo_tradeprocess_layout);
            View orderinfo_listinfo_pinfo_arrow = view.findViewById(R.id.orderinfo_listinfo_pinfo_arrow);
            orderinfo_listinfo_pinfo_arrow.setVisibility(View.INVISIBLE);
            pinfo_layout = view.findViewById(R.id.orderinfo_listinfo_pinfo_layout);
            if (index > 4) {
                orderinfo_tradeprocess_layout.setVisibility(View.VISIBLE);
            } else {
                orderinfo_tradeprocess_layout.setVisibility(View.GONE);
            }
            orderinfo_payinfo_layout.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.orderinfo_listinfo_oname_layout:
                    intent = new Intent(ctx, OrderInfoDetailActivity.class);
                    intent.putExtra("mono", ((MemberOrderFooterPojo) items.get(getAdapterPosition())).getMono());
                    intent.putExtra("token", token);
                    ctx.startActivity(intent);
                    break;
            }
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button button1, button2, button3;
        ToastMessageDialog toastMessageDialog;

        public FooterHolder(View view) {
            super(view);
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
                    button1.setText("不同意取消");
                    button2.setText("同意取消");
                    reShapeButton(button1, R.color.gray);
                    reShapeButton(button2, R.color.red);
                    break;
                case 1:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setOnClickListener(this);
                    button1.setText("確認收件");
                    button2.setText("查看物流編號");
                    button3.setText("退貨單");
                    reShapeButton(button1, R.color.gray);
                    reShapeButton(button2, R.color.red);
                    reShapeButton(button3, R.color.colorPrimaryDark);
                    break;
                case 2:
                    button1.setVisibility(View.INVISIBLE);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.INVISIBLE);
                    button2.setText("備貨完成");
                    reShapeButton(button2, R.color.shipway_blue_light);
                    break;
                case 3:
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
                case 4:
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
                case 5:
                    button1.setVisibility(View.INVISIBLE);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.INVISIBLE);
                    button2.setText("給買家評價");
                    reShapeButton(button2, R.color.shipway_green_dark);
                    break;
                case 6:
                    button1.setOnClickListener(this);
                    button2.setOnClickListener(this);
                    button3.setVisibility(View.GONE);
                    button1.setText("給買家評價");
                    button2.setText("投訴賣家");
                    reShapeButton(button1, R.color.shipway_green_dark);
                    reShapeButton(button2, R.color.gray);
                    break;
                case 7:
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
                            toastMessageDialog.setTitleText("不同意取消");
                            toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new StoreJsonData().applyCancel(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), 1, note);
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((ProductOrderActivity) ctx).setFilterByIndex(0, 2, 3);
                                                } else {
                                                    Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 1:
                            if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 0) {
                                toastMessageDialog.setTitleText("不同意退換貨");
                                toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                    @Override
                                    public void ItemClicked(Dialog dialog, View view, final String note) {
                                        new JsonDataThread() {
                                            @Override
                                            public JSONObject getJsonData() {
                                                return new StoreJsonData().applyReturn(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), 1, note);
                                            }

                                            @Override
                                            public void runUiThread(JSONObject json) {
                                                try {
                                                    if (json.getBoolean("Success")) {
                                                        ((ProductOrderActivity) ctx).setFilterByIndex(1, 6);
                                                    } else {
                                                        Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        dialog.dismiss();
                                    }
                                });
                            } else if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 2) {
                                toastMessageDialog.setTitleText("確認收件");
                                toastMessageDialog.setCheckListener(new ToastMessageDialog.CheckListener() {
                                    @Override
                                    public void ItemClicked(Dialog dialog, View view) {
                                        new JsonDataThread() {
                                            @Override
                                            public JSONObject getJsonData() {
                                                return new StoreJsonData().confirmReceipt(token, ((MemberOrderFooterPojo) items.get(position)).getMono());
                                            }

                                            @Override
                                            public void runUiThread(JSONObject json) {
                                                try {
                                                    if (json.getBoolean("Success")) {
                                                        ((ProductOrderActivity) ctx).setFilterByIndex(1);
                                                    } else {
                                                        Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        dialog.dismiss();
                                    }
                                });
                                toastMessageDialog.check();
                            } else if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 3) {

                            }
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            intent = new Intent(ctx, AppreciateActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("type", "1");
                            intent.putExtra("mono", ((MemberOrderFooterPojo) (items.get(position))).getMono());
                            ctx.startActivity(intent);
                            break;
                        case 7:
                            break;
                    }
                    break;
                case R.id.orderinfo_footer_btn2:
                    switch (index) {
                        case 0:
                            toastMessageDialog.setTitleText("同意取消");
                            toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new StoreJsonData().applyCancel(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), 0, note);
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((ProductOrderActivity) ctx).setFilterByIndex(0, 7);
                                                } else {
                                                    Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 1:
                            if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 0) {
                                toastMessageDialog.setTitleText("同意退換貨");
                                toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                    @Override
                                    public void ItemClicked(Dialog dialog, View view, final String note) {
                                        new JsonDataThread() {
                                            @Override
                                            public JSONObject getJsonData() {
                                                return new StoreJsonData().applyReturn(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), 0, note);
                                            }

                                            @Override
                                            public void runUiThread(JSONObject json) {
                                                try {
                                                    if (json.getBoolean("Success")) {
                                                        ((ProductOrderActivity) ctx).setFilterByIndex(1);
                                                    } else {
                                                        Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        dialog.dismiss();
                                    }
                                });
                            } else if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 2) {
                                toastMessageDialog.setTitleText("查看物流編號");
                                toastMessageDialog.setMessageText(((MemberOrderFooterPojo) items.get(position)).getAlnum());
                                toastMessageDialog.confirm();
                            } else if (((MemberOrderFooterPojo) items.get(position)).getAlchk() == 3) {
                                toastMessageDialog.setTitleText("查看物流編號");
                                toastMessageDialog.setMessageText(((MemberOrderFooterPojo) items.get(position)).getAlnum());
                                toastMessageDialog.confirm();
                            }
                            break;
                        case 2:
                            toastMessageDialog.setTitleText("備貨完成");
                            toastMessageDialog.setCheckListener(new ToastMessageDialog.CheckListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new StoreJsonData().stockingCompleted(token, ((MemberOrderFooterPojo) items.get(position)).getMono());
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((ProductOrderActivity) ctx).setFilterByIndex(2, 3);
                                                } else {
                                                    Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    dialog.dismiss();
                                }
                            });
                            toastMessageDialog.check();
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            intent = new Intent(ctx, AppreciateActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("type", "1");
                            intent.putExtra("mono", ((MemberOrderFooterPojo) (items.get(position))).getMono());
                            ctx.startActivity(intent);
                            break;
                        case 6:
                            toastMessageDialog.setTitleText("投訴賣家");
                            toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new StoreJsonData().complaintMember(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), note);
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((ProductOrderActivity) ctx).setFilterByIndex(6);
                                                } else {
                                                    Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case 7:
                            toastMessageDialog.setTitleText("投訴賣家");
                            toastMessageDialog.choice(new ToastMessageDialog.ClickListener() {
                                @Override
                                public void ItemClicked(Dialog dialog, View view, final String note) {
                                    new JsonDataThread() {
                                        @Override
                                        public JSONObject getJsonData() {
                                            return new StoreJsonData().complaintMember(token, ((MemberOrderFooterPojo) items.get(position)).getMono(), note);
                                        }

                                        @Override
                                        public void runUiThread(JSONObject json) {
                                            try {
                                                if (json.getBoolean("Success")) {
                                                    ((ProductOrderActivity) ctx).setFilterByIndex(7);
                                                } else {
                                                    Toast.makeText(ctx, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
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
                            toastMessageDialog.setTitleText("退貨單");
                            toastMessageDialog.confirm();
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
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
