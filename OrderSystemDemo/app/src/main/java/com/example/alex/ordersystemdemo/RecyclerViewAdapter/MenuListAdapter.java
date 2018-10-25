package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.API.Analyze.Analyze_Restaurant;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.MenuPojo;
import com.example.alex.ordersystemdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.RecycleHolder> {
    private static int CONTENT = 0;
    private static int FOOTER = 1;
    private Context ctx;
    private DisplayMetrics dm;
    private ArrayList<MenuPojo> list;
    private TextView totalmoney;
    private Map<String, String> map;

    public MenuListAdapter(Context ctx, JSONObject jsonObject) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        totalmoney = ((Activity) ctx).findViewById(R.id.total_money);
        map = new HashMap<>();
        map.put("name", "");
        map.put("phone", "");
        map.put("address", "");


        if (jsonObject != null) {
            list = new Analyze_Restaurant().getMenu(jsonObject);
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size())
            return FOOTER;
        return CONTENT;
    }

    @Override
    public MenuListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menulist, parent, false);
        } else if (viewType == FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menulist_footer, parent, false);
        }
        return new MenuListAdapter.RecycleHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        if (getItemViewType(position) == CONTENT) {
            holder.food.setText(list.get(position).getFood());
            holder.money.setText(list.get(position).getMoney() + "");
            holder.number.setText(list.get(position).getNumber() + "");
        } else if (getItemViewType(position) == FOOTER) {

        }

    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView food, money;
        EditText number;
        EditText student_name, student_phone, student_address;


        public RecycleHolder(View view, int viewType) {
            super(view);
            if (viewType == CONTENT) {
                food = view.findViewWithTag("food");
                money = view.findViewWithTag("money");
                number = view.findViewWithTag("number");
                number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals("")) {
                            list.get(getAdapterPosition()).setNumber(0);
                            totalmoney.setText(getTotalMoney() + "");
                        } else {
                            list.get(getAdapterPosition()).setNumber(Integer.parseInt(s.toString()));
                            totalmoney.setText(getTotalMoney() + "");
                        }
                    }
                });
            } else if (viewType == FOOTER) {
                student_name = view.findViewWithTag("student_name");
                student_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        map.put("name", s.toString());
                    }
                });
                student_phone = view.findViewWithTag("student_phone");
                student_phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        map.put("phone", s.toString());
                    }
                });
                student_address = view.findViewWithTag("student_address");
                student_address.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        map.put("address", s.toString());
                    }
                });
            }
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject jsonObject) {
        if (jsonObject != null) {
            list = new Analyze_Restaurant().getMenu(jsonObject);
        } else {
            list = new ArrayList<>();
        }
        notifyDataSetChanged();

    }

    public int getTotalMoney() {
        int totalmoney = 0;
        for (MenuPojo menuPojo : list) {
            totalmoney += menuPojo.getNumber() * menuPojo.getMoney();
        }
        return totalmoney;
    }

    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (MenuPojo menuPojo : list) {
            if (menuPojo.getNumber() > 0) {
                builder.append("," + menuPojo.getFood() + "*" + menuPojo.getNumber());
            }
        }

        if (builder.length() > 0) {
            builder.replace(0, 1, "");
        }
        return builder.toString();
    }

    public Map<String, String> getStudentInfo() {
        return map;
    }
}
