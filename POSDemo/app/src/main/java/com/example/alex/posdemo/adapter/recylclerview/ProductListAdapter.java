package com.example.alex.posdemo.adapter.recylclerview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import Utils.StringUtil;
import library.AnalyzeJSON.APIpojo.ProductListPojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_CountInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.CountApi;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.RecycleHolder> {
    public static byte TYPE_SELL = 0;
    public static byte TYPE_RETURN = 1;
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<ProductListPojo> list;
    Analyze_CountInfo analyze_countInfo;
    private byte type;

    public ProductListAdapter(Context ctx, JSONObject json, byte type) {
        this.ctx = ctx;
        this.type = type;
        dm = ctx.getResources().getDisplayMetrics();
        analyze_countInfo = new Analyze_CountInfo();
        list = analyze_countInfo.getSearch_Member_Order(json);
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public ProductListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_count_productlist, parent, false);
        return new ProductListAdapter.RecycleHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));
        holder.pname.setText(list.get(position).getPname());
        holder.pcode.setText(list.get(position).getPcode());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.fprice.setText(StringUtil.getDeciamlString(list.get(position).getFprice()));
        holder.price.setText(StringUtil.getDeciamlString(list.get(position).getPrice()));
        holder.samount.setSelection(list.get(position).getCurrentCount());
        holder.sum.setText(StringUtil.getDeciamlString(list.get(position).getPrice() * holder.samount.getSelectedItemPosition()));

        // holder.note.setText(list.get(position).getNote());

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        TextView delete, line, pname, pcode, color, size, fprice, price, sum, note;
        Spinner samount;

        public RecycleHolder(View view, int index) {
            super(view);
            background = view.findViewWithTag("background");
            delete = view.findViewWithTag("delete");
            line = view.findViewWithTag("line");
            pname = view.findViewWithTag("pname");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            fprice = view.findViewWithTag("fprice");
            price = view.findViewWithTag("price");
            samount = view.findViewWithTag("samount");
            sum = view.findViewWithTag("sum");
            note = view.findViewWithTag("note");
            itemView.setOnClickListener(this);
            //
            if (type == TYPE_RETURN) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(0);
                for (int i = 1; i < list.get(index).getSamount(); i++) {
                    arrayList.add(i);
                }
                samount.setAdapter(new ArrayAdapter(ctx, R.layout.item_spinner, arrayList));
                samount.setSelection(arrayList.size() - 1);
                list.get(index).setCurrentCount(arrayList.size() - 1);
            } else if (type == TYPE_SELL) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(0);
                for (int i = 1; i < list.get(index).getTotal(); i++) {
                    arrayList.add(i);
                }
                samount.setAdapter(new ArrayAdapter(ctx, R.layout.item_spinner, arrayList));
                samount.setSelection(1);
                list.get(index).setCurrentCount(1);
            }

            //samount
            samount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sum.setText(StringUtil.getDeciamlString(list.get(getAdapterPosition()).getPrice() * position));
                    list.get(getAdapterPosition()).setCurrentCount(position);
                    showTotalCount();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //delete
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm("確定要刪除此商品?", new ToastMessageDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm(AlertDialog alertDialog) {
                            list.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            showTotalCount();
                        }
                    });
                }
            });
            //note
            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == TYPE_RETURN) {
                        new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_EDIT).sendApi(list.get(getAdapterPosition()).getNote(), new ToastMessageDialog.OnSendApiListener() {
                            @Override
                            public void onConfirm(AlertDialog alertDialog, final String note) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new CountApi().member_order_note(list.get(getAdapterPosition()).getMoi_no(), note);
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            list.get(getAdapterPosition()).setNote(note);
                                            new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                                        } else {
                                            new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_ERROR).confirm(AnalyzeUtil.getMessage(jsonObject));
                                        }

                                    }
                                });
                            }
                        });
                    } else if (type == TYPE_SELL) {
                        new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_EDIT).confirm("",null);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject json) {
        list = analyze_countInfo.getSearch_Member_Order(json);
        notifyDataSetChanged();
        showTotalCount();
    }

    public void addItem(JSONObject json) {
        ArrayList<ProductListPojo> arrayList = analyze_countInfo.getProduct_Item(json);
        if (arrayList.size() > 0) {
            list.addAll(analyze_countInfo.getProduct_Item(json));
            notifyDataSetChanged();
        } else {
            new ToastMessageDialog(ctx, TYPE_SELL).confirm("查無商品");
        }
        showTotalCount();
    }

    private void showTotalCount() {
        int total_price = 0;
        for (ProductListPojo productListPojo : list) {
            total_price += (productListPojo.getPrice() * productListPojo.getCurrentCount());
        }
        ((TextView) ((Activity) ctx).findViewById(R.id.count_txt_total)).setText(StringUtil.getDeciamlString(total_price));
    }
}