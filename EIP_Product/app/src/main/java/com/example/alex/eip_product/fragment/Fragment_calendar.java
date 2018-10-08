package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.alex.eip_product.R;

import java.util.Calendar;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_calendar extends Fragment {
    View v;
    private CalendarView calendarView;
    private Button confirm;
    private String current_date = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);
        initCalendarView();
        initButton();
        return v;
    }

    private void initButton() {
        confirm = v.findViewById(R.id.confirm_date);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), current_date, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCalendarView() {
        calendarView = v.findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance();
        current_date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                current_date = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });
    }
}
