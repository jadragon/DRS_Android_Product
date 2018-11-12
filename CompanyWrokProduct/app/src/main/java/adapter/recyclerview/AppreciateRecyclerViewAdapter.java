package adapter.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.AppreciatePojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Util.ComponentUtil;
import library.AnalyzeJSON.AnalyzeOrderInfo;

public class AppreciateRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private Context ctx;
    private GlobalVariable gv;
    private DisplayMetrics dm;
    private ArrayList<AppreciatePojo> arrayList;

    public AppreciateRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        gv = ((GlobalVariable) ctx.getApplicationContext());
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
            arrayList = AnalyzeOrderInfo.getOrderComment(json);
        } else {
            arrayList = new ArrayList<>();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //頁面
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(((int) (10 * dm.density)), ((int) (10 * dm.density)), ((int) (10 * dm.density)), ((int) (10 * dm.density)));
            textView.setGravity(Gravity.CENTER);
            return new AppreciateRecyclerViewAdapter.HeaderHolder(ctx, textView);

        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_appreciate, parent, false);
        return new AppreciateRecyclerViewAdapter.ContentHolder(ctx, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {//payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            if (getItemViewType(position) == TYPE_HEADER) {
                if (arrayList.get(position).getComtimes() < 2) {
                    ((HeaderHolder) holder).textView.setText("溫馨提醒,您可再" + (2 - arrayList.get(position).getComtimes()) + "次修改評價");

                } else {
                    ((HeaderHolder) holder).textView.setText("目前無法修改評價，如有任何問題請聯絡客服");
                    Button confirm = ((Activity) ctx).findViewById(R.id.appreciate_confirm);
                    confirm.setText("已無法修改");
                    confirm.setEnabled(false);
                    new ComponentUtil(ctx).reShapeButton(confirm, R.color.gray);
                }
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                ImageLoader.getInstance().displayImage(arrayList.get(position - 1).getImg(), ((ContentHolder) holder).appreciate_srars_img);
                ((ContentHolder) holder).appreciate_srars_pname.setText(arrayList.get(position - 1).getPname());
                ((ContentHolder) holder).appreciate_srars_color.setText(arrayList.get(position - 1).getColor());
                ((ContentHolder) holder).appreciate_srars_size.setText(arrayList.get(position - 1).getSize());
                ((ContentHolder) holder).appreciate_srars_comment.setText(arrayList.get(position - 1).getComment());
                ((ContentHolder) holder).appreciate_srars_comdate.setText(arrayList.get(position - 1).getComdate());
                for (int i = 0; i < arrayList.get(position - 1).getComscore(); i++) {
                    ((ContentHolder) holder).stars.get(i).setImageResource(R.mipmap.star_on);
                }
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
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() != 0 ? arrayList.size() + 1 : 0;

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView textView;

        public HeaderHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            textView = ((TextView) view);
        }

    }

    class ContentHolder extends RecyclerView.ViewHolder {
        Context ctx;
        ArrayList<ImageView> stars;
        ArrayList<Float> starsX;
        LinearLayout linearLayout;
        ImageView appreciate_srars_img;
        TextView appreciate_srars_pname, appreciate_srars_color, appreciate_srars_size, appreciate_srars_comdate, appreciate_srars_comment;
        private int finalScore;

        public ContentHolder(final Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            appreciate_srars_img = view.findViewById(R.id.appreciate_srars_img);
            appreciate_srars_pname = view.findViewById(R.id.appreciate_srars_pname);
            appreciate_srars_color = view.findViewById(R.id.appreciate_srars_color);
            appreciate_srars_size = view.findViewById(R.id.appreciate_srars_size);
            appreciate_srars_comdate = view.findViewById(R.id.appreciate_srars_comdate);
            appreciate_srars_comment = view.findViewById(R.id.appreciate_srars_comment);
            appreciate_srars_comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    arrayList.get(getAdapterPosition() - 1).setComment(editable + "");
                }
            });
            linearLayout = view.findViewById(R.id.appreciate_srars_layout);
            starsX = new ArrayList<>();
            stars = new ArrayList<>();
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                stars.add(((ImageView) linearLayout.getChildAt(i)));
            }
            linearLayout.post(new Runnable() {
                @Override
                public void run() {
                    for (ImageView imageView : stars) {
                        starsX.add(imageView.getX());
                    }
                }
            });
            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float x;
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        //当手指按下的时候
                        x = motionEvent.getX();
                        for (int i = starsX.size() - 1; i >= 0; i--) {
                            if (x > starsX.get(i)) {
                                for (int j = i; j >= 0; j--) {
                                    stars.get(j).setImageResource(R.mipmap.star_on);
                                }
                                for (int j = i + 1; j < starsX.size(); j++) {
                                    stars.get(j).setImageResource(R.mipmap.star_off);
                                }
                                finalScore = i + 1;
                                break;
                            }
                        }
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        //当手指离开的时候
                        x = motionEvent.getX();
                        for (int i = starsX.size() - 1; i >= 0; i--) {
                            if (x > starsX.get(i)) {
                                for (int j = i; j >= 0; j--) {
                                    stars.get(j).setImageResource(R.mipmap.star_on);
                                }
                                for (int j = i + 1; j < starsX.size(); j++) {
                                    stars.get(j).setImageResource(R.mipmap.star_off);
                                }
                                finalScore = i + 1;
                                break;
                            }
                        }
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        arrayList.get(getAdapterPosition() - 1).setComscore(finalScore);
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return false;
                }
            });
        }
    }

    public JSONArray getJSONArray() {
        JSONArray array = new JSONArray();
        JSONObject item;
        for (AppreciatePojo appreciatePojo : arrayList) {
            try {
                item = new JSONObject();
                item.put("moino", appreciatePojo.getMoino());
                item.put("comment", appreciatePojo.getComment());
                item.put("comscore", appreciatePojo.getComscore());
                array.put(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public void setFilter(JSONObject json) {
        if (json != null) {
            arrayList = AnalyzeOrderInfo.getOrderComment(json);
        } else {
            arrayList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
