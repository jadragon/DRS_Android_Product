package tw.com.lccnet.app.designateddriving.RecyclerAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.GlobalVariable;
import tw.com.lccnet.app.designateddriving.R;

public class MyCouponRecyclerAdapter extends RecyclerView.Adapter<MyCouponRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private List<ContentValues> list;
    private SimpleDateFormat dateParse,dateFormat;
    private GlobalVariable gv;

    public MyCouponRecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
        dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        gv = (GlobalVariable) ctx.getApplicationContext();
    }


    @Override
    public MyCouponRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_coupon, parent, false);
        return new MyCouponRecyclerAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (list.get(position).getAsBoolean("isuse")) {
            holder.image.setImageResource(R.drawable.coupon_gray);
            holder.tag.setText("折扣碼已使用");
            holder.tag.setBackgroundColor(ctx.getResources().getColor(R.color.gray2));
        } else {
            holder.image.setImageResource(R.drawable.coupon_yellow);
            holder.tag.setText("複製折扣代碼");
            holder.tag.setBackgroundColor(ctx.getResources().getColor(R.color.orange2));
        }

        holder.name.setText(list.get(position).getAsString("name"));
        holder.money.setText("＄" + list.get(position).getAsString("money"));
        holder.coupon.setText(list.get(position).getAsString("coupon"));
        try {
            holder.edate.setText("到期日期:" + dateFormat.format(dateParse.parse(list.get(position).getAsString("edate"))));
            holder.utime.setText("使用日期:" +  dateFormat.format(dateParse.parse(list.get(position).getAsString("utime"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, money, edate, coupon, utime, tag;
        ImageView image;

        public RecycleHolder(View view) {
            super(view);
            image = view.findViewById(R.id.viewitem_coupon_image);
            name = view.findViewById(R.id.viewitem_coupon_name);
            money = view.findViewById(R.id.viewitem_coupon_money);
            edate = view.findViewById(R.id.viewitem_coupon_edate);
            coupon = view.findViewById(R.id.viewitem_coupon_coupon);
            utime = view.findViewById(R.id.viewitem_coupon_utime);
            tag = view.findViewById(R.id.viewitem_coupon_tag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            if(!list.get(position).getAsBoolean("isuse")) {
                copyToClipboard(list.get(position).getAsString("coupon"));
                Toast.makeText(ctx, "複製成功", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void setFilter(JSONObject json) {
        list = AnalyzeCustomer.getMyCoupon(json, 0);
        notifyDataSetChanged();
    }

    private void copyToClipboard(String str) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", str);
            clipboard.setPrimaryClip(clip);
        }
    }

    private String copyFromClipboard() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < clipboard.getPrimaryClip().getItemCount(); i++) {
                sb.append(clipboard.getPrimaryClip().getItemAt(i).getText());
            }
            return sb.toString();
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString();
        }
    }
}