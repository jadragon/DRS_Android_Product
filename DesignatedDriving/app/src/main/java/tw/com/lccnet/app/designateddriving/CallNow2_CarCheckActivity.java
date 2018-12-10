package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.Component.CarImageView;

public class CallNow2_CarCheckActivity extends ToolbarActivity implements View.OnClickListener {
    private List<CarImageView> list;
    private int[] image1 = {R.drawable.car1, R.drawable.car2, R.drawable.car3, R.drawable.car4, R.drawable.car5, R.drawable.car6, R.drawable.car7, R.drawable.car7,
            R.drawable.car9, R.drawable.car9, R.drawable.car11, R.drawable.car12, R.drawable.car13, R.drawable.car14, R.drawable.car15, R.drawable.car16, R.drawable.car16, R.drawable.car16, R.drawable.car16};
    private int[] image2 = {R.drawable.car_red1, R.drawable.car_red2, R.drawable.car_red3, R.drawable.car_red4, R.drawable.car_red5, R.drawable.car_red6, R.drawable.car_red7, R.drawable.car_red8,
            R.drawable.car_red9, R.drawable.car_red10, R.drawable.car_red11, R.drawable.car_red12, R.drawable.car_red13, R.drawable.car_red14, R.drawable.car_red15, R.drawable.car_red16, R.drawable.car_red17, R.drawable.car_red18, R.drawable.car_red19};
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow2_car_check);
        initToolbar("汽車檢查", true);
        initView();
    }

    private void initView() {
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        list = new ArrayList<>();
        list.add((CarImageView) findViewById(R.id.car1));
        list.add((CarImageView) findViewById(R.id.car2));
        list.add((CarImageView) findViewById(R.id.car3));
        list.add((CarImageView) findViewById(R.id.car4));
        list.add((CarImageView) findViewById(R.id.car5));
        list.add((CarImageView) findViewById(R.id.car6));
        list.add((CarImageView) findViewById(R.id.car7));
        list.add((CarImageView) findViewById(R.id.car8));
        list.add((CarImageView) findViewById(R.id.car9));
        list.add((CarImageView) findViewById(R.id.car10));
        list.add((CarImageView) findViewById(R.id.car11));
        list.add((CarImageView) findViewById(R.id.car12));
        list.add((CarImageView) findViewById(R.id.car13));
        list.add((CarImageView) findViewById(R.id.car14));
        list.add((CarImageView) findViewById(R.id.car15));
        list.add((CarImageView) findViewById(R.id.car16));
        list.add((CarImageView) findViewById(R.id.car17));
        list.add((CarImageView) findViewById(R.id.car18));
        list.add((CarImageView) findViewById(R.id.car19));
        for (CarImageView carImageView : list) {
            carImageView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof CarImageView) {
            CarImageView carImageView = ((CarImageView) view);
            if (carImageView.isCheck()) {
                carImageView.setBackgroundColor(getResources().getColor(R.color.invisible));
                carImageView.setImageResource(image1[list.indexOf(view)]);
            } else {
                carImageView.setBackgroundColor(getResources().getColor(R.color.invisible_red));
                carImageView.setImageResource(image2[list.indexOf(view)]);
            }
            carImageView.setCheck(!carImageView.isCheck());
        } else {
            startActivity(new Intent(CallNow2_CarCheckActivity.this, CallNow3_DrivingActivity.class));
            finish();
        }
    }

}