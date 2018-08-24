package com.example.alex.posdemo.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.AttendanceAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.AttendanceStorePojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_PunchInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.PunchApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_punch extends Fragment {
    View v;
    View punch_clenderstart, punch_clenderend;
    TextView punch_search_datestart, punch_search_dateend;
    EditText punch_search_name, punch_search_en;
    EditText punch_attendance_accound, punch_attendance_pawd, punch_attendance_note;
    Button punch_search_confirm, punch_attendace_confim;
    PunchApi punchApi;
    RecyclerView punch_recyclerview;
    AttendanceAdapter attendanceAdapter;
    Spinner punch_search_store, punch_attendance_classnum, punch_attendance_commute;
    AttendanceStorePojo attendanceStorePojo;
    private UserInfo userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_punch_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();
        Time time = new Time();
        time.setToNow(); // 取得系统时间。
        punchApi = new PunchApi();
        initView();
        initCalender(time);
        initSearchAttendance(time);
        initPunchButton();
        return v;
    }

    private void initView() {
        //
        punch_search_name = v.findViewById(R.id.punch_search_name);
        punch_search_en = v.findViewById(R.id.punch_search_en);
        //
        punch_attendance_accound = v.findViewById(R.id.punch_attendance_accound);
        punch_attendance_pawd = v.findViewById(R.id.punch_attendance_pawd);
        punch_attendance_note = v.findViewById(R.id.punch_attendance_note);
        //
        punch_attendance_classnum = v.findViewById(R.id.punch_attendance_classnum);
        punch_attendance_classnum.setAdapter(new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.attendance_classnum)));
        //
        punch_attendance_commute = v.findViewById(R.id.punch_attendance_commute);
        punch_attendance_commute.setAdapter(new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.attendance_commute)));


    }

    private void initSearchAttendance(final Time time) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                String date = dateFormat(time);
                return punchApi.attendance_seach(userInfo.getS_no(), "", "", date, date);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initSearchView(jsonObject);
                initRecyclerView(jsonObject);

            }

        });


    }

    private void initSearchView(JSONObject jsonObject) {
        punch_search_store = v.findViewById(R.id.punch_search_store);
        attendanceStorePojo = new Analyze_PunchInfo().getAttendanceStore(jsonObject);
        punch_search_store.setAdapter(new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, attendanceStorePojo.getStore()));
    }

    private void initRecyclerView(JSONObject jsonObject) {
        punch_recyclerview = v.findViewById(R.id.punch_recyclerview);
        attendanceAdapter = new AttendanceAdapter(getContext(), jsonObject);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        punch_recyclerview.setLayoutManager(layoutManager);
        punch_recyclerview.setAdapter(attendanceAdapter);
    }


    private void initPunchButton() {
        punch_search_confirm = v.findViewById(R.id.punch_search_confirm);
        punch_search_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public void onTaskBefore() {

                    }

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return punchApi.attendance_seach(attendanceStorePojo.getS_no().get(punch_search_store.getSelectedItemPosition()), punch_search_name.getText().toString(), punch_search_en.getText().toString(), punch_search_datestart.getText().toString(), punch_search_dateend.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        attendanceAdapter.setFilter(jsonObject);
                    }
                });
            }
        });
        punch_attendace_confim = v.findViewById(R.id.punch_attendace_confim);
        punch_attendace_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public void onTaskBefore() {

                    }

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return punchApi.attendance_puch(punch_attendance_classnum.getSelectedItemPosition(), punch_attendance_commute.getSelectedItemPosition(),
                                punch_attendance_note.getText().toString(), punch_attendance_accound.getText().toString(), punch_attendance_pawd.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            setFilter();
                        } else {
                            new ToastMessageDialog(getContext(),ToastMessageDialog.TYPE_ERROR).confirm(AnalyzeUtil.getMessage(jsonObject));
                        }

                    }
                });
            }
        });
    }

    private void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return punchApi.attendance_seach(attendanceStorePojo.getS_no().get(punch_search_store.getSelectedItemPosition()), punch_search_name.getText().toString(),
                        punch_search_en.getText().toString(), punch_search_datestart.getText().toString(), punch_search_dateend.getText().toString());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                attendanceAdapter.setFilter(jsonObject);
            }
        });
    }

    private void initCalender(Time time) {
        //start
        punch_search_datestart = v.findViewById(R.id.punch_search_datestart);
        final DatePicker datePicker1 = new DatePicker(getContext());

        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;
        punch_search_datestart.setText(dateFormat(year, month, day));
        datePicker1.init(year, month, day, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                punch_search_datestart.setText(dateFormat(datePicker1.getYear(), datePicker1.getMonth(), datePicker1.getDayOfMonth()));
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
        punch_search_dateend = v.findViewById(R.id.punch_search_dateend);
        final DatePicker datePicker2 = new DatePicker(getContext());
        punch_search_dateend.setText(dateFormat(year, month, day));
        datePicker2.init(year, month, day, null);

        builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                punch_search_dateend.setText(dateFormat(datePicker2.getYear(), datePicker2.getMonth(), datePicker2.getDayOfMonth()));
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

    private String dateFormat(int year, int month, int day) {
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    private String dateFormat(Time time) {
        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }
}
