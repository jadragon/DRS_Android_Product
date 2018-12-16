package tw.com.lccnet.app.designateddriving.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.com.lccnet.app.designateddriving.R;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<Map<String, String>> list;
    private static final int TYPE_ME = 0x01;
    private static final int TYPE_YOU = 0x02;

    public MessageRecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
        Map<String, String> map;
        for (int i = 0; i < 20; i++) {
            map = new HashMap<>();
            map.put("type", (i % 2 + 1) + "");
            map.put("msg", i + "");
            map.put("cdate", i + "");
            list.add(map);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).get("type").equals("2")) {
            return TYPE_ME;
        } else if (list.get(position).get("type").equals("1")) {
            return TYPE_YOU;
        }
        return 0;
    }

    @Override
    public MessageRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_me, parent, false);
        } else if (viewType == TYPE_YOU) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_you, parent, false);
        }
        return new MessageRecyclerAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (list.get(position).get("type").equals("2")) {
            holder.image.setImageResource(R.drawable.menu_header1);
        } else {
            holder.image.setImageResource(R.drawable.menu_item5);
        }
        holder.msg.setText(list.get(position).get("msg"));
        holder.cdate.setText(list.get(position).get("cdate"));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView msg, cdate;


        public RecycleHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            msg = view.findViewById(R.id.msg);
            cdate = view.findViewById(R.id.cdate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
        }
    }


    public void setFilter(JSONObject json) {
        notifyDataSetChanged();
    }

}