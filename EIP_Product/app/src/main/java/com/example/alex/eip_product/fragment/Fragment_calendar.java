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
import java.util.Date;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_calendar extends Fragment {
    private View v;
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
        ((MainActivity) getContext()).setCurrent_date(new Date());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                ((MainActivity) getContext()).setCurrent_date(calendar.getTime());
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            calendarView.setDate(calendarView.getDate());
        }
    }
}
