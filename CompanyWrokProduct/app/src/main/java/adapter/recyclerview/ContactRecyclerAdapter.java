package adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽.ReplyActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeContact;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private ContactRecyclerAdapter.ClickListener clickListener;
    private List<ItemPojo> itemPojoList;
    private Bitmap bitmap;

    public ContactRecyclerAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeContact.getContact(json);
        } else {
            list = new ArrayList<>();
        }
        initItems(list);
    }

    public ContactRecyclerAdapter(Context ctx, JSONObject json, Bitmap bitmap) {
        this.ctx = ctx;
        this.bitmap = bitmap;
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeContact.getContact(json);
        } else {
            list = new ArrayList<>();
        }
        initItems(list);
    }

    private void initItems(ArrayList<Map<String, String>> list) {
        itemPojoList = new ArrayList<>();
        ItemPojo itemPojo;
        for (int i = 0; i < list.size(); i++) {
            itemPojo = new ItemPojo();
            itemPojo.setMsno(list.get(i).get("msno"));
            itemPojo.setType(list.get(i).get("type"));
            itemPojo.setPerson(list.get(i).get("person"));
            itemPojo.setTitle(list.get(i).get("title"));
            itemPojo.setIsread(list.get(i).get("isread"));
            itemPojo.setContent(list.get(i).get("content"));
            itemPojo.setTime(list.get(i).get("time"));
            itemPojo.setCheck(false);
            itemPojoList.add(itemPojo);
        }
    }

    @Override
    public ContactRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_contact, parent, false);
        if (bitmap != null)
            ((ImageView) view.findViewById(R.id.head_portrait)).setImageBitmap(bitmap);
        return new ContactRecyclerAdapter.RecycleHolder(ctx, view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (itemPojoList.get(position).isCheck())
            holder.viewitem_contact_checkbox.setChecked(true);
        else
            holder.viewitem_contact_checkbox.setChecked(false);
        holder.viewitem_contact_person.setText(itemPojoList.get(position).getPerson());
        holder.viewitem_contact_title.setText(itemPojoList.get(position).getTitle());
        holder.viewitem_contact_time.setText(itemPojoList.get(position).getTime());


    }


    @Override
    public int getItemCount() {
        return itemPojoList.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView viewitem_contact_person, viewitem_contact_title, viewitem_contact_time;
        CheckBox viewitem_contact_checkbox;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.context = ctx;
            viewitem_contact_person = view.findViewById(R.id.viewitem_contact_person);
            viewitem_contact_title = view.findViewById(R.id.viewitem_contact_title);
            viewitem_contact_time = view.findViewById(R.id.viewitem_contact_time);
            viewitem_contact_checkbox = view.findViewById(R.id.viewitem_contact_checkbox);
            viewitem_contact_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (itemPojoList.get(position).isCheck()) {
                        itemPojoList.get(position).setCheck(false);
                        ((CheckBox) ((ViewGroup) view.getParent().getParent().getParent()).findViewById(R.id.contact_allcheck)).setChecked(false);
                    } else {
                        itemPojoList.get(position).setCheck(true);
                        checkAll(view);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("msno", itemPojoList.get(getAdapterPosition()).getMsno());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, itemPojoList);
            }
        }
    }

    public void setClickListener(ContactRecyclerAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, List<ItemPojo> itemPojoList);
    }

    public void setFilter(JSONObject json) {
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeContact.getContact(json);

        } else {
            list = new ArrayList<>();
        }
        initItems(list);
        notifyDataSetChanged();

    }

    public void checkAll(View view) {
        for (ItemPojo itemPojo : itemPojoList) {
            if (!itemPojo.isCheck()) {
                return;
            }
        }
        ((CheckBox) ((ViewGroup) view.getParent().getParent().getParent()).findViewById(R.id.contact_allcheck)).setChecked(true);
        notifyDataSetChanged();
    }

    public String getCheckedList() {

        StringBuilder builder = new StringBuilder();

        for (ItemPojo itemPojo : itemPojoList) {
            if (itemPojo.isCheck())
                builder.append("," + itemPojo.getMsno());
        }
        return builder.delete(0, 1).toString();
    }

    public void setAllChecked() {
        for (ItemPojo itemPojo : itemPojoList)
            itemPojo.setCheck(true);
        notifyDataSetChanged();
    }

    public void setAllNotCheck() {
        for (ItemPojo itemPojo : itemPojoList)
            itemPojo.setCheck(false);
        notifyDataSetChanged();
    }

    private class ItemPojo {
        private String msno;
        private String type;
        private String person;
        private String title;
        private String isread;
        private String content;
        private String time;
        boolean isCheck;

        public String getMsno() {
            return msno;
        }

        public void setMsno(String msno) {
            this.msno = msno;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsread() {
            return isread;
        }

        public void setIsread(String isread) {
            this.isread = isread;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}