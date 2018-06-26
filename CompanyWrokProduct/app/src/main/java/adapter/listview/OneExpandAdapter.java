package adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.AddDeliveryShipWayActivity;
import com.test.tw.wrokproduct.AddStoreShipWayActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ShopCartJsonData;

/**
 * 点击item展开隐藏部分,再次点击收起
 * 只可展开一条记录
 *
 * @author WangJ
 * @date 2016.01.31
 */
public class OneExpandAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private JSONObject json;
    private DisplayMetrics dm;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    ViewHolder holder;
    ArrayList<Map<String, String>> title_list;
    ArrayList<ArrayList<Map<String, String>>> items_list;
    private List<HeaderPojo> datas;
    String[] lanes = {"無", "本島", "離島", "海外"};
    int[] colors = {R.color.sienna, R.color.seagreen, R.color.deepskyblue, R.color.violet, R.color.gold, R.color.limegreen, R.color.darkorange, R.color.navy};
    View[] holderList;
    String token;
    String sno, plno, mino, sname;

    public OneExpandAdapter(Context context, JSONObject json) {
        super();
        dm = context.getResources().getDisplayMetrics();
        this.context = context;
        this.json = json;
        token = ((GlobalVariable) context.getApplicationContext()).getToken();
        if (json != null) {
            title_list = AnalyzeShopCart.getStoreLogisticsData(json);
            items_list = AnalyzeShopCart.getmyLogisticsArray(json);
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }
        holderList = new View[title_list.size()];
        initData();
    }

    @Override
    public int getCount() {
        return title_list.size();
    }

    @Override
    public Object getItem(int position) {
        return title_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(
                R.layout.viewitem_shipwaylist, parent, false);
        holder = new ViewHolder();
        //header
        holder.showArea = convertView.findViewById(R.id.layout_showArea);
        holder.showArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag != currentItem) { //再次点击
                    currentItem = tag;
                }
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步
            }
        });
        holder.vitem_shipwaylist_logisticsVal = holder.showArea.findViewById(R.id.vitem_shipwaylist_logisticsVal);
        holder.vitem_shipwaylist_lpay = holder.showArea.findViewById(R.id.vitem_shipwaylist_lpay);
        holder.vitem_shipwaylist_land = holder.showArea.findViewById(R.id.vitem_shipwaylist_land);
        holder.vitem_shipwaylist_sname = holder.showArea.findViewById(R.id.vitem_shipwaylist_sname);
        holder.vitem_shipwaylist_nike = holder.showArea.findViewById(R.id.vitem_shipwaylist_nike);
        //content
        holder.hideArea = convertView.findViewById(R.id.layout_hideArea);
        holder.hideArea.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        holder.hideArea.setDividerDrawable(context.getResources().getDrawable(R.drawable.decoration_line));

        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;
        View view;
        if (datas.get(position).getIsused()) {
            holder.vitem_shipwaylist_nike.setVisibility(View.VISIBLE);
        } else {
            holder.vitem_shipwaylist_nike.setVisibility(View.INVISIBLE);
        }
        //item
        holder.item_images = new ArrayList<>();
        holder.item_layouts = new ArrayList<>();
        holder.item_names = new ArrayList<>();
        holder.item_snames = new ArrayList<>();
        holder.item_addresses = new ArrayList<>();
        for (int i = 0; i < items_list.get(position).size(); i++) {
            view = LayoutInflater.from(context).inflate(R.layout.viewitem_shipwaylist_item, parent, false);
            linearLayout = view.findViewById(R.id.shipwaylist_item_layout);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels / 7));
            linearLayout.setTag(position);
            linearLayout.setOnClickListener(this);
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
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * dm.density)));
        textView = new TextView(context);
        textView.setText("新增" + datas.get(position).getLogisticsVal());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        linearLayout.addView(textView);
        //設定背景
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        linearLayout.setBackgroundResource(outValue.resourceId);
        holder.footer = linearLayout;
        holder.footer.setTag(position);
        //新增運送方式
        holder.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                Intent intent;
                if (datas.get(index).getLogistics() <= 3) {
                    intent = new Intent(context, AddStoreShipWayActivity.class);
                } else {
                    intent = new Intent(context, AddDeliveryShipWayActivity.class);
                }
                intent.putExtra("sno", title_list.get(index).get("sno"));
                intent.putExtra("plno", title_list.get(index).get("plno"));
                intent.putExtra("type", title_list.get(index).get("type"));
                intent.putExtra("land", title_list.get(index).get("land"));
                intent.putExtra("logistics", title_list.get(index).get("logistics"));
                context.startActivity(intent);
            }
        });
        holder.hideArea.addView(linearLayout);
        if (datas.get(position).getIsused()) {
            holder.hideArea.setVisibility(View.VISIBLE);
            //設定目前選取的item位置
            currentItem = position;
        }
        convertView.setTag(holder);
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (holderList[position] == null) {
            convertView = createView(position, convertView, parent);
            holderList[position] = convertView;
        } else {
            holder = (ViewHolder) holderList[position].getTag();
        }
        //header
        holder.vitem_shipwaylist_logisticsVal.setText(datas.get(position).getLogisticsVal());
        holder.vitem_shipwaylist_logisticsVal.setBackgroundColor(context.getResources().getColor(colors[datas.get(position).getLogistics()]));
        holder.vitem_shipwaylist_lpay.setText("$" + datas.get(position).getLpay());
        holder.vitem_shipwaylist_land.setText(lanes[datas.get(position).getLand()]);
        holder.vitem_shipwaylist_sname.setText(datas.get(position).getSname());
        //content
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

        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
        holder.showArea.setTag(position);
        //根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.vitem_shipwaylist_nike.setVisibility(View.VISIBLE);
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.vitem_shipwaylist_nike.setVisibility(View.INVISIBLE);
            holder.hideArea.setVisibility(View.GONE);
        }

        return holderList[position];
    }

    @Override
    public void onClick(View v) {
        int header_position = (int) v.getTag();
        holder = (ViewHolder) holderList[header_position].getTag();
        int position = holder.item_layouts.indexOf(v);
        /*
        //取得header
        ViewParent viewGroup = v.getParent();
        ViewGroup view = (ViewGroup) viewGroup;
        //取得item總數
        int count = view.getChildCount();
        */
        for (int i = 0; i < holder.item_layouts.size(); i++) {
            holder.item_images.get(i).setSelected(false);
        }
        holder.item_images.get(position).setSelected(true);
        //設定運送方式
        sno = datas.get(header_position).getSno();
        plno = datas.get(header_position).getPlno();
        mino = datas.get(header_position).getMyLogisticsArray().get(position).getMlno();
        sname = datas.get(header_position).getMyLogisticsArray().get(position).getSname();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ShopCartJsonData().setStoreMemberLogistics(token, sno, plno, mino);
                json = new ShopCartJsonData().getStoreLogistics(token, sno);
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        holder.vitem_shipwaylist_sname.setText(sname);
                        setFilter(json);
                    }
                });
            }
        }).start();

    }

    private class ViewHolder {
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
    }

    public void setFilter(JSONObject newjson) {
        this.json = newjson;
        if (json != null) {
            title_list = AnalyzeShopCart.getStoreLogisticsData(json);
            items_list = AnalyzeShopCart.getmyLogisticsArray(json);
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }

        holderList = new View[title_list.size()];
        initData();
        notifyDataSetChanged();
    }

    public void setFilterAfterAdd(JSONObject newjson) {
        this.json = newjson;
        if (json != null) {
            title_list = AnalyzeShopCart.getStoreLogisticsData(json);
            items_list = AnalyzeShopCart.getmyLogisticsArray(json);
        } else {
            title_list = new ArrayList<>();
            items_list = new ArrayList<>();
        }

        holderList = new View[title_list.size()];
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

        notifyDataSetChanged();
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