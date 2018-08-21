package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.AttendanceContentPojo;
import library.AnalyzeJSON.Analyze_PunchInfo;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<AttendanceContentPojo> list;
    Analyze_PunchInfo analyze_punchInfo;
    String[] classnum, commute;

    public AttendanceAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        analyze_punchInfo = new Analyze_PunchInfo();
        classnum = ctx.getResources().getStringArray(R.array.attendance_classnum);
        commute = ctx.getResources().getStringArray(R.array.attendance_commute);
        list = analyze_punchInfo.getAttendanceContent(json);
    }

    @Override
    public AttendanceAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punch, parent, false);
        return new AttendanceAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));
        holder.classnum.setText(classnum[list.get(position).getClassnum()]);
        holder.commute.setText(commute[list.get(position).getCommute()]);
        holder.note.setText(list.get(position).getNote());
        holder.puch_time.setText(list.get(position).getPuch_time());
        holder.name.setText(list.get(position).getName());
        holder.en.setText(list.get(position).getEn());
        holder.store.setText(list.get(position).getStore());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        TextView line, classnum, commute, note, puch_time, name, en, store;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("backfround");
            line = view.findViewWithTag("line");
            classnum = view.findViewWithTag("classnum");
            commute = view.findViewWithTag("commute");
            note = view.findViewWithTag("note");
            puch_time = view.findViewWithTag("puch_time");
            name = view.findViewWithTag("name");
            en = view.findViewWithTag("en");
            store = view.findViewWithTag("store");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }


    public void setFilter(JSONObject json) {
        list = analyze_punchInfo.getAttendanceContent(json);
        notifyDataSetChanged();

    }
}