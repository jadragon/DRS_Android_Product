package com.example.alex.designateddriving_driver.RecyclerAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.designateddriving_driver.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyEvaluationRecyclerAdapter extends RecyclerView.Adapter<MyEvaluationRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private List<ContentValues> list;

    public MyEvaluationRecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
    }


    @Override
    public MyEvaluationRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_evaluation, parent, false);
        return new MyEvaluationRecyclerAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return 10;
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




        public RecycleHolder(View view) {
            super(view);
        }

        @Override
        public void onClick(View view) {
        }
    }


    public void setFilter(JSONObject json) {
    }

}