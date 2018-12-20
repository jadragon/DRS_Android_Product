package tw.com.lccnet.app.designateddriving.RecyclerAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.CouponActivity;
import tw.com.lccnet.app.designateddriving.GlobalVariable;
import tw.com.lccnet.app.designateddriving.R;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;

public class GetCouponRecyclerAdapter extends RecyclerView.Adapter<GetCouponRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private List<ContentValues> list;
    private SimpleDateFormat dateParse, dateFormat;
    private GlobalVariable gv;

    public GetCouponRecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
        dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        gv = (GlobalVariable) ctx.getApplicationContext();
    }


    @Override
    public GetCouponRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_coupon, parent, false);
        return new GetCouponRecyclerAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        holder.name.setText(list.get(position).getAsString("name"));
        holder.money.setText("＄" + list.get(position).getAsString("money"));
        try {
            holder.edate.setText("到期日期:" + dateFormat.format(dateParse.parse(list.get(position).getAsString("edate"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tag.setText("取得優惠券");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, money, edate, tag;


        public RecycleHolder(View view) {
            super(view);
            name = view.findViewById(R.id.viewitem_coupon_name);
            money = view.findViewById(R.id.viewitem_coupon_money);
            edate = view.findViewById(R.id.viewitem_coupon_edate);
            tag = view.findViewById(R.id.viewitem_coupon_tag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return CustomerApi.setCoupon("OAQiVfqwAHys1cCskXTF9w==", list.get(position).getAsString("mmno"));
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        ((CouponActivity) ctx).setFilter();
                    } else {
                        Toast.makeText(ctx, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    public void setFilter(JSONObject json) {
        list = AnalyzeCustomer.getMyCoupon(json, 1);
        notifyDataSetChanged();
    }

}