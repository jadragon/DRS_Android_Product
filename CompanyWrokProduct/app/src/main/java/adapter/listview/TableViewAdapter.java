package adapter.listview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeHelpCenter;

public class TableViewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private DisplayMetrics dm;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    private TableViewAdapter.ViewHolder holder;
    private View[] holderList;
    private ArrayList<String> titleList;
    private ArrayList<ArrayList<Map<String, String>>> itemLists;
    private View header;
    private int had_header;

    public TableViewAdapter(Context context, JSONObject json) {
        super();
        dm = context.getResources().getDisplayMetrics();
        this.context = context;
        initList(json);
        holderList = new View[titleList.size()];

    }

    public void setHeader(View header) {
        this.header = header;
        had_header = 1;
    }

    public void removeHeader() {
        header = null;
        had_header = 0;
    }

    private void initList(JSONObject json) {
        titleList = AnalyzeHelpCenter.getCategoryTitle(json);
        itemLists = AnalyzeHelpCenter.getCategoryItemArray(json);
    }

    @Override
    public int getCount() {
        if (header != null)
            return titleList.size() + 1;
        else
            return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        if (header != null)
            return titleList.size() + 1;
        else
            return titleList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View createView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(
                R.layout.table_view_header, parent, false);
        holder = new TableViewAdapter.ViewHolder();
        //header
        holder.table_view_header_layout = convertView.findViewById(R.id.table_view_header_layout);
        holder.table_view_header_layout.setOnClickListener(new View.OnClickListener() {
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
        holder.table_view_header_text = holder.table_view_header_layout.findViewById(R.id.table_view_header_text);
        holder.table_view_header_arrow = holder.table_view_header_layout.findViewById(R.id.table_view_header_arrow);
        //content
        holder.table_view_item_layout = convertView.findViewById(R.id.table_view_item_layout);
        holder.table_view_item_layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        holder.table_view_item_layout.setDividerDrawable(context.getResources().getDrawable(R.drawable.decoration_line));

        LinearLayout linearLayout;
        TextView textView;
        View view;
        //item
        holder.item_layouts = new ArrayList<>();
        holder.item_names = new ArrayList<>();
        for (int i = 0; i < itemLists.get(position).size(); i++) {
            view = LayoutInflater.from(context).inflate(R.layout.table_view_item, parent, false);
            linearLayout = view.findViewById(R.id.table_view_item_layout);
            linearLayout.setTag(position);
            linearLayout.setOnClickListener(this);
            holder.item_layouts.add(linearLayout);
            textView = view.findViewById(R.id.table_view_item_text);
            holder.item_names.add(textView);
            holder.table_view_item_layout.addView(view);
        }
        convertView.setTag(holder);
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (header != null && position == 0) {
            return header;
        }
        position -= had_header;
        if (holderList[position] == null) {
            convertView = createView(position, parent);
            holderList[position] = convertView;
        } else {
            holder = (TableViewAdapter.ViewHolder) holderList[position].getTag();
        }
        //header
        holder.table_view_header_text.setText(titleList.get(position));
        //content
        for (int i = 0; i < itemLists.get(position).size(); i++) {
            holder.item_names.get(i).setText(itemLists.get(position).get(i).get("title"));
        }
        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
        holder.table_view_header_layout.setTag(position);
        //根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.table_view_header_arrow.setRotation(180);
            holder.table_view_item_layout.setVisibility(View.VISIBLE);
        } else {
            holder.table_view_header_arrow.setRotation(0);
            holder.table_view_item_layout.setVisibility(View.GONE);
        }

        return holderList[position];

    }

    @Override
    public void onClick(View v) {
        int header_position = (int) v.getTag();
        holder = (TableViewAdapter.ViewHolder) holderList[header_position].getTag();
        int position = holder.item_layouts.indexOf(v);
        /*
        //取得header
        ViewParent viewGroup = v.getParent();
        ViewGroup view = (ViewGroup) viewGroup;
        //取得item總數
        int count = view.getChildCount();
        */

        //設定運送方式

    }

    private class ViewHolder {
        private LinearLayout table_view_header_layout;
        private TextView table_view_header_text;
        private ImageView table_view_header_arrow;
        private LinearLayout table_view_item_layout;

        private List<TextView> item_names;

        private List<LinearLayout> item_layouts;
    }

}