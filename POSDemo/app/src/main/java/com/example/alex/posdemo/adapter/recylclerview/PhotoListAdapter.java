package com.example.alex.posdemo.adapter.recylclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.MainActivity;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.fragment.sub.Fragment_photo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.AlbumListPojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_AlbumInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.PhotoApi;

public class PhotoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static byte TYPE_NORMAL = 0;
    public static byte TYPE_SELECT = 1;
    int type;
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<AlbumListPojo> list;
    PhotoApi photoApi;
    Analyze_AlbumInfo analyze_albumInfo;
    UserInfo userInfo;
    String a_no;

    public PhotoListAdapter(Context ctx, JSONObject json, String a_no, byte type) {
        this.ctx = ctx;
        this.a_no = a_no;
        this.type = type;
        userInfo = (UserInfo) ctx.getApplicationContext();
        dm = ctx.getResources().getDisplayMetrics();
        photoApi = new PhotoApi();
        analyze_albumInfo = new Analyze_AlbumInfo();
        list = new ArrayList<>();
        list.add(new AlbumListPojo());
        list.addAll(analyze_albumInfo.getAlbum_Data(json));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (type == TYPE_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumlist, parent, false);
            return new PhotoListAdapter.NormalHolder(view, viewType);
        } else if (type == TYPE_SELECT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumselect, parent, false);
            return new PhotoListAdapter.SelectHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (type == TYPE_NORMAL) {
            return position;
        } else {
            return -position - 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (type == TYPE_NORMAL) {
            NormalHolder normalHolder = (NormalHolder) holder;
            if (position > 0) {
                if (list.get(position).getImg().equals("")) {
                    normalHolder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_default));
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).getImg(), normalHolder.photo);
                }

                normalHolder.title.setText(list.get(position).getName());
                normalHolder.count.setText("[" + list.get(position).getCount() + "張相片]");
            } else {
                normalHolder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_add));
                normalHolder.title.setText("新增相簿");
            }
        } else {
            SelectHolder selectHolder = (SelectHolder) holder;
            if (position > 0) {
                if (list.get(position).getImg().equals("")) {
                    selectHolder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_default));
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).getImg(), selectHolder.photo);
                }
                if (list.get(position).isCheck()) {
                    selectHolder.cover.setVisibility(View.VISIBLE);
                    selectHolder.checkbox.setChecked(true);

                } else {
                    selectHolder.cover.setVisibility(View.INVISIBLE);
                    selectHolder.checkbox.setChecked(false);
                }
            } else {
                selectHolder.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.album_add));
                selectHolder.checkbox.setVisibility(View.INVISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SelectHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        View cover;
        CheckBox checkbox;
        Handler handler;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(getAdapterPosition());
            }
        };

        public SelectHolder(View itemView) {
            super(itemView);
            handler = new Handler(ctx.getMainLooper());
            photo = itemView.findViewWithTag("photo");
            cover = itemView.findViewWithTag("cover");
            checkbox = itemView.findViewWithTag("checkbox");
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        list.get(getAdapterPosition()).setCheck(true);
                    } else {
                        list.get(getAdapterPosition()).setCheck(false);
                    }
                    handler.post(runnable);
                }
            });
        }
    }


    class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        ImageView photo;
        ImageView edit, delete;
        TextView title, count;


        public NormalHolder(View view, final int position) {
            super(view);
            layout = view.findViewWithTag("layout");
            if (position == 0) {
                layout.setVisibility(View.GONE);
            }
            photo = view.findViewWithTag("photo");
            delete = view.findViewWithTag("delete");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm("確定要刪除此相簿?", new ToastMessageDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm(AlertDialog alertDialog) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public void onTaskBefore() {

                                }

                                @Override
                                public JSONObject onTasking(Void... params) {
                                    // return photoApi.remove_album(list.get(getAdapterPosition()).getA_no());
                                    return null;
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        resetAdapter();
                                    }
                                    new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                                }
                            });
                        }
                    });
                }
            });
            title = view.findViewWithTag("title");
            count = view.findViewWithTag("count");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == 0) {
                new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_EDIT).sendApi("", new ToastMessageDialog.OnSendApiListener() {
                    @Override
                    public void onConfirm(AlertDialog alertDialog, final String note) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public void onTaskBefore() {

                            }

                            @Override
                            public JSONObject onTasking(Void... params) {
                                Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.mainslider_home_img1);
                                return photoApi.insert_photo(a_no,userInfo.getDu_no(), note, bitmap);
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    resetAdapter();
                                }
                                new ToastMessageDialog(ctx, ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                            }
                        });
                    }
                });
            } else {
                Fragment_photo fragment_photo = new Fragment_photo();
                Bundle bundle = new Bundle();
                bundle.putString("a_no", list.get(getAdapterPosition()).getA_no());
                fragment_photo.setArguments(bundle);
                ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, fragment_photo, "photo").commit();
                ((MainActivity) ctx).setSUB_FRAGMENT_TAG("photo");
                ((MainActivity) ctx).showBack(true);
            }


        }

    }

    public void resetAdapter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return photoApi.photo_data(a_no);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                setFilter(jsonObject);
            }
        });
    }

    public void setFilter(JSONObject json) {
        list = new ArrayList<>();
        list.add(new AlbumListPojo());
        list.addAll(analyze_albumInfo.getAlbum_Data(json));
        notifyDataSetChanged();
    }

    public String showCheckedItem() {
        StringBuilder builder = new StringBuilder();
        for (AlbumListPojo pojo : list) {
            if (pojo.isCheck()) {
                builder.append("," + pojo.getA_no());
            }
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    public void changeType(byte type) {
        this.type = type;
        for (AlbumListPojo pojo : list) {
            if (pojo.isCheck()) {
                pojo.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

}