package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import java.util.ArrayList;

import pojo.HotkeywordsPojo;


/**
 * Created by user on 2017/7/28.
 */

public class RecyclerAdapterBitmap extends RecyclerView.Adapter<RecyclerAdapterBitmap.RecycleHolder> {
    private ArrayList<HotkeywordsPojo> arrayList;
    private Context ctx;
    int type;
    int layout_width, layout_heigh;

    public RecyclerAdapterBitmap(Context ctx, ArrayList<HotkeywordsPojo> arrayList, int layout_width, int layout_heigh,int type) {
        this.arrayList = arrayList;
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.type = type;
    }


    @Override
    public RecyclerAdapterBitmap.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //頁面
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_home, parent, false);
        RecyclerAdapterBitmap.RecycleHolder recycleHolder = new RecyclerAdapterBitmap.RecycleHolder(ctx, view, arrayList);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterBitmap.RecycleHolder holder, int position) {
        //DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        //int real_heigh = (int)((displayMetrics.widthPixels-35)/(float)3.5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layout_width, layout_heigh);
        if(type==0){
            layoutParams.setMargins(5, 0, 5, 0);
        }else if(type==1){
            layoutParams.setMargins(0, 0, 0, 0);
            holder.tv1.setTextColor(ctx.getResources().getColor(R.color.colorBlack));
            holder.tv1.setBackgroundColor(ctx.getResources().getColor(R.color.colorInvisible));
        }
        holder.frameLayout.setLayoutParams(layoutParams);
        HotkeywordsPojo hotkeywordsPojo = arrayList.get(position);
        holder.imageView.setImageBitmap(hotkeywordsPojo.getImage());
        holder.tv1.setText(hotkeywordsPojo.getTitle());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        FrameLayout frameLayout;
        TextView tv1;
        Context ctx;
        ArrayList<HotkeywordsPojo> contacts;

        public RecycleHolder(Context ctx, View view, ArrayList<HotkeywordsPojo> dp) {
            super(view);
            this.contacts = dp;
            this.ctx = ctx;
            frameLayout = view.findViewById(R.id.frame_layout);
            imageView = (ImageView) view.findViewById(R.id.imView);
            tv1 = (TextView) view.findViewById(R.id.tV1);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(ctx, contacts.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }

        /*
        public void setFilter(ArrayList<ProductPojo> newList) {
            arrayList = new ArrayList<>();
            arrayList.addAll(newList);
            notifyDataSetChanged();

        }
        */
    }
}
