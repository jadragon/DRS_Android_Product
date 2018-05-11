package adapter.listview;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

public class CommunityListViewAdapter extends BaseAdapter {
    View view;
    String[] txt_sourse;
    TypedArray image_sourse;
    public CommunityListViewAdapter( TypedArray image_sourse, String[] txt_sourse) {
        this.image_sourse = image_sourse;
        this.txt_sourse = txt_sourse;
    }

    //優化Listview 避免重新加載
    //這邊宣告你會動到的Item元件
    static class ViewHolder {
        ImageView imageView;
        TextView Name;
    }

    @Override
    public int getCount() {
        return txt_sourse.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //當ListView被拖拉時會不斷觸發getView，為了避免重複加載必須加上這個判斷
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_listview, parent, false);
            holder.imageView = convertView.findViewById(R.id.viewitem_listview_img);
            holder.Name = convertView.findViewById(R.id.viewitem_listview_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(image_sourse.getResourceId(position, 0));
        holder.Name.setText(txt_sourse[position]);
        return convertView;
    }

}
