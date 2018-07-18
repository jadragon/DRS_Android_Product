package adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.帳務管理.現金折價券.CouponActivity;
import com.test.tw.wrokproduct.帳務管理.現金折價券.pojo.CouponPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.AnalyzeJSON.AnalyzeBill;
import library.Component.ToastMessageDialog;
import library.GetJsonData.BillJsonData;
import library.JsonDataThread;

public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    JSONObject json;
    private List<CouponPojo> itemPojoList;
    private ToastMessageDialog toastMessageDialog;
    GlobalVariable gv;

    public CouponRecyclerAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;
        gv = (GlobalVariable) ctx.getApplicationContext();
        toastMessageDialog = new ToastMessageDialog(ctx);
        if (json != null) {
            itemPojoList = new AnalyzeBill().getCouponPojo(json);
        } else {
            itemPojoList = new ArrayList<>();
        }
    }


    @Override
    public CouponRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_coupon, parent, false);
        return new CouponRecyclerAdapter.RecycleHolder(ctx, view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (itemPojoList.get(position).getIsuse() == 0)
            holder.image.setImageResource(R.drawable.coupon_yellow);
        else if (itemPojoList.get(position).getIsuse() == 1) {
            holder.image.setImageResource(R.drawable.coupon_gray);
        }
        holder.coupon.setText(itemPojoList.get(position).getCoupon());
        holder.money.setText("$" + itemPojoList.get(position).getMoney());
        holder.cost.setText("最低消費金額:$" + itemPojoList.get(position).getCost());
        holder.edate.setText("有效至" + itemPojoList.get(position).getEdate());
    }


    @Override
    public int getItemCount() {
        return itemPojoList.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView coupon, money, cost, edate;
        ImageView image;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.context = ctx;
            coupon = view.findViewById(R.id.viewitem_coupon_coupon);
            money = view.findViewById(R.id.viewitem_coupon_money);
            cost = view.findViewById(R.id.viewitem_coupon_cost);
            edate = view.findViewById(R.id.viewitem_coupon_edate);
            image = view.findViewById(R.id.viewitem_coupon_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (itemPojoList.get(position).getIsuse() == 0) {
                copyToClipboard(itemPojoList.get(position).getCoupon());
            } else if (itemPojoList.get(position).getIsuse() == 1) {
                toastMessageDialog.setTitleText("是否要刪除此折價券?");
                toastMessageDialog.showCheck(false,new ToastMessageDialog.ClickListener() {
                    @Override
                    public void ItemClicked(Dialog dialog, View view, String note) {
                        new JsonDataThread() {
                            @Override
                            public JSONObject getJsonData() {
                                return new BillJsonData().delCoupon(gv.getToken(), itemPojoList.get(position).getMcno());
                            }

                            @Override
                            public void runUiThread(JSONObject json) {
                                try {
                                    toastMessageDialog.setTitleText("注意");
                                    toastMessageDialog.setMessageText(json.getString("Message"));
                                    toastMessageDialog.show();
                                    ((CouponActivity) ctx).setFilter();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }.start();
                        dialog.dismiss();
                    }

                });
            }
        }
    }


    public void setFilter(JSONObject json) {
        this.json = json;
        if (json != null) {
            itemPojoList = new AnalyzeBill().getCouponPojo(json);
        } else {
            itemPojoList = new ArrayList<>();
        }
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
        toastMessageDialog.setTitleText("注意");
        toastMessageDialog.setMessageText("已複製完成");
        toastMessageDialog.show();
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