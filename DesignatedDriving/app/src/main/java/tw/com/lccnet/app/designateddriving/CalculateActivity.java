package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class CalculateActivity extends ToolbarActivity {
    private Spinner sp_time, sp_type;
    private EditText calculate_edit1, calculate_edit2;
    private Button calculate_btn;
    private TextView pay, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        initToolbar("費用試算", true);
        initEditText();
        initSpinner();
        initButton();

    }

    private void initButton() {
        calculate_btn = findViewById(R.id.calculate_btn);
        calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_type.getSelectedItemPosition() < 2) {
                    if (TextUtils.isEmpty(calculate_edit1.getText())) {
                        calculate_edit1.setError("起始位置不能為空");
                        return;
                    } else if (TextUtils.isEmpty(calculate_edit2.getText())) {
                        calculate_edit2.setError("終點位置不能為空");
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(calculate_edit1.getText())) {
                        calculate_edit1.setError("時間不能為空");
                        return;
                    }
                }
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        switch (sp_type.getSelectedItemPosition()) {
                            case 0:
                                return CustomerApi.calculate1((sp_time.getSelectedItemPosition() + 1) + "", calculate_edit1.getText().toString(), calculate_edit2.getText().toString());
                            case 1:
                                return CustomerApi.calculate2(calculate_edit1.getText().toString(), calculate_edit2.getText().toString());
                            case 2:
                                return CustomerApi.calculate3((sp_time.getSelectedItemPosition() + 1) + "", calculate_edit1.getText().toString());
                            default:
                                return null;
                        }
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                try {
                                    pay.setText(jsonObject.getJSONObject("Data").getString("pay"));
                                } catch (JSONException e) {
                                    pay.setText("");
                                    e.printStackTrace();
                                }
                                try {
                                    distance.setText(jsonObject.getJSONObject("Data").getString("distance"));
                                } catch (JSONException e) {
                                    distance.setText("");
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(CalculateActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

            }

        });
    }

    private void initEditText() {
        pay = findViewById(R.id.pay);
        distance = findViewById(R.id.distance);
        calculate_edit1 = findViewById(R.id.calculate_edit1);
        calculate_edit2 = findViewById(R.id.calculate_edit2);
    }

    private void initSpinner() {
        final ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"07:00~23:59", "00:00~06:59"});
        final ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"00:00~06:59"});
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"立即", "長途", "鐘點"});
        sp_time = findViewById(R.id.sp_time);
        sp_time.setAdapter(arrayAdapter1);

        sp_type = findViewById(R.id.sp_type);
        sp_type.setAdapter(arrayAdapter3);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        calculate_edit1.setInputType(InputType.TYPE_CLASS_TEXT);
                        calculate_edit1.setHint("請輸入起始地址");
                        calculate_edit2.setVisibility(View.VISIBLE);
                        calculate_edit2.setHint("請輸入終點地址");
                        sp_time.setAdapter(arrayAdapter1);
                        break;
                    case 1:
                        calculate_edit1.setInputType(InputType.TYPE_CLASS_TEXT);
                        calculate_edit1.setHint("請輸入起始地址");
                        calculate_edit2.setVisibility(View.VISIBLE);
                        calculate_edit2.setHint("請輸入終點地址");
                        sp_time.setAdapter(arrayAdapter2);
                        break;
                    case 2:
                        calculate_edit1.setInputType(InputType.TYPE_CLASS_NUMBER);
                        calculate_edit1.setHint("請輸入預計時間(單位:分鐘)");
                        calculate_edit2.setVisibility(View.INVISIBLE);
                        sp_time.setAdapter(arrayAdapter1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
