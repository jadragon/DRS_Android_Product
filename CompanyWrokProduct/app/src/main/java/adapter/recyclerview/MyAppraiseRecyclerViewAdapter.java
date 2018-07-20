package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.ReplyAppreciateActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MyCommentitemPojo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.AnalyzeJSON.AnalyzeComment;

public class MyAppraiseRecyclerViewAdapter extends RecyclerView.Adapter<MyAppraiseRecyclerViewAdapter.BuyerHolder> {
    private Context ctx;
    GlobalVariable gv;
    ArrayList<MyCommentitemPojo> itemList;

    int type;

    public void setType(int type) {
        this.type = type;
    }

    public MyAppraiseRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {
            itemList = new AnalyzeComment().getMyCommentitemPojo(json);
        } else {
            itemList = new ArrayList<>();
        }
    }

    @Override
    public BuyerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_myappraise, parent, false);
        return new BuyerHolder(ctx, view);


    }

    @Override
    public void onBindViewHolder(BuyerHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(BuyerHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            //     ((HeaderHolder) holder).odate.setText(((MemberOrderHeaderPojo) items.get(position)).getOdate());

            ImageLoader.getInstance().displayImage(itemList.get(position).getImg(), holder.img);

            holder.sname.setText(itemList.get(position).getSname());
            holder.comscore.setText(itemList.get(position).getComscore() + "");
            holder.pname.setText(itemList.get(position).getPname());
            holder.format.setText("規格:" + itemList.get(position).getColor() + " " + itemList.get(position).getSize());
            holder.comment.setText(itemList.get(position).getComment());
            holder.comdate.setText(itemList.get(position).getComdate());
            holder.sname.setText(itemList.get(position).getSname());
            if (!itemList.get(position).getRecomdate().equals("")) {
                holder.myappraise_btn_recomment.setVisibility(View.INVISIBLE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.recomment.setText(itemList.get(position).getRecomment());
                holder.recomdate.setText(itemList.get(position).getRecomdate());
            } else {
                holder.myappraise_btn_recomment.setVisibility(View.VISIBLE);
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
        return itemList.size();

    }


    class BuyerHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ImageView img;
        TextView sname, pname, format, comment, comscore, comdate, recomment, recomdate;
        LinearLayout linearLayout;
        Button myappraise_btn_recomment;

        public BuyerHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            img = view.findViewById(R.id.myappraise_img);
            linearLayout = view.findViewById(R.id.myappraise_recomment_layout);
            sname = view.findViewById(R.id.myappraise_sname);
            pname = view.findViewById(R.id.myappraise_pname);
            format = view.findViewById(R.id.myappraise_format);
            comment = view.findViewById(R.id.myappraise_comment);
            comscore = view.findViewById(R.id.myappraise_comscore);
            comdate = view.findViewById(R.id.myappraise_comdate);
            recomment = view.findViewById(R.id.myappraise_recomment);
            recomdate = view.findViewById(R.id.myappraise_recomdate);
            myappraise_btn_recomment = view.findViewById(R.id.myappraise_btn_recomment);
            myappraise_btn_recomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyCommentitemPojo myCommentitemPojo = itemList.get(getAdapterPosition());
                    Intent intent = new Intent(ctx, ReplyAppreciateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("myCommentitemPojo", myCommentitemPojo);
                    intent.putExtras(bundle);
                    intent.putExtra("type",type);
                    ((AppCompatActivity) ctx).startActivityForResult(intent, myCommentitemPojo.getComscore());
                }
            });
        }
    }



    public void setFilter(JSONObject json) {
        if (json != null) {
            itemList = new AnalyzeComment().getMyCommentitemPojo(json);
        } else {
            itemList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
