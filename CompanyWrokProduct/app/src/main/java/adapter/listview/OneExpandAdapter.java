package adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 点击item展开隐藏部分,再次点击收起
 * 只可展开一条记录
 *
 * @author WangJ
 * @date 2016.01.31
 */
public class OneExpandAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    ViewHolder holder = null;

    public OneExpandAdapter(Context context,
                            ArrayList<HashMap<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(
                R.layout.vitem_shipwaylist, parent, false);
        holder = new ViewHolder();
        holder.showArea = convertView.findViewById(R.id.layout_showArea);
        holder.hideArea = convertView.findViewById(R.id.layout_hideArea);
        View view = null;
        for (int i = 0; i < 10; i++) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.viewitem_shipwaylist_item, parent, false);
            holder.shipwaylist_item_layout = view.findViewById(R.id.shipwaylist_item_layout);
            holder.shipwaylist_item_layout.setTag(i);
            holder.shipwaylist_item_layout.setOnClickListener(this);
            holder.hideArea.addView(view);
        }
        convertView.setTag(holder);
        return convertView;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createView(position, convertView, parent);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
        holder.showArea.setTag(position);

        //根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }

        holder.showArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                }
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        //取得header
        ViewParent viewGroup = v.getParent();
        ViewGroup view = (ViewGroup) viewGroup;
        //取得item總數
        int count = view.getChildCount();
        for (int i = 0; i < count; i++) {
            holder.button = view.findViewWithTag(i).findViewById(R.id.shipwaylist_item_btn);
            holder.button.setSelected(false);
        }
        holder.button = v.findViewById(R.id.shipwaylist_item_btn);
        holder.button.setSelected(true);
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        private LinearLayout showArea;
        private LinearLayout hideArea;
        private Button button;
        private LinearLayout shipwaylist_item_layout;
    }
}