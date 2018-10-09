package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;

import java.util.Calendar;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_calendar extends Fragment {
    View v;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);
        initCalendarView();
        return v;
    }


    private void initCalendarView() {
        calendarView = v.findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance();
        ((MainActivity) getContext()).setCurrent_date(c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "(" + getDayOfWeek(c.get(Calendar.DAY_OF_WEEK)) + ")");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                ((MainActivity) getContext()).setCurrent_date(year + "/" + (month + 1) + "/" + dayOfMonth + "(" + getDayOfWeek(dayOfWeek) + ")");
            }
        });
    }

    public String getDayOfWeek(int dayofweek) {
        String day = "";
        switch (dayofweek) {
            case Calendar.SUNDAY:
                day = "日";
                break;
            case Calendar.MONDAY:
                day = "一";
                break;
            case Calendar.TUESDAY:
                day = "二";
                break;
            case Calendar.WEDNESDAY:
                day = "三";
                break;
            case Calendar.THURSDAY:
                day = "四";
                break;
            case Calendar.FRIDAY:
                day = "五";
                break;
            case Calendar.SATURDAY:
                day = "六";
                break;
        }
        return day;
    }

}
