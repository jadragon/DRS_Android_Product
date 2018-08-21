package com.example.alex.posdemo.adapter.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SpinnerPojoAdapter<T> extends BaseAdapter {


    private LayoutInflater inflator;
    private String[] colorNames;
    private int[] colorList;

    public SpinnerPojoAdapter(Context context, T t) {
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return colorList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //    convertView = inflator.inflate(R.layout.color_spinner, null);
        //   TextView tvColor = (TextView) convertView.findViewById(R.id.tv_color);

        //tvColor.setText(colorNames[position]);
        //  tvColor.setTextColor(Color.BLACK);
        // tvColor.setBackgroundColor(colorList[position]);

        return convertView;


    }
}
