package adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.FooterPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.HeaderPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.Item;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import library.AnalyzeJSON.AnalyzeOrderInfo;

public class OrderInfoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_INFO = 2;
    public static final int TYPE_FOOTER = 3;
    private Context ctx;
    private List<Item> items;
    ArrayList<HeaderPojo> headerPojoArrayList;
    ArrayList<ArrayList<ContentPojo>> contentPojoArrayList;
    ArrayList<FooterPojo> footerPojoArrayList;
    String token;
    int index;

    public OrderInfoRecyclerViewAdapter(Context ctx, JSONObject json, int index) {
        this.ctx = ctx;
        this.index = index;
        token = ((GlobalVariable) ctx.getApplicationContext()).getToken();
        if (json != null) {
            headerPojoArrayList = AnalyzeOrderInfo.getMemberOrderHeader(json);
            contentPojoArrayList = AnalyzeOrderInfo.getMemberOrderContent(json);
            footerPojoArrayList = AnalyzeOrderInfo.getMemberOrderFooter(json);
        } else {
            headerPojoArrayList = new ArrayList<>();
            contentPojoArrayList = new ArrayList<>();
            footerPojoArrayList = new ArrayList<>();
        }

        //初始化checkbox
        initItems();
    }


    public void initItems() {
        items = new ArrayList<>();
        Item item;
        for (int i = 0; i < headerPojoArrayList.size(); i++) {
            item = headerPojoArrayList.get(i);
            item.setType(TYPE_HEADER);
            items.add(item);
            //TYPE_CONTENT
            for (int j = 0; j < contentPojoArrayList.get(i).size(); j++) {
                item = contentPojoArrayList.get(i).get(j);
                item.setType(TYPE_CONTENT);
                items.add(item);
            }
            //TYPE_INFO
            item = footerPojoArrayList.get(i);
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
                ((HeaderHolder) holder).odate.setText(((HeaderPojo) items.get(position)).getOdate());
                ((HeaderHolder) holder).ordernum.setText(((HeaderPojo) items.get(position)).getOrdernum());
                ((HeaderHolder) holder).sname.setText(((HeaderPojo) items.get(position)).getSname());
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                ((ContentHolder) holder).pname.setText(((ContentPojo) items.get(position)).getPname());
                ImageLoader.getInstance().displayImage(((ContentPojo) items.get(position)).getPimg(), ((ContentHolder) holder).pimg);
                ((ContentHolder) holder).color.setText(((ContentPojo) items.get(position)).getColor());
                ((ContentHolder) holder).size.setText(((ContentPojo) items.get(position)).getSize());
                ((ContentHolder) holder).oiname.setText(((ContentPojo) items.get(position)).getOiname());
                ((ContentHolder) holder).oiname.setTextColor(Color.parseColor(((ContentPojo) items.get(position)).getOicolor()));
                ((ContentHolder) holder).price.setText("$" + ((ContentPojo) items.get(position)).getPrice());
                ((ContentHolder) holder).sprice.setText("$" + ((ContentPojo) items.get(position)).getSprice());
                ((ContentHolder) holder).stotal.setText("X" + ((ContentPojo) items.get(position)).getStotal());

            } else if (getItemViewType(position) == TYPE_INFO) {
                ((InfoHolder) holder).ttotal.setText(((FooterPojo) items.get(position)).getTtotal() + "");
                ((InfoHolder) holder).tprice.setText("$" + ((FooterPojo) items.get(position)).getTprice());
                ((InfoHolder) holder).oname.setText(((FooterPojo) items.get(position)).getOname());

                ((InfoHolder) holder).pinfo.setText(((FooterPojo) items.get(position)).getPinfo());

                ((InfoHolder) holder).pname.setText(((FooterPojo) items.get(position)).getPname());
                ((InfoHolder) holder).pname.setTextColor(Color.parseColor(((FooterPojo) items.get(position)).getPcolor()));
                ((InfoHolder) holder).lpname.setText(((FooterPojo) items.get(position)).getLpname());
                ((InfoHolder) holder).lpname.setTextColor(Color.parseColor(((FooterPojo) items.get(position)).getLcolor()));
                ((InfoHolder) holder).iname.setText(((FooterPojo) items.get(position)).getIname());
                ((InfoHolder) holder).iname.setTextColor(Color.parseColor(((FooterPojo) items.get(position)).getIcolor()));
            } else if (getItemViewType(position) == TYPE_FOOTER) {
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

    private String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
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

    class InfoHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView ttotal, tprice, oname, pinfo;
        TextView pname, lpname, iname;

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
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context ctx;
        Button button1, button2;

        public FooterHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            initFooterButton(view);
        }

        private void initFooterButton(View view) {
            switch (index) {
                case 0:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setOnClickListener(this);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setOnClickListener(this);
                    button1.setText("取消訂單");
                    button2.setText("重新結帳");
                    button1.setBackgroundColor(ctx.getResources().getColor(R.color.gray));
                    button2.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                    break;
                case 1:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setOnClickListener(this);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setVisibility(View.INVISIBLE);
                    button1.setText("申請取消");
                    button1.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                    break;
                case 2:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setOnClickListener(this);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setOnClickListener(this);
                    button1.setText("延長收貨");
                    button2.setText("確認收貨");
                    button1.setBackgroundColor(ctx.getResources().getColor(R.color.orange));
                    button2.setBackgroundColor(ctx.getResources().getColor(R.color.shipway_blue_light));
                    break;
                case 3:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setOnClickListener(this);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setOnClickListener(this);
                    button1.setText("申請退換貨");
                    button2.setText("給予評價");
                    button1.setBackgroundColor(ctx.getResources().getColor(R.color.shipway_green_dark));
                    button2.setBackgroundColor(ctx.getResources().getColor(R.color.mediumpurple));
                    break;
                case 4:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setVisibility(View.INVISIBLE);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setOnClickListener(this);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setOnClickListener(this);
                    button1.setText("給予評價");
                    button2.setText("投訴賣家");
                    button1.setBackgroundColor(ctx.getResources().getColor(R.color.shipway_green_dark));
                    button2.setBackgroundColor(ctx.getResources().getColor(R.color.gray));
                    break;
                case 6:
                    button1 = view.findViewById(R.id.orderinfo_footer_btn1);
                    button1.setVisibility(View.INVISIBLE);
                    button2 = view.findViewById(R.id.orderinfo_footer_btn2);
                    button2.setOnClickListener(this);
                    button2.setText("投訴賣家");
                    button2.setBackgroundColor(ctx.getResources().getColor(R.color.gray));
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.orderinfo_footer_btn1:
                    break;
                case R.id.orderinfo_footer_btn2:
                    break;
            }

        }
    }

    public void setFilter(JSONObject json) {
        initItems();
        notifyDataSetChanged();
    }


}
