package adapter.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.AddDeliveryShipWayActivity;
import com.test.tw.wrokproduct.AddStoreShipWayActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ShopCartJsonData;

public class ShowShipWayRecyclerViewAdapter extends RecyclerView.Adapter<ShowShipWayRecyclerViewAdapter.RecycleHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_FOOTER = 3;
    private Context ctx;
    private JSONObject json;
    private DisplayMetrics dm;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    ArrayList<Map<String, String>> title_list;
    ArrayList<ArrayList<Map<String, String>>> items_list;
    private List<HeaderPojo> datas;
    String[] lanes = {"無", "本島", "離島", "海外"};
    int[] colors = {R.color.shipway_yellow, R.color.shipway_green_dark, R.color.shipway_blue_light, R.color.shipway_pink, R.color.shipway_orange_light, R.color.shipway_green_light, R.color.shipway_orange_dark, R.color.shipway_blue_dark};
    String token;
    String sno, plno, mino, sname;

    public ShowShipWayRecyclerViewAdapter(Context ctx, JSONObject json, String token) {
        this(ctx, json);
        this.token = token;

    }

    public ShowShipWayRecyclerViewAdapter(Context ctx, JSONObject json) {
        dm = ctx.getResources().getDisplayMetrics();
        this.ctx = ctx;
        this.json = json;
        GlobalVariable gv = (GlobalVariable) ctx.getApplicationContext();
        token = gv.getToken();
        if (json != null) {
            if (AnalyzeShopCart.getStoreLogisticsData(json) != null) {
                title_list = AnalyzeShopCart.getStoreLogisticsData(json);
                items_list = AnalyzeShopCart.getmyLogisticsArray(json);
            } else {
                title_list = new ArrayList<>();
                items_list = new ArrayList<>();
            }
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }
        initData();
    }

    @Override
    public ShowShipWayRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vitem_shipwaylist, parent, false);
        //頁面
        view.setTag(viewType);
        return new ShowShipWayRecyclerViewAdapter.RecycleHolder(parent, view);

    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            holder.vitem_shipwaylist_logisticsVal.setText(datas.get(position).getLogisticsVal());
            holder.vitem_shipwaylist_logisticsVal.setBackgroundColor(ctx.getResources().getColor(colors[datas.get(position).getLogistics()]));
            holder.vitem_shipwaylist_lpay.setText("$" + datas.get(position).getLpay());
            holder.vitem_shipwaylist_land.setText(lanes[datas.get(position).getLand()]);
            holder.vitem_shipwaylist_sname.setText(datas.get(position).getSname());
            initItemView(holder, holder.parent, position);
            //判斷當當前使用位置未被設定時
            if (datas.get(position).getIsused() && currentItem == -1) {
                currentItem = position;
            }
            if (currentItem == position) {
                holder.vitem_shipwaylist_nike.setVisibility(View.VISIBLE);
                holder.hideArea.setVisibility(View.VISIBLE);
            } else {
                holder.vitem_shipwaylist_nike.setVisibility(View.INVISIBLE);
                holder.hideArea.setVisibility(View.GONE);
            }
            for (int i = 0; i < holder.item_images.size(); i++) {
                if (datas.get(position).getMyLogisticsArray().get(i).getIsused()) {
                    holder.item_images.get(i).setSelected(true);
                } else {
                    holder.item_images.get(i).setSelected(false);
                }

                holder.item_names.get(i).setText(datas.get(position).getMyLogisticsArray().get(i).getName());
                holder.item_snames.get(i).setText(datas.get(position).getMyLogisticsArray().get(i).getSname());
                holder.item_addresses.get(i).setText(datas.get(position).getMyLogisticsArray().get(i).getAddress());
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

        return position;
    }

    @Override
    public int getItemCount() {
        return datas.size();

    }

    private String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout showArea;
        private TextView vitem_shipwaylist_logisticsVal;
        private TextView vitem_shipwaylist_lpay;
        private TextView vitem_shipwaylist_land;
        private TextView vitem_shipwaylist_sname;
        private ImageView vitem_shipwaylist_nike;
        private LinearLayout hideArea;
        private List<ImageView> item_images;
        private List<TextView> item_names;
        private List<TextView> item_snames;
        private List<TextView> item_addresses;
        private List<LinearLayout> item_layouts;
        private View footer;
        private ViewGroup parent;

        public RecycleHolder(ViewGroup parent, View view) {
            super(view);
            this.parent = parent;
            showArea = view.findViewById(R.id.layout_showArea);
            showArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //用 currentItem 记录点击位置
                    if (getAdapterPosition() != currentItem) { //再次点击
                        currentItem = getAdapterPosition();
                    }
                    //通知adapter数据改变需要重新加载
                    notifyDataSetChanged(); //必须有的一步
                }
            });
            vitem_shipwaylist_logisticsVal = showArea.findViewById(R.id.vitem_shipwaylist_logisticsVal);
            vitem_shipwaylist_lpay = showArea.findViewById(R.id.vitem_shipwaylist_lpay);
            vitem_shipwaylist_land = showArea.findViewById(R.id.vitem_shipwaylist_land);
            vitem_shipwaylist_sname = showArea.findViewById(R.id.vitem_shipwaylist_sname);
            vitem_shipwaylist_nike = showArea.findViewById(R.id.vitem_shipwaylist_nike);
            hideArea = view.findViewById(R.id.layout_hideArea);
            hideArea.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            hideArea.setDividerDrawable(ctx.getResources().getDrawable(R.drawable.decoration_line));
        }

        @Override
        public void onClick(final View view) {

        }

    }

    public void setFilter(JSONObject newjson) {
        this.json = newjson;
        if (json != null) {
            if (AnalyzeShopCart.getStoreLogisticsData(json) != null) {
                title_list = AnalyzeShopCart.getStoreLogisticsData(json);
                items_list = AnalyzeShopCart.getmyLogisticsArray(json);
            } else {
                title_list = new ArrayList<>();
                items_list = new ArrayList<>();
            }
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }
        initData();
        notifyDataSetChanged();
    }

    public void setFilterAfterAdd(JSONObject newjson) {
        this.json = newjson;
        if (json != null) {
            if (AnalyzeShopCart.getStoreLogisticsData(json) != null) {
                title_list = AnalyzeShopCart.getStoreLogisticsData(json);
                items_list = AnalyzeShopCart.getmyLogisticsArray(json);
            } else {
                title_list = new ArrayList<>();
                items_list = new ArrayList<>();
            }
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }
        initData();
        //新增後做登陸動作
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getIsused() && (datas.get(i).getMyLogisticsArray().get(datas.get(i).getMyLogisticsArray().size() - 1).getIsused())) {
                sno = datas.get(i).getSno();
                plno = datas.get(i).getPlno();
                mino = datas.get(i).getMyLogisticsArray().get(datas.get(i).getMyLogisticsArray().size() - 1).getMlno();
                sname = datas.get(i).getMyLogisticsArray().get(datas.get(i).getMyLogisticsArray().size() - 1).getSname();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ShopCartJsonData().setStoreMemberLogistics(token, sno, plno, mino);
                        json = new ShopCartJsonData().getStoreLogistics(token, sno);
                    }
                }).start();
                break;
            }
        }
        currentItem = -1;
        notifyDataSetChanged();
    }

    private void initItemView(final RecycleHolder holder, ViewGroup parent, final int position) {
        holder.hideArea.removeAllViews();
        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;
        View view;
        //item
        holder.item_images = new ArrayList<>();
        holder.item_layouts = new ArrayList<>();
        holder.item_names = new ArrayList<>();
        holder.item_snames = new ArrayList<>();
        holder.item_addresses = new ArrayList<>();
        for (int i = 0; i < items_list.get(position).size(); i++) {
            view = LayoutInflater.from(ctx).inflate(R.layout.viewitem_shipwaylist_item, parent, false);
            linearLayout = view.findViewById(R.id.shipwaylist_item_layout);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels / 7));
            linearLayout.setTag(position + "," + i);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = view.getTag().toString();
                    int header_position = Integer.parseInt(tag.split(",")[0]);
                    int position = Integer.parseInt(tag.split(",")[1]);
                    sno = datas.get(header_position).getSno();
                    plno = datas.get(header_position).getPlno();
                    mino = datas.get(header_position).getMyLogisticsArray().get(position).getMlno();
                    sname = datas.get(header_position).getMyLogisticsArray().get(position).getSname();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new ShopCartJsonData().setStoreMemberLogistics(token, sno, plno, mino);
                            json = new ShopCartJsonData().getStoreLogistics(token, sno);
                            new Handler(ctx.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.vitem_shipwaylist_sname.setText(sname);
                                    setFilter(json);
                                }
                            });
                        }
                    }).start();
                }
            });
            holder.item_layouts.add(linearLayout);
            imageView = view.findViewById(R.id.shipwaylist_item_btn);
            holder.item_images.add(imageView);
            textView = view.findViewById(R.id.shipwaylist_item_name);
            holder.item_names.add(textView);
            textView = view.findViewById(R.id.shipwaylist_item_sname);
            holder.item_snames.add(textView);
            textView = view.findViewById(R.id.shipwaylist_item_address);
            holder.item_addresses.add(textView);
            holder.hideArea.addView(view);

        }
        //addfooter
        linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * dm.density)));
        textView = new TextView(ctx);
        textView.setText("新增" + datas.get(position).getLogisticsVal());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        linearLayout.addView(textView);
        //設定背景
        TypedValue outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        linearLayout.setBackgroundResource(outValue.resourceId);
        holder.footer = linearLayout;
        holder.footer.setTag(position);
        //新增運送方式
        holder.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (datas.get(position).getLogistics() <= 3) {
                    intent = new Intent(ctx, AddStoreShipWayActivity.class);
                } else {
                    intent = new Intent(ctx, AddDeliveryShipWayActivity.class);
                }
                intent.putExtra("sno", title_list.get(position).get("sno"));
                intent.putExtra("plno", title_list.get(position).get("plno"));
                intent.putExtra("type", title_list.get(position).get("type"));
                intent.putExtra("land", title_list.get(position).get("land"));
                intent.putExtra("logistics", title_list.get(position).get("logistics"));
                ((Activity) ctx).startActivityForResult(intent, 0);
            }
        });
        holder.hideArea.addView(linearLayout);
    }

    public void initData() {
        datas = new ArrayList<>();
        HeaderPojo headerPojo;
        List<ItemPojo> itemPojoList;
        ItemPojo itemPojo;
        for (int i = 0; i < title_list.size(); i++) {
            headerPojo = new HeaderPojo();
            headerPojo.setSno(title_list.get(i).get("sno"));
            headerPojo.setSlno(title_list.get(i).get("slno"));
            headerPojo.setPlno(title_list.get(i).get("plno"));
            headerPojo.setType(title_list.get(i).get("type"));
            headerPojo.setLand(Integer.parseInt(title_list.get(i).get("land")));
            headerPojo.setLogistics(Integer.parseInt(title_list.get(i).get("logistics")));
            headerPojo.setLogisticsVal(title_list.get(i).get("logisticsVal"));
            headerPojo.setLpay(title_list.get(i).get("lpay"));
            headerPojo.setIsused(title_list.get(i).get("isused").equals("1"));
            headerPojo.setSname(title_list.get(i).get("sname"));
            headerPojo.setIscom(title_list.get(i).get("iscom"));
            itemPojoList = new ArrayList<>();
            for (int j = 0; j < items_list.get(i).size(); j++) {
                itemPojo = new ItemPojo();
                itemPojo.setMlno(items_list.get(i).get(j).get("mlno"));
                itemPojo.setName(items_list.get(i).get(j).get("name"));
                itemPojo.setSname(items_list.get(i).get(j).get("sname"));
                itemPojo.setSid(items_list.get(i).get(j).get("sid"));
                itemPojo.setAddress(items_list.get(i).get(j).get("address"));
                itemPojo.setIsused(items_list.get(i).get(j).get("isused").equals("1"));
                itemPojoList.add(itemPojo);
            }
            headerPojo.setMyLogisticsArray(itemPojoList);
            datas.add(headerPojo);
        }
    }

    private class HeaderPojo {
        private String sno;
        private String slno;
        private String plno;
        private String type;
        private int land;
        private int logistics;
        private String logisticsVal;
        private String lpay;
        private boolean isused;
        private String sname;
        private String iscom;
        private List<ItemPojo> myLogisticsArray;

        public String getSno() {
            return sno;
        }

        public String getIscom() {
            return iscom;
        }

        public void setIscom(String iscom) {
            this.iscom = iscom;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public String getSlno() {
            return slno;
        }

        public void setSlno(String slno) {
            this.slno = slno;
        }

        public String getPlno() {
            return plno;
        }

        public void setPlno(String plno) {
            this.plno = plno;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLand() {
            return land;
        }

        public void setLand(int land) {
            this.land = land;
        }

        public int getLogistics() {
            return logistics;
        }

        public void setLogistics(int logistics) {
            this.logistics = logistics;
        }

        public String getLogisticsVal() {
            return logisticsVal;
        }

        public void setLogisticsVal(String logisticsVal) {
            this.logisticsVal = logisticsVal;
        }

        public String getLpay() {
            return lpay;
        }

        public void setLpay(String lpay) {
            this.lpay = lpay;
        }

        public boolean getIsused() {
            return isused;
        }

        public void setIsused(boolean isused) {
            this.isused = isused;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public List<ItemPojo> getMyLogisticsArray() {
            return myLogisticsArray;
        }

        public void setMyLogisticsArray(List<ItemPojo> myLogisticsArray) {
            this.myLogisticsArray = myLogisticsArray;
        }
    }

    private class ItemPojo {
        private String mlno;
        private String name;
        private String sname;
        private String sid;
        private String address;
        private boolean isused;

        public String getMlno() {
            return mlno;
        }

        public void setMlno(String mlno) {
            this.mlno = mlno;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean getIsused() {
            return isused;
        }

        public void setIsused(boolean isused) {
            this.isused = isused;
        }

    }

}
