package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.CommunityActivity;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeHelpCenter;
import library.GetJsonData.HelpCenterJsonData;

public class HelpCenterRecyclerViewAdapter extends RecyclerView.Adapter<HelpCenterRecyclerViewAdapter.RecycleHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private Context ctx;
    private DisplayMetrics dm;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    ArrayList<String> titleList;
    ArrayList<ArrayList<Map<String, String>>> itemLists;
    private List<HeaderPojo> datas;
    private ArrayList<Map<String, String>> headerList;
    View header;
    int had_header;

    public void setHeader(ArrayList<Map<String, String>> headerList) {
        this.headerList = headerList;
        had_header = 1;
    }

    public void removeHeader() {
        this.headerList = null;
        had_header = 1;
    }

    public HelpCenterRecyclerViewAdapter(Context ctx, JSONObject json) {
        dm = ctx.getResources().getDisplayMetrics();
        this.ctx = ctx;
        if (json != null) {
            if (AnalyzeHelpCenter.getCategoryTitle(json) != null) {
                initList(json);
            } else {
                titleList = new ArrayList<>();
                itemLists = new ArrayList<>();
            }
        } else {
            titleList = new ArrayList<>();
            itemLists = new ArrayList<>();
        }
        initData();
    }

    private void startWebView(final String icno) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json = new HelpCenterJsonData().getCitem(icno);
                new Handler(ctx.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ctx, CommunityActivity.class);

                        try {
                            intent.putExtra("title", json.getJSONObject("Data").getString("title"));
                            intent.putExtra("html", json.getJSONObject("Data").getString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }
                });
            }
        }).start();
    }

    private void initList(JSONObject json) {
        titleList = AnalyzeHelpCenter.getCategoryTitle(json);
        itemLists = AnalyzeHelpCenter.getCategoryItemArray(json);
    }

    @Override
    public HelpCenterRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(ctx).inflate(
                    R.layout.table_view_header, parent, false);
            //header 背景
            LinearLayout layout = view.findViewById(R.id.table_view_header_layout);
            layout.setBackgroundColor(ctx.getResources().getColor(R.color.gray));
            //header title
            TextView textView = view.findViewById(R.id.table_view_header_text);
            textView.setText("搜尋項目");
            view.setTag(TYPE_HEADER);
            return new HelpCenterRecyclerViewAdapter.RecycleHolder(parent, view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_header, parent, false);
            view.setTag(TYPE_CONTENT);
            return new HelpCenterRecyclerViewAdapter.RecycleHolder(parent, view);
        }
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_HEADER) {
                if (headerList != null) {
                    holder.header.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    View item;
                    TextView textView;
                    holder.search_layout.removeAllViews();
                    int index = 0;
                    for (Map<String, String> map : headerList) {
                        item = LayoutInflater.from(ctx).inflate(
                                R.layout.table_view_item, holder.parent, false);
                        item.setTag(index);
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String icno = headerList.get((int) view.getTag()).get("icno");
                                startWebView(icno);
                            }
                        });
                        textView = item.findViewById(R.id.table_view_item_text);
                        textView.setText(map.get("title"));
                        holder.search_layout.addView(item);
                        index++;
                    }
                } else {
                    holder.header.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                }
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                holder.header_title.setText(datas.get(position).getTitle());
                initItemView(holder, holder.parent, position);
                //判斷當當前使用位置未被設定時
                if (currentItem == position) {
                    holder.table_view_header_arrow.setRotation(180);
                    holder.hideArea.setVisibility(View.VISIBLE);
                } else {
                    holder.table_view_header_arrow.setRotation(0);
                    holder.hideArea.setVisibility(View.GONE);
                }
                for (int i = 0; i < holder.item_title.size(); i++) {
                    holder.item_title.get(i).setText(datas.get(position).getMyLogisticsArray().get(i).getTitle());
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
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return datas.size();

    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout showArea;
        private TextView header_title;
        private ImageView table_view_header_arrow;
        private LinearLayout hideArea;
        private List<TextView> item_title;
        private List<LinearLayout> item_layouts;
        private ViewGroup parent;
        private View header;
        private LinearLayout search_layout;

        public RecycleHolder(ViewGroup parent, View view) {
            super(view);
            this.parent = parent;
            if ((int) view.getTag() == TYPE_HEADER) {
                header = view;
                search_layout = view.findViewById(R.id.table_view_item_layout);
            } else if ((int) view.getTag() == TYPE_CONTENT) {
                showArea = view.findViewById(R.id.table_view_header_layout);
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
                header_title = showArea.findViewById(R.id.table_view_header_text);
                table_view_header_arrow = showArea.findViewById(R.id.table_view_header_arrow);
                hideArea = view.findViewById(R.id.table_view_item_layout);
                hideArea.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                hideArea.setDividerDrawable(ctx.getResources().getDrawable(R.drawable.decoration_line));
            }
        }

        @Override
        public void onClick(final View view) {

        }

    }

    private void initItemView(final RecycleHolder holder, ViewGroup parent, final int position) {
        holder.hideArea.removeAllViews();
        LinearLayout linearLayout;
        TextView textView;
        View view;
        //item
        holder.item_title = new ArrayList<>();
        holder.item_layouts = new ArrayList<>();
        for (int i = 0; i < datas.get(position).getMyLogisticsArray().size(); i++) {
            view = LayoutInflater.from(ctx).inflate(R.layout.table_view_item, parent, false);
            linearLayout = view.findViewById(R.id.table_view_item_layout);
            linearLayout.setTag(position + "," + i);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = view.getTag().toString();
                    int header_position = Integer.parseInt(tag.split(",")[0]);
                    int position = Integer.parseInt(tag.split(",")[1]);
                    String icno = datas.get(header_position).getMyLogisticsArray().get(position).getIcno();
                    startWebView(icno);
                }
            });
            holder.item_layouts.add(linearLayout);
            textView = view.findViewById(R.id.table_view_item_text);
            holder.item_title.add(textView);
            holder.hideArea.addView(view);

        }
    }

    public void initData() {
        datas = new ArrayList<>();
        HeaderPojo headerPojo = new HeaderPojo();
        List<ItemPojo> itemPojoList = new ArrayList<>();
        ItemPojo itemPojo;
        if (headerList != null) {
            for (int j = 0; j < headerList.size(); j++) {
                itemPojo = new ItemPojo();
                itemPojo.setIcno(headerList.get(j).get("icno"));
                itemPojo.setTitle(headerList.get(j).get("title"));
                itemPojoList.add(itemPojo);
            }
            headerPojo.setMyLogisticsArray(itemPojoList);
        }
        datas.add(headerPojo);

        for (int i = 0; i < titleList.size(); i++) {
            headerPojo = new HeaderPojo();
            headerPojo.setTitle(titleList.get(i));
            itemPojoList = new ArrayList<>();
            for (int j = 0; j < itemLists.get(i).size(); j++) {
                itemPojo = new ItemPojo();
                itemPojo.setIcno(itemLists.get(i).get(j).get("icno"));
                itemPojo.setTitle(itemLists.get(i).get(j).get("title"));
                itemPojoList.add(itemPojo);
            }
            headerPojo.setMyLogisticsArray(itemPojoList);
            datas.add(headerPojo);
        }
    }

    private class HeaderPojo {
        private String title;
        private List<ItemPojo> myLogisticsArray;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ItemPojo> getMyLogisticsArray() {
            return myLogisticsArray;
        }

        public void setMyLogisticsArray(List<ItemPojo> myLogisticsArray) {
            this.myLogisticsArray = myLogisticsArray;
        }
    }

    private class ItemPojo {
        private String icno;
        private String title;


        public String getIcno() {
            return icno;
        }

        public void setIcno(String icno) {
            this.icno = icno;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
