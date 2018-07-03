package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class Fragment_ExchangePoint extends Fragment {
    JSONObject json;
    View v;
    int type, point_type;
    GlobalVariable gv;
    ImageView exchange_image;
    TextView exchange_title, exchange_point, exchange_name1, exchange_rate1, exchange_name2, exchange_rate2;
    EditText exchange_edit1, exchange_edit2;

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
        initID();
        return v;
    }

    private void initID() {
        exchange_image = v.findViewById(R.id.exchange_image);
        exchange_title = v.findViewById(R.id.exchange_title);
        exchange_point = v.findViewById(R.id.exchange_point);
        exchange_name1 = v.findViewById(R.id.exchange_name1);
        exchange_rate1 = v.findViewById(R.id.exchange_rate1);
        exchange_name2 = v.findViewById(R.id.exchange_name2);
        exchange_rate2 = v.findViewById(R.id.exchange_rate2);
        exchange_edit1 = v.findViewById(R.id.exchange_edit1);
        exchange_edit2 = v.findViewById(R.id.exchange_edit2);
    }

    public void setText(JSONObject json) {
        NumberFormat nf = NumberFormat.getInstance();
        if (json != null) {
            try {
                String point = json.getJSONObject("Data").getString("point");
                exchange_point.setText(point);
                switch (type) {
                    case 1:
                        Double rate = json.getJSONObject("Data").getDouble("kuva_rate");
                        exchange_rate1.setText("1:" + rate.toString());
                        break;
                    case 2:
                        Double rate1 = json.getJSONObject("Data").getDouble("polk_rate");
                        Double rate2 = json.getJSONObject("Data").getDouble("acoin_rate");
                        exchange_rate1.setText("1:" + rate1.toString());
                        exchange_rate2.setText("1:" + String.valueOf(nf.format(rate2)));
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
