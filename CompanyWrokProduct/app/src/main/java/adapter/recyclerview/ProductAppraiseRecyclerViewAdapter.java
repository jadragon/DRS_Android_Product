package adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.商品.pojo.ProductCommentPojo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.AnalyzeJSON.AnalyzeProduct;

public class ProductAppraiseRecyclerViewAdapter extends RecyclerView.Adapter<ProductAppraiseRecyclerViewAdapter.BuyerHolder> {
    private Context ctx;
    GlobalVariable gv;
    ArrayList<ProductCommentPojo> arrayList;

    public ProductAppraiseRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {
            arrayList = new AnalyzeProduct().getProductCommentPojo(json);
        } else {
            arrayList = new ArrayList<>();
        }
    }

    @Override
    public ProductAppraiseRecyclerViewAdapter.BuyerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_productappraise, parent, false);
        return new ProductAppraiseRecyclerViewAdapter.BuyerHolder(ctx, view);


    }

    @Override
    public void onBindViewHolder(BuyerHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(BuyerHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            //     ((HeaderHolder) holder).odate.setText(((MemberOrderHeaderPojo) items.get(position)).getOdate());
            holder.mname.setText(arrayList.get(position).getMname());
            holder.comment.setText(arrayList.get(position).getComment());
            holder.comscore.setText(arrayList.get(position).getComscore()+"");
            holder.comdate.setText(arrayList.get(position).getComdate());
            ImageLoader.getInstance().displayImage(arrayList.get(position).getMimg(), holder.mimg);
            if (!arrayList.get(position).getRecomdate().equals("null")) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.recomment.setText(arrayList.get(position).getRecomment());
                holder.recomdate.setText(arrayList.get(position).getRecomdate());
            } else {
                holder.linearLayout.setVisibility(View.GONE);
            }
        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
            int type = (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type) {
                case 0:
                    break;
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }


    class BuyerHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ImageView mimg;
        TextView mname, comment, comscore, comdate, recomment, recomdate;
        LinearLayout linearLayout;

        public BuyerHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            mimg = view.findViewById(R.id.productappraise_mimg);
            linearLayout = view.findViewById(R.id.productappraise_seller_layout);
            mname = view.findViewById(R.id.productappraise_mname);
            comment = view.findViewById(R.id.productappraise_comment);
            comscore = view.findViewById(R.id.productappraise_comscore);
            comdate = view.findViewById(R.id.productappraise_comdate);
            recomment = view.findViewById(R.id.productappraise_recomment);
            recomdate = view.findViewById(R.id.productappraise_recomdate);
        }
    }


    public void setFilter(JSONObject json) {
        if (json != null) {
            arrayList = new AnalyzeProduct().getProductCommentPojo(json);
        } else {
            arrayList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }


}
