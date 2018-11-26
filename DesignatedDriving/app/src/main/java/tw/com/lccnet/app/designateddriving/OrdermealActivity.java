package tw.com.lccnet.app.designateddriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class OrdermealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermeal);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.ordermeal();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                try {
                    ImageLoader.getInstance().displayImage(jsonObject.getString("Data"), (ImageView) findViewById(R.id.image));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
