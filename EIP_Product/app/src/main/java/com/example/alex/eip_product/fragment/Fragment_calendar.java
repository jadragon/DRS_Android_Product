package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.alex.eip_product.GlobalVariable;
import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_calendar extends Fragment {
    private Button confirm;
    private View v;
    private CalendarView calendarView;
    private GlobalVariable gv;

    public static Fragment_calendar newInstance(int index) {
        Fragment_calendar f = new Fragment_calendar();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);
        gv = ((GlobalVariable) getContext().getApplicationContext());
        initCalendarView();
        initButton();
        return v;
    }

    private void initCalendarView() {
        calendarView = v.findViewById(R.id.calendarView);
        gv.setCurrent_date(new Date());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                gv.setCurrent_date(calendar.getTime());
            }
        });
    }

    private void initButton() {
        confirm = v.findViewById(R.id.confirm_date);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = ((MainActivity) getContext());
                Fragment fragment_company = mainActivity.getSupportFragmentManager().findFragmentByTag("company");
                if (fragment_company == null) {
                    fragment_company = new Fragment_company();
                }
                //  Bundle bundle = new Bundle();
                //bundle.putString("date", gv.getCurrent_date());
                // fragment_company.setArguments(bundle);
                mainActivity.switchFrament(fragment_company, "company");
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            calendarView.setDate((long) gv.getCurrent_date(1));
        }
    }

}
