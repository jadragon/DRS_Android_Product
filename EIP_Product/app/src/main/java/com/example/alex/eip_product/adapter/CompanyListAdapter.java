package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.GlobalVariable;
import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.fragment.Fragment_inspect_content;

import java.util.ArrayList;
import java.util.Map;

import db.OrderDatabase;

import static db.OrderDatabase.KEY_CheckMan;
import static db.OrderDatabase.KEY_VendorName;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.RecycleHolder> {
    private ArrayList<Map<String, String>> list;
    private Context ctx;
    private GlobalVariable gv;

    /*
import static db.OrderDatabase.KEY_Area;
import static db.OrderDatabase.KEY_CheckMan;
import static db.OrderDatabase.KEY_Notes;
import static db.OrderDatabase.KEY_OrderComments;
import static db.OrderDatabase.KEY_OrderDetails;
import static db.OrderDatabase.KEY_OrderItemComments;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_Phone;
import static db.OrderDatabase.KEY_PlanCheckDate;
import static db.OrderDatabase.KEY_SalesMan;
import static db.OrderDatabase.KEY_Shipping;
import static db.OrderDatabase.KEY_VendorCode;
import static db.OrderDatabase.KEY_VendorName;
*/
    public CompanyListAdapter(Context ctx, String VendorCode) {
        this.ctx = ctx;
        gv = (GlobalVariable) ctx.getApplicationContext();
        OrderDatabase db = new OrderDatabase(ctx);
        list = db.getOrdersByDateAndVendorCode(gv.getCurrent_date(), VendorCode);
        db.close();
    }

    @Override
    public CompanyListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_companyitem, null);
        return new CompanyListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        holder.VendorName.setText(list.get(position).get(KEY_VendorName));
        if (list.get(position).get(KEY_CheckMan).equals(gv.getUsername())) {
            holder.CheckMan.setVisibility(View.VISIBLE);
            holder.CheckMan.setText(list.get(position).get(KEY_CheckMan));
        } else {
            holder.CheckMan.setVisibility(View.INVISIBLE);
        }
        /*
        if (list.get(position).get("company").equals("")) {
            holder.company.setVisibility(View.INVISIBLE);
        } else {
            holder.company.setVisibility(View.VISIBLE);
            holder.company.setText(list.get(position).get("company"));
        }
*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView VendorName, CheckMan;

        public RecycleHolder(View view) {
            super(view);
            VendorName = view.findViewWithTag("VendorName");
            CheckMan = view.findViewWithTag("CheckMan");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment_inspect_content = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("inspect_content");
            if (fragment_inspect_content == null) {//判斷Fragment_inspect_content是否已存在
                fragment_inspect_content = new Fragment_inspect_content();
            }
            /*
            Bundle bundle = new Bundle();
            bundle.putString("name", list.get(getAdapterPosition()).get(KEY_VendorName));
            bundle.putString("date", gv.getCurrent_date());
            ArrayList<String> arrayList = new ArrayList<>();
            for (Map<String, String> map : list) {
                arrayList.add(map.get(KEY_PONumber));
            }
            bundle.putStringArrayList(KEY_PONumber, arrayList);
            fragment_inspect_content.setArguments(bundle);
            */
            ((MainActivity) ctx).switchFrament(fragment_inspect_content, "inspect_content");
            // ctx.startActivity(new Intent(ctx, InsepectOrderActivity.class));
        }
    }

    public void setFilter(String VendorCode) {
        OrderDatabase db = new OrderDatabase(ctx);
        list = db.getOrdersByDateAndVendorCode(gv.getCurrent_date(), VendorCode);
        db.close();
        notifyDataSetChanged();
    }
}
