package com.example.alex.posdemo.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.JsonApi.PunchApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_punch extends Fragment {
    View v;
    View punch_clenderstart, punch_clenderend;
    TextView punch_datestart, punch_dateend;
    Button punch_search;
    PunchApi punchApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_punch_layout, container, false);
        punchApi = new PunchApi();
        initCalender();
        initSearchPunch();
        return v;
    }

    private void initSearchPunch() {
        punch_search = v.findViewById(R.id.punch_search);
        punch_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public void onTaskBefore() {

                    }

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return punchApi.attendance_seach("a42LeDpQoCqr43FmcC@_Dpw==", "", "", "2018-05-01", "2018-08-20");
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        Log.e("punch", jsonObject + "");
                    }
                });
            }
        });
    }

    private void initCalender() {
        //start
        punch_datestart = v.findViewById(R.id.punch_datestart);
        final DatePicker datePicker1 = new DatePicker(getContext());
        Time time = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        time.setToNow(); // 取得系统时间。
        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;
        punch_datestart.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));

        datePicker1.init(year, month , day, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                punch_datestart.setText(datePicker1.getYear() + "-" + String.format("%02d", datePicker1.getMonth() ) + "-" + String.format("%02d", datePicker1.getDayOfMonth()));
                dialogInterface.dismiss();
            }
        });
        builder.setView(datePicker1);
        final AlertDialog alertDialog = builder.create();  //创建对话框
        punch_clenderstart = v.findViewById(R.id.punch_clenderstart);
        punch_clenderstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        //end
        punch_dateend = v.findViewById(R.id.punch_dateend);
        final DatePicker datePicker2 = new DatePicker(getContext());
        punch_dateend.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));

        datePicker2.init(year, month - 1, day, null);

        builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                punch_dateend.setText(datePicker2.getYear() + "-" + String.format("%02d", datePicker2.getMonth() + 1) + "-" + String.format("%02d", datePicker2.getDayOfMonth()));
                dialogInterface.dismiss();
            }
        });
        builder.setView(datePicker2);
        final AlertDialog alertDialog2 = builder.create();  //创建对话框
        punch_clenderend = v.findViewById(R.id.punch_clenderend);
        punch_clenderend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.show();
            }
        });
    }


}
