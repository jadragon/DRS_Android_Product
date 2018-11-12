package adapter.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料.CameraActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeContact;
import library.GetJsonData.ContactJsonData;
import library.SQLiteDatabaseHandler;

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private  JSONObject json;
    private  String msno;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_FOOTER = 2;
    private ReplyRecyclerAdapter.ClickListener clickListener;
    private List<ItemPojo> itemPojoList;
    private DisplayMetrics dm;
    private Bitmap myphoto;
    private  String myname;
    private  Bitmap[] photos;
    private GlobalVariable gv;
    public void setMsno(String msno) {
        this.msno = msno;
    }

    public ReplyRecyclerAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        this.json = json;
        dm = ctx.getResources().getDisplayMetrics();
        gv = ((GlobalVariable) ctx.getApplicationContext());
        //DB
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ctx);
        myname = db.getMemberDetail().get("name");
        byte[] bytes = db.getPhotoImage();
        myphoto = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        db.close();
        //JSON
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeContact.getContactCont(json);
        } else {
            list = new ArrayList<>();
        }
        initItems(list);
        photos = new Bitmap[6];
    }

    private void initItems(ArrayList<Map<String, String>> list) {
        itemPojoList = new ArrayList<>();
        ItemPojo itemPojo;
        for (int i = 0; i < list.size(); i++) {
            itemPojo = new ItemPojo();
            itemPojo.setType(list.get(i).get("type"));
            itemPojo.setPerson(list.get(i).get("person"));
            itemPojo.setIsread(list.get(i).get("isread"));
            itemPojo.setContent(list.get(i).get("content"));
            itemPojo.setTime(list.get(i).get("time"));
            itemPojo.setCheck(false);
            itemPojoList.add(itemPojo);
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public ReplyRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins((int) (20 * dm.density), (int) (10 * dm.density), (int) (20 * dm.density), (int) (10 * dm.density));
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            return new ReplyRecyclerAdapter.RecycleHolder(ctx, textView, TYPE_HEADER);
        }
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_reply_keyin, parent, false);
            return new ReplyRecyclerAdapter.RecycleHolder(ctx, view, TYPE_FOOTER);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_reply, parent, false);
        return new ReplyRecyclerAdapter.RecycleHolder(ctx, view, TYPE_CONTENT);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            try {
                holder.title.setText(json.getJSONObject("Data").getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (getItemViewType(position) == TYPE_CONTENT) {
            position--;
            holder.viewitem_reply_image.setImageBitmap(myphoto);
            holder.viewitem_reply_person.setText(itemPojoList.get(position).getPerson());
            holder.viewitem_reply_time.setText(itemPojoList.get(position).getTime());
            holder.viewitem_reply_content.setText(itemPojoList.get(position).getContent());
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            holder.viewitem_reply_keyin_image.setImageBitmap(myphoto);
            holder.viewitem_reply_keyin_person.setText(myname);
            int i = 0;
            for (Bitmap bitmap : photos) {
                if (bitmap != null) {
                    holder.photoImages.get(i).setImageBitmap(photos[i]);
                }
                i++;
            }


        }

    }


    @Override
    public int getItemCount() {
        return itemPojoList.size() + 2;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView viewitem_reply_person, viewitem_reply_time, viewitem_reply_content, viewitem_reply_keyin_person;
        ImageView viewitem_reply_image, viewitem_reply_isread, viewitem_reply_keyin_image;
        TextView title;
        EditText viewitem_reply_keyin_content;
        Button viewitem_reply_keyin_confirm;
        ArrayList<ImageView> photoImages;

        public RecycleHolder(Context ctx, View view, int viewType) {
            super(view);
            this.context = ctx;
            if (viewType == TYPE_HEADER) {
                title = (TextView) view;
            } else if (viewType == TYPE_CONTENT) {
                viewitem_reply_image = view.findViewById(R.id.viewitem_reply_image);
                viewitem_reply_person = view.findViewById(R.id.viewitem_reply_person);
                viewitem_reply_time = view.findViewById(R.id.viewitem_reply_time);
                viewitem_reply_content = view.findViewById(R.id.viewitem_reply_content);
            } else if (viewType == TYPE_FOOTER) {
                viewitem_reply_keyin_image = view.findViewById(R.id.viewitem_reply_keyin_image);
                viewitem_reply_keyin_person = view.findViewById(R.id.viewitem_reply_keyin_person);
                viewitem_reply_keyin_content = view.findViewById(R.id.viewitem_reply_keyin_content);
                viewitem_reply_keyin_confirm = view.findViewById(R.id.viewitem_reply_keyin_confirm);
                viewitem_reply_keyin_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final JSONObject jsonObject = new ContactJsonData().setContactCont(gv.getToken(), msno, viewitem_reply_keyin_content.getText().toString());
                                new android.os.Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (jsonObject.getBoolean("Success")) {
                                                ((Activity) context).finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                });
                ImageView photoImage;
                photoImages = new ArrayList<>();
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo1);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo2);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo3);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo4);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo5);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
                photoImage = view.findViewById(R.id.viewitem_reply_keyin_photo6);
                photoImage.setOnClickListener(this);
                photoImages.add(photoImage);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra("Shape", "square");
            switch (view.getId()) {
                case R.id.viewitem_reply_keyin_photo1:
                    ((AppCompatActivity) context).startActivityForResult(intent, 100);
                    break;
                case R.id.viewitem_reply_keyin_photo2:
                    ((AppCompatActivity) context).startActivityForResult(intent, 200);
                    break;
                case R.id.viewitem_reply_keyin_photo3:
                    ((AppCompatActivity) context).startActivityForResult(intent, 300);
                    break;
                case R.id.viewitem_reply_keyin_photo4:
                    ((AppCompatActivity) context).startActivityForResult(intent, 400);
                    break;
                case R.id.viewitem_reply_keyin_photo5:
                    ((AppCompatActivity) context).startActivityForResult(intent, 500);
                    break;
                case R.id.viewitem_reply_keyin_photo6:
                    ((AppCompatActivity) context).startActivityForResult(intent, 600);
                    break;
            }

            /*
            int position = getAdapterPosition();
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, itemPojoList);
            }
            */
        }
    }

    public void setPhotos(Bitmap photo, int position) {
        photos[position] = photo;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setClickListener(ReplyRecyclerAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, List<ItemPojo> itemPojoList);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        ArrayList<Map<String, String>> list;
        if (json != null) {
            list = AnalyzeContact.getContact(json);

        } else {
            list = new ArrayList<>();
        }
        initItems(list);
        notifyDataSetChanged();

    }


    private class ItemPojo {
        private String type;
        private String person;
        private String isread;
        private String content;
        private String time;
        boolean isCheck;

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