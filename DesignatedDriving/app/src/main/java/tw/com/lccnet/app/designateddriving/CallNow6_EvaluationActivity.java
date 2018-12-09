package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CallNow6_EvaluationActivity extends ToolbarActivity {

    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow6_evaluation);
        initToolbar("結帳", false);
        initStar();
        initButton();
    }

    private void initButton() {
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<Float> starsX;
    private int finalScore;

    private void initStar() {
        final List<ImageView> stars = new ArrayList<>();
        stars.add((ImageView) findViewById(R.id.appreciate_srar1));
        stars.add((ImageView) findViewById(R.id.appreciate_srar2));
        stars.add((ImageView) findViewById(R.id.appreciate_srar3));
        stars.add((ImageView) findViewById(R.id.appreciate_srar4));
        stars.add((ImageView) findViewById(R.id.appreciate_srar5));

        final LinearLayout linearLayout = findViewById(R.id.appreciate_srars_layout);
        starsX = new ArrayList<>();
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
                    if (starsX.get(0) > x) {
                        for (int j = 4; j >= 0; j--) {
                            stars.get(j).setImageResource(R.drawable.star_off);
                        }
                    } else {
                        for (int i = starsX.size() - 1; i >= 0; i--) {
                            if (x > starsX.get(i)) {
                                for (int j = i; j >= 0; j--) {
                                    stars.get(j).setImageResource(R.drawable.star_on);
                                }
                                for (int j = i + 1; j < starsX.size(); j++) {
                                    stars.get(j).setImageResource(R.drawable.star_off);
                                }
                                finalScore = i + 1;
                                break;
                            }
                        }
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    //当手指离开的时候
                    x = motionEvent.getX();
                    if (starsX.get(0) > x) {
                        for (int j = 4; j >= 0; j--) {
                            stars.get(j).setImageResource(R.drawable.star_off);
                        }
                    } else {
                        for (int i = starsX.size() - 1; i >= 0; i--) {
                            if (x > starsX.get(i)) {
                                for (int j = i; j >= 0; j--) {
                                    stars.get(j).setImageResource(R.drawable.star_on);
                                }
                                for (int j = i + 1; j < starsX.size(); j++) {
                                    stars.get(j).setImageResource(R.drawable.star_off);
                                }
                                finalScore = i + 1;
                                break;
                            }
                        }
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }

}
