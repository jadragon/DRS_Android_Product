package adapter.recyclerview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.AddDeliveryShipWayActivity;
import com.test.tw.wrokproduct.AddStoreShipWayActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeLogistics;
import library.GetJsonData.LogisticsJsonData;

public class ShipAddressRecyclerAdapter extends RecyclerView.Adapter<ShipAddressRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    JSONObject json;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private List<ItemPojo> itemPojoList;
    private DisplayMetrics dm;
    String[] shipways;
    TypedArray colors;
    int lastuse_position;
    int type;
GlobalVariable gv;
    public ShipAddressRecyclerAdapter(Context ctx, JSONObject json, int type) {
        this.ctx = ctx;
        this.json = json;
        this.type = type;
        dm = ctx.getResources().getDisplayMetrics();
        gv = ((GlobalVariable) ctx.getApplicationContext());
        shipways = ctx.getResources().getStringArray(R.array.shipway_title);
        colors = ctx.getResources().obtainTypedArray(R.array.shipway_color);
        //JSON
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeLogistics.getLogistics(json);
        } else {
            list = new ArrayList<>();
        }
        initItems(list);

    }

    private void initItems(ArrayList<Map<String, String>> list) {
        itemPojoList = new ArrayList<>();
        ItemPojo itemPojo;
        for (int i = 0; i < list.size(); i++) {
            itemPojo = new ItemPojo();
            itemPojo.setMlno(list.get(i).get("mlno"));
            itemPojo.setLand(Integer.parseInt(list.get(i).get("land")));
            itemPojo.setLogistics(Integer.parseInt(list.get(i).get("logistics")));
            itemPojo.setLogisticsVal(list.get(i).get("logisticsVal"));
            itemPojo.setName(list.get(i).get("name"));
            itemPojo.setMpcode(list.get(i).get("mpcode"));
            itemPojo.setMp(list.get(i).get("mp"));
            itemPojo.setSname(list.get(i).get("sname"));
            itemPojo.setSid(list.get(i).get("sid"));
            itemPojo.setShit(list.get(i).get("shit"));
            itemPojo.setCity(list.get(i).get("city"));
            itemPojo.setArea(list.get(i).get("area"));
            itemPojo.setZipcode(list.get(i).get("zipcode"));
            itemPojo.setAddress(list.get(i).get("address"));
            if (list.get(i).get("isused").equals("1")) {
                itemPojo.setIsused(true);
                lastuse_position = i;
            } else {
                itemPojo.setIsused(false);
            }
            itemPojoList.add(itemPojo);
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public ShipAddressRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            HorizontalScrollView scrollView = new HorizontalScrollView(ctx);
            scrollView.setHorizontalScrollBarEnabled(false);
            LinearLayout linearLayout = new LinearLayout(ctx);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView;
            if (type == 0) {
                for (int i = 1; i < 4; i++) {
                    textView = new TextView(ctx);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("+" + shipways[i] + "地址");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((dm.widthPixels - 20 * dm.density) / 2), dm.heightPixels / 9);
                    params.setMargins((int) (5 * dm.density), (int) (10 * dm.density), (int) (5 * dm.density), (int) (10 * dm.density));
                    textView.setLayoutParams(params);
                    textView.setTag(i);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundColor(ctx.getResources().getColor(colors.getResourceId(i, 0)));
                    linearLayout.addView(textView);
                }
            } else {
                for (int i = 4; i < 6; i++) {
                    textView = new TextView(ctx);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("+" + shipways[i] + "地址");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((dm.widthPixels - 20 * dm.density) / 2), dm.heightPixels / 9);
                    params.setMargins((int) (5 * dm.density), (int) (10 * dm.density), (int) (5 * dm.density), (int) (10 * dm.density));
                    textView.setLayoutParams(params);
                    textView.setTag(i);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundColor(ctx.getResources().getColor(colors.getResourceId(i, 0)));
                    linearLayout.addView(textView);
                }
            }
            scrollView.addView(linearLayout);
            return new ShipAddressRecyclerAdapter.RecycleHolder(ctx, scrollView, TYPE_HEADER);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_shipway_address, parent, false);
        view.findViewById(R.id.資訊布局).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels / 5));
        return new ShipAddressRecyclerAdapter.RecycleHolder(ctx, view, TYPE_CONTENT);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {

        } else if (getItemViewType(position) == TYPE_CONTENT) {
            position--;
            holder.viewitem_shipaddress_logisticsVal.setText(itemPojoList.get(position).getLogisticsVal());
            holder.viewitem_shipaddress_logisticsVal.setBackgroundColor(ctx.getResources().getColor(colors.getResourceId(itemPojoList.get(position).getLogistics(), 0)));
            if (itemPojoList.get(position).isIsused())
                ((GradientDrawable) holder.按鈕預設.getBackground()).setColor(ctx.getResources().getColor(R.color.red));
            else
                ((GradientDrawable) holder.按鈕預設.getBackground()).setColor(ctx.getResources().getColor(R.color.gainsboro));
            if (type == 0) {
                holder.文字店號.setText("店號(" + itemPojoList.get(position).getSid() + ")");
                holder.文字門市.setText(itemPojoList.get(position).getSname());
                holder.文字姓名.setText(itemPojoList.get(position).getName());
                holder.文字電話.setText(itemPojoList.get(position).getMpcode() + " " + itemPojoList.get(position).getMp());
                holder.文字地址.setText(itemPojoList.get(position).getAddress());
            } else if (type == 1) {
                holder.文字店號.setText(itemPojoList.get(position).getZipcode());
                holder.文字門市.setText(itemPojoList.get(position).getArea());
                holder.文字姓名.setText(itemPojoList.get(position).getName());
                holder.文字電話.setText(itemPojoList.get(position).getMpcode() + " " + itemPojoList.get(position).getMp());
                holder.文字地址.setText(itemPojoList.get(position).getAddress());
            }
        }

    }


    @Override
    public int getItemCount() {
        return itemPojoList.size() + 1;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView viewitem_shipaddress_logisticsVal, 文字店號, 文字門市, 文字姓名, 文字電話, 文字地址;
        Button 按鈕預設;
        LinearLayout 資訊布局;
        AlertDialog.Builder builder;
        Intent intent;

        public RecycleHolder(final Context ctx, View view, int viewType) {
            super(view);
            this.context = ctx;
            if (viewType == TYPE_HEADER) {
                ViewGroup layout = ((ViewGroup) (((ViewGroup) view).getChildAt(0)));
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View textView = layout.getChildAt(i);
                    textView.setOnClickListener(this);
                }
            } else if (viewType == TYPE_CONTENT) {
                viewitem_shipaddress_logisticsVal = view.findViewById(R.id.viewitem_shipaddress_logisticsVal);
                文字店號 = view.findViewById(R.id.文字店號);
                文字門市 = view.findViewById(R.id.文字門市);
                文字姓名 = view.findViewById(R.id.文字姓名);
                文字電話 = view.findViewById(R.id.文字電話);
                文字地址 = view.findViewById(R.id.文字地址);
                按鈕預設 = view.findViewById(R.id.按鈕預設);
                按鈕預設.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject = new LogisticsJsonData().setInitLogistics(gv.getToken(), itemPojoList.get(getAdapterPosition() - 1).getMlno(), type);
                                try {
                                    if (jsonObject.getBoolean("Success")) {
                                        new Handler(ctx.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                itemPojoList.get(lastuse_position).setIsused(false);
                                                notifyItemChanged(lastuse_position + 1);
                                                itemPojoList.get(getAdapterPosition() - 1).setIsused(true);
                                                lastuse_position = getAdapterPosition() - 1;
                                                notifyItemChanged(getAdapterPosition());
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                資訊布局 = view.findViewById(R.id.資訊布局);
                資訊布局.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        if (type == 0)
                            intent = new Intent(context, AddStoreShipWayActivity.class);
                        else
                            intent = new Intent(context, AddDeliveryShipWayActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("land", itemPojoList.get(getAdapterPosition() - 1).getLand() + "");
                        intent.putExtra("logistics", itemPojoList.get(getAdapterPosition() - 1).getLogistics() + "");
                        intent.putExtra("mlno", itemPojoList.get(getAdapterPosition() - 1).getMlno());
                        intent.putExtra("name", itemPojoList.get(getAdapterPosition() - 1).getName());
                        intent.putExtra("mp", itemPojoList.get(getAdapterPosition() - 1).getMp());
                        intent.putExtra("sname", itemPojoList.get(getAdapterPosition() - 1).getSname());
                        intent.putExtra("sid", itemPojoList.get(getAdapterPosition() - 1).getSid());
                        intent.putExtra("city", itemPojoList.get(getAdapterPosition() - 1).getCity());
                        intent.putExtra("area", itemPojoList.get(getAdapterPosition() - 1).getArea());
                        intent.putExtra("zipcode", itemPojoList.get(getAdapterPosition() - 1).getZipcode());
                        intent.putExtra("address", itemPojoList.get(getAdapterPosition() - 1).getAddress());
                        ((Activity) ctx).startActivityForResult(intent, 0);
                    }
                });
                資訊布局.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        builder = new AlertDialog.Builder(ctx);
                        builder.setMessage("是否刪除此運送方式?").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final JSONObject jsonObject = new LogisticsJsonData().delLogistics(gv.getToken(), itemPojoList.get(getAdapterPosition() - 1).getMlno());
                                            json = new LogisticsJsonData().getLogistics(gv.getToken(), type);
                                            if (jsonObject.getBoolean("Success")) {
                                                new Handler(ctx.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        setFilter(json);
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
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ctx, "取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        //点击dialog之外的区域禁止取消dialog
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        return false;
                    }
                });
            }


        }

        @Override
        public void onClick(View view) {

            switch ((int) view.getTag()) {

                case 0:
                    break;
                case 1:
                    intent = new Intent(context, AddStoreShipWayActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("land", "1");
                    intent.putExtra("logistics", "1");
                    ((Activity) ctx).startActivityForResult(intent, 0);
                    break;
                case 2:
                    intent = new Intent(context, AddStoreShipWayActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("land", "1");
                    intent.putExtra("logistics", "2");
                    ((Activity) ctx).startActivityForResult(intent, 0);
                    break;
                case 3:
                    intent = new Intent(context, AddStoreShipWayActivity.class);
                    intent.putExtra("type", type + "");
                    intent.putExtra("land", "1");
                    intent.putExtra("logistics", "3");
                    ((Activity) ctx).startActivityForResult(intent, 0);
                    break;
                case 4:
                    builder = new AlertDialog.Builder(context);
                    builder.setItems(new String[]{"本島", "離島"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            intent = new Intent(context, AddDeliveryShipWayActivity.class);
                            intent.putExtra("type", type + "");
                            intent.putExtra("land", "" + (i + 1));
                            intent.putExtra("logistics", "4");
                            ((Activity) ctx).startActivityForResult(intent, 0);
                        }
                    }).setTitle("請選擇您要運送的區域").show();


                    break;
                case 5:
                    builder = new AlertDialog.Builder(context);
                    builder.setItems(new String[]{"本島", "離島"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            intent = new Intent(context, AddDeliveryShipWayActivity.class);
                            intent.putExtra("type", type + "");
                            intent.putExtra("land", "" + (i + 1));
                            intent.putExtra("logistics", "5");
                            ((Activity) ctx).startActivityForResult(intent, 0);
                        }
                    }).setTitle("請選擇您要運送的區域").show();
                    break;
                case 6:
                    break;
                case 7:
                    break;
            }
        }
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeLogistics.getLogistics(json);
        } else {
            list = new ArrayList<>();
        }
        initItems(list);

        notifyDataSetChanged();

    }


    private class ItemPojo {
        String mlno;
        int land;
        int logistics;
        String logisticsVal;
        String name;
        String mpcode;
        String mp;
        String sname;
        String sid;
        String shit;
        String city;
        String area;
        String zipcode;
        String address;
        boolean isused;

        public String getMlno() {
            return mlno;
        }

        public void setMlno(String mlno) {
            this.mlno = mlno;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMpcode() {
            return mpcode;
        }

        public void setMpcode(String mpcode) {
            this.mpcode = mpcode;
        }

        public String getMp() {
            return mp;
        }

        public void setMp(String mp) {
            this.mp = mp;
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

        public String getShit() {
            return shit;
        }

        public void setShit(String shit) {
            this.shit = shit;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isIsused() {
            return isused;
        }

        public void setIsused(boolean isused) {
            this.isused = isused;
        }
    }
}