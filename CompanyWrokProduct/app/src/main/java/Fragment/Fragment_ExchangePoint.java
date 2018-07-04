package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.帳務管理.點值轉換.ExchangePointActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import library.Component.ToastMessageDialog;
import library.GetJsonData.BillJsonData;
import library.JsonDataThread;

public class Fragment_ExchangePoint extends Fragment implements View.OnClickListener {
    JSONObject json;
    View v;
    int type, point_type;
    GlobalVariable gv;
    TextView exchange_point, exchange_rate1, exchange_rate2;
    EditText exchange_edit1, exchange_edit2;
    Button exchange_confirm;
    ToastMessageDialog toastMessageDialog;

    public void setPoint_type(int point_type) {
        this.point_type = point_type;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public void setType(int type) {
        this.type = type;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_exchange_point, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        toastMessageDialog = new ToastMessageDialog(getContext());
        initID();
        exchange_confirm.setOnClickListener(this);
        initComponent();
        return v;
    }

    private void initID() {
        exchange_point = v.findViewById(R.id.exchange_point);
        exchange_rate1 = v.findViewById(R.id.exchange_rate1);
        exchange_rate2 = v.findViewById(R.id.exchange_rate2);
        exchange_edit1 = v.findViewById(R.id.exchange_edit1);
        exchange_edit2 = v.findViewById(R.id.exchange_edit2);
        exchange_confirm = v.findViewById(R.id.exchange_confirm);
    }

    private void initComponent() {
        switch (type) {
            case 1:
                ((ImageView) v.findViewById(R.id.exchange_image)).setImageResource(R.mipmap.poke_point);
                ((TextView) v.findViewById(R.id.exchange_title)).setText("波克點值");
                ((TextView) v.findViewById(R.id.exchange_name1)).setText("波克:庫瓦");
                v.findViewById(R.id.exchange_group2).setVisibility(View.GONE);
                break;
            case 2:
                ((ImageView) v.findViewById(R.id.exchange_image)).setImageResource(R.mipmap.colwa_point);
                ((TextView) v.findViewById(R.id.exchange_title)).setText("庫瓦點值");
                ((TextView) v.findViewById(R.id.exchange_name1)).setText("波克:庫瓦");
                ((TextView) v.findViewById(R.id.exchange_name2)).setText("庫瓦:雙閃");
                break;
        }

    }


    public void setText(JSONObject json) {
        if (json != null) {
            try {
                String point = json.getJSONObject("Data").getString("point");
                exchange_point.setText(point);
                switch (type) {
                    case 1:
                        BigDecimal rate = new BigDecimal(json.getJSONObject("Data").getString("kuva_rate"));
                        exchange_rate1.setText("1:" + rate);
                        break;
                    case 2:
                        BigDecimal rate1 = new BigDecimal(json.getJSONObject("Data").getString("polk_rate"));
                        BigDecimal rate2 = new BigDecimal(json.getJSONObject("Data").getString("acoin_rate"));
                        exchange_rate1.setText("1:" + rate1);
                        exchange_rate2.setText("1:" + rate2);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        String edit1 = exchange_edit1.getText().toString().equals("") ? "0" : exchange_edit1.getText().toString();
        String edit2 = exchange_edit2!=null&&exchange_edit2.getText().toString().equals("") ? "0" : exchange_edit1.getText().toString();
        switch (type) {
            case 1:
                if (!edit1.equals("0")) {
                    new JsonDataThread() {
                        @Override
                        public JSONObject getJsonData() {
                            return new BillJsonData().getPolkTrans(gv.getToken(), exchange_edit1.getText().toString());
                        }

                        @Override
                        public void runUiThread(JSONObject json) {
                            try {
                                if (json.getBoolean("Success")) {
                                    exchange_edit1.setText(null);
                                    ((ExchangePointActivity) getActivity()).refreshText();
                                }
                                toastMessageDialog.setMessageText(json.getString("Message"));
                                toastMessageDialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            case 2:
                if(!edit1.equals("0")||!edit2.equals("0")) {
                    new JsonDataThread() {
                        @Override
                        public JSONObject getJsonData() {
                            return new BillJsonData().getKuvaTrans(gv.getToken(), exchange_edit1.getText().toString(), exchange_edit2.getText().toString());
                        }

                        @Override
                        public void runUiThread(JSONObject json) {
                            try {
                                if (json.getBoolean("Success")) {
                                    exchange_edit1.setText(null);
                                    exchange_edit2.setText(null);
                                    ((ExchangePointActivity) getActivity()).refreshText();
                                }
                                toastMessageDialog.setMessageText(json.getString("Message"));
                                toastMessageDialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
        }
    }
}
