package tw.com.lccnet.app.designateddriving.RecyclerAdapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.com.lccnet.app.designateddriving.CallNow6_EvaluationActivity;
import tw.com.lccnet.app.designateddriving.Component.LinePathView;
import tw.com.lccnet.app.designateddriving.R;

public class CountListAdapter extends RecyclerView.Adapter<CountListAdapter.RecycleHolder> {
    private static int TYPE_HEADER = 0x01;
    private static int TYPE_CONTENT1 = 0x02;
    private static int TYPE_CONTENT2 = 0x03;
    private static int TYPE_FOOTER = 0x04;
    private Context ctx;
    private List<Map<String, String>> list;

    public CountListAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
    }


    @NonNull
    @Override
    public CountListAdapter.RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            textView.setText("訂單編號12345678");
            textView.setTextSize(18);
            textView.setTextColor(ctx.getResources().getColor(R.color.royal_blue));
            textView.setPadding(20, 0, 0, 0);
            //  ((LinearLayout.LayoutParams) textView.getLayoutParams()).setMargins(0, 20, 0, 20);
            return new CountListAdapter.RecycleHolder(textView, viewType);
        } else if (viewType == TYPE_CONTENT1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_countlist_content1, parent, false);
        } else if (viewType == TYPE_CONTENT2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_countlist_content2, parent, false);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_countlist_footer, parent, false);
        }
        return new CountListAdapter.RecycleHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position < 5) {
            return TYPE_CONTENT1;
        } else if (position < 11) {
            return TYPE_CONTENT2;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT1;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
    }


    @Override
    public int getItemCount() {
        return 12;
    }

    class RecycleHolder extends RecyclerView.ViewHolder {

        public RecycleHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_FOOTER) {
                final ImageView imageView = view.findViewWithTag("paint");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final LinePathView linePathView = new LinePathView(ctx);
                        AlertDialog alertDialog = new AlertDialog.Builder(ctx).setView(linePathView).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageView.setImageBitmap(linePathView.getBitMap(imageView.getWidth(), imageView.getHeight()));
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        // alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();
                        p.width = imageView.getWidth();
                        p.height = imageView.getHeight() + 300;
                        alertDialog.getWindow().setAttributes(p);     //设置生效
                    }
                });
                view.findViewWithTag("next").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ctx.startActivity(new Intent(ctx, CallNow6_EvaluationActivity.class));
                        ((Activity) ctx).finish();
                    }
                });


            }

        }


    }

    public void setFilter(JSONObject jsonObject) {
    }
}
