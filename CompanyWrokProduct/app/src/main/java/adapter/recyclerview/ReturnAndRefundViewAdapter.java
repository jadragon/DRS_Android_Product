package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料.CameraActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.Item;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ReturnAndRefundContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ReturnAndRefundHeaderPojo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.AnalyzeJSON.AnalyzeOrderInfo;

public class ReturnAndRefundViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_HEADER = 0;
    public final int TYPE_CONTENT = 1;
    public final int TYPE_FOOTER = 2;
    private Context ctx;
    private List<Item> items;
    ReturnAndRefundHeaderPojo returnAndRefundHeaderPojo;
    ArrayList<ReturnAndRefundContentPojo> contentPojos;
    GlobalVariable gv;
    Bitmap[] photos;

    public ReturnAndRefundViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        photos = new Bitmap[6];
        gv = ((GlobalVariable) ctx.getApplicationContext());
        if (json != null) {
            returnAndRefundHeaderPojo = AnalyzeOrderInfo.getMOrderReturnHeader(json);
            contentPojos = AnalyzeOrderInfo.getMOrderReturnContent(json);
            initItems();
        } else {
            items = new ArrayList<>();
        }
    }

    public void initItems() {
        items = new ArrayList<>();
        Item item = returnAndRefundHeaderPojo;
        item.setType(TYPE_HEADER);
        items.add(item);
        for (int i = 0; i < contentPojos.size(); i++) {
            item = contentPojos.get(i);
            item.setType(TYPE_CONTENT);
            items.add(item);
        }
        //TYPE_FOOTER
        item = new Item();
        item.setType(TYPE_FOOTER);
        items.add(item);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_orderinfo_header, parent, false);
            return new ReturnAndRefundViewAdapter.HeaderHolder(ctx, view);
        }
        if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_return_and_refund_content, parent, false);
            return new ReturnAndRefundViewAdapter.ContentHolder(ctx, view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_return_and_refund_footer, parent, false);
        return new ReturnAndRefundViewAdapter.FooterHolder(ctx, view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_HEADER) {
                ((HeaderHolder) holder).ordernum.setText(((ReturnAndRefundHeaderPojo) items.get(position)).getOrdernum());
                ((HeaderHolder) holder).odate.setText(((ReturnAndRefundHeaderPojo) items.get(position)).getOdate());
                ((HeaderHolder) holder).sname.setText(((ReturnAndRefundHeaderPojo) items.get(position)).getSname());

            } else if (getItemViewType(position) == TYPE_CONTENT) {
                ImageLoader.getInstance().displayImage(((ReturnAndRefundContentPojo) items.get(position)).getPimg(), ((ContentHolder) holder).pimg);
                ((ContentHolder) holder).pname.setText(((ReturnAndRefundContentPojo) items.get(position)).getPname());
                ((ContentHolder) holder).format.setText("規格:" + ((ReturnAndRefundContentPojo) items.get(position)).getColor() + ((ReturnAndRefundContentPojo) items.get(position)).getSize());
                //   ((ContentHolder) holder).oiname.setText(((ReturnAndRefundContentPojo) items.get(position)).getOiname());
                ((ContentHolder) holder).price.setText("$" + ((ReturnAndRefundContentPojo) items.get(position)).getPrice());
                ((ContentHolder) holder).sprice.setText("$" + ((ReturnAndRefundContentPojo) items.get(position)).getSprice());
                ((ContentHolder) holder).stotal.setText("" + ((ReturnAndRefundContentPojo) items.get(position)).getStotal());
                ((ContentHolder) holder).checkbox.setChecked(((ReturnAndRefundContentPojo) items.get(position)).isCheck());
            } else if (getItemViewType(position) == TYPE_FOOTER) {
                if (photos[0] != null)
                    ((FooterHolder) holder).photo1.setImageBitmap(photos[0]);
                if (photos[1] != null)
                    ((FooterHolder) holder).photo2.setImageBitmap(photos[1]);
                if (photos[2] != null)
                    ((FooterHolder) holder).photo3.setImageBitmap(photos[2]);
                if (photos[3] != null)
                    ((FooterHolder) holder).photo4.setImageBitmap(photos[3]);
                if (photos[4] != null)
                    ((FooterHolder) holder).photo5.setImageBitmap(photos[4]);
                if (photos[5] != null)
                    ((FooterHolder) holder).photo6.setImageBitmap(photos[5]);
            }
        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
            int type = (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type) {
                case 0:
                    if (getItemViewType(position) == TYPE_HEADER) {

                    } else if (getItemViewType(position) == TYPE_CONTENT) {

                    }
                    break;
            }
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position).getType() == TYPE_HEADER)
            return TYPE_HEADER;
        if (items.get(position).getType() == TYPE_CONTENT)
            return TYPE_CONTENT;

        return TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView odate;
        TextView ordernum;
        TextView sname;

        public HeaderHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            odate = view.findViewById(R.id.orderinfo_header_odate);
            ordernum = view.findViewById(R.id.orderinfo_header_ordernum);
            sname = view.findViewById(R.id.orderinfo_header_sname);
        }

    }

    class ContentHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ImageView pimg;
        TextView pname, format, oiname, price, sprice, stotal;
        CheckBox checkbox;

        public ContentHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            pimg = view.findViewById(R.id.return_and_refund_pimg);
            pname = view.findViewById(R.id.return_and_refund_pname);
            format = view.findViewById(R.id.return_and_refund_format);
            oiname = view.findViewById(R.id.return_and_refund_oiname);
            price = view.findViewById(R.id.return_and_refund_price);
            sprice = view.findViewById(R.id.return_and_refund_sprice);
            stotal = view.findViewById(R.id.return_and_refund_stotal);
            checkbox = view.findViewById(R.id.return_and_refund_checkbox);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ReturnAndRefundContentPojo) items.get(getAdapterPosition())).setCheck(!((ReturnAndRefundContentPojo) items.get(getAdapterPosition())).isCheck());
                    for (Item item : items) {
                        if (item instanceof ReturnAndRefundContentPojo) {
                            Log.e("checkbox", getAdapterPosition() + "");
                        }
                    }
                }
            });
        }
    }


    class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context ctx;
        ImageView photo1, photo2, photo3, photo4, photo5, photo6;

        public FooterHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            photo1 = view.findViewById(R.id.return_and_refund_photo1);
            photo2 = view.findViewById(R.id.return_and_refund_photo2);
            photo3 = view.findViewById(R.id.return_and_refund_photo3);
            photo4 = view.findViewById(R.id.return_and_refund_photo4);
            photo5 = view.findViewById(R.id.return_and_refund_photo5);
            photo6 = view.findViewById(R.id.return_and_refund_photo6);
            photo1.setOnClickListener(this);
            photo2.setOnClickListener(this);
            photo3.setOnClickListener(this);
            photo4.setOnClickListener(this);
            photo5.setOnClickListener(this);
            photo6.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ctx, CameraActivity.class);
            intent.putExtra("Shape", "square");

            switch (view.getId()) {
                case R.id.return_and_refund_photo1:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 100);
                    break;
                case R.id.return_and_refund_photo2:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 200);
                    break;
                case R.id.return_and_refund_photo3:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 300);
                    break;
                case R.id.return_and_refund_photo4:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 400);
                    break;
                case R.id.return_and_refund_photo5:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 500);
                    break;
                case R.id.return_and_refund_photo6:
                    ((AppCompatActivity) ctx).startActivityForResult(intent, 600);
                    break;
            }

        }
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            returnAndRefundHeaderPojo = AnalyzeOrderInfo.getMOrderReturnHeader(json);
            contentPojos = AnalyzeOrderInfo.getMOrderReturnContent(json);
            initItems();
        } else {
            items = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void setPhotos(Bitmap bitmap, int index) {
        photos[index] = bitmap;
    }
}
