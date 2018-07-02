package adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.帳務管理.pojo.BillPojo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Util.StringUtil;
import library.AnalyzeJSON.AnalyzeBill;

public class PointRecyclerAdapter extends RecyclerView.Adapter<PointRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    JSONObject json;
    private List<BillPojo> itemPojoList;
    int type, point_type;

    public PointRecyclerAdapter(Context ctx, JSONObject json, int type, int point_type) {
        this.ctx = ctx;
        this.json = json;
        this.type = type;
        this.point_type = point_type;
        if (json != null) {
            itemPojoList = AnalyzeBill.getBillPojo(json);
        } else {
            itemPojoList = new ArrayList<>();
        }
    }


    @Override
    public PointRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_point, parent, false);
        return new PointRecyclerAdapter.RecycleHolder(ctx, view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        switch (point_type) {
            case 1:
                getPolk(holder);
                break;
            case 2:
                getKuva(holder, position);
                break;
            case 3:
                getAcoin(holder);
                break;
            case 4:
                getEwallet(holder,position);
                break;
            default:
                getPolk(holder);
                break;
        }
        holder.cdate.setText("" + itemPojoList.get(position).getCdate());
        holder.rate.setText("" + itemPojoList.get(position).getRate());
        if (itemPojoList.get(position).getIetype() == 1) {
            holder.iemoney.setTextColor(ctx.getResources().getColor(R.color.green));
            holder.iemoney.setText("+" + StringUtil.getDeciamlString(itemPojoList.get(position).getIemoney()));
        } else if (itemPojoList.get(position).getIetype() == 2) {
            holder.iemoney.setTextColor(ctx.getResources().getColor(R.color.red));
            holder.iemoney.setText("" + StringUtil.getDeciamlString(itemPojoList.get(position).getIemoney()));
        }

        holder.bmoney.setText("" + StringUtil.getDeciamlString(itemPojoList.get(position).getBmoney()));
        holder.amoney.setText("" + StringUtil.getDeciamlString(itemPojoList.get(position).getAmoney()));

    }


    @Override
    public int getItemCount() {
        return itemPojoList.size();
    }

    public void getEwallet(RecycleHolder holder,int position) {
        holder.btitle.setVisibility(View.GONE);
        switch (type) {
            case 1:
                holder.btitle.setVisibility(View.GONE);
                holder.type.setText("錢包消費");
                break;
            case 2:
                holder.btitle.setVisibility(View.VISIBLE);
                if (itemPojoList.get(position).getBstatus() == 0) {
                } else if (itemPojoList.get(position).getBstatus() == 1) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                } else if (itemPojoList.get(position).getBstatus() == 2) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                }
                holder.btitle.setText(itemPojoList.get(position).getBtitle());
                holder.type.setText("錢包儲值");
                break;
            case 3:
                holder.btitle.setVisibility(View.VISIBLE);
                if (itemPojoList.get(position).getBstatus() == 0) {
                } else if (itemPojoList.get(position).getBstatus() == 1) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                } else if (itemPojoList.get(position).getBstatus() == 2) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                }
                holder.btitle.setText(itemPojoList.get(position).getBtitle());
                holder.type.setText("錢包提領");
                break;
        }
    }
    public void getKuva(RecycleHolder holder, int position) {

        switch (type) {
            case 1:
                holder.btitle.setVisibility(View.GONE);
                holder.type.setText("庫瓦消費");
                break;
            case 2:
                holder.btitle.setVisibility(View.GONE);
                holder.type.setText("庫瓦回饋");
                break;
            case 3:
                holder.btitle.setVisibility(View.VISIBLE);
                if (itemPojoList.get(position).getBstatus() == 0) {
                } else if (itemPojoList.get(position).getBstatus() == 1) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                } else if (itemPojoList.get(position).getBstatus() == 2) {
                    holder.btitle.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                }
                holder.btitle.setText(itemPojoList.get(position).getBtitle());
                holder.type.setText("庫瓦:雙閃");
                break;
        }
    }

    public void getPolk(RecycleHolder holder) {
        holder.btitle.setVisibility(View.GONE);
        switch (type) {
            case 1:
                holder.type.setText("波克消費");
                break;
            case 2:
                holder.type.setText("波克回饋");
                break;
            case 3:
                holder.type.setText("波克:庫瓦");
                break;
        }
    }

    public void getAcoin(RecycleHolder holder) {
        holder.btitle.setVisibility(View.GONE);
        switch (type) {
            case 1:
                holder.type.setText("雙閃幣:庫瓦幣");
                break;
            case 2:
                holder.type.setText("波克回饋");
                break;
            case 3:
                holder.type.setText("波克:庫瓦");
                break;
        }
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView btitle, cdate, rate, iemoney, bmoney, amoney, type;

        Context context;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.context = ctx;
            btitle = view.findViewById(R.id.viewitem_point_btitle);
            type = view.findViewById(R.id.viewitem_point_type);
            cdate = view.findViewById(R.id.viewitem_point_cdate);
            rate = view.findViewById(R.id.viewitem_point_rate);
            iemoney = view.findViewById(R.id.viewitem_point_iemoney);
            bmoney = view.findViewById(R.id.viewitem_point_bmoney);
            amoney = view.findViewById(R.id.viewitem_point_amoney);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

        }
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        if (json != null) {

            itemPojoList = AnalyzeBill.getBillPojo(json);

        } else {
            itemPojoList = new ArrayList<>();
        }
        notifyDataSetChanged();

    }

}