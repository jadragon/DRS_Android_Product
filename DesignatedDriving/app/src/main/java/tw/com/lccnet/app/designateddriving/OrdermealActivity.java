package tw.com.lccnet.app.designateddriving;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Component.ResizableImageView;
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
                    ImageLoader.getInstance().loadImage(jsonObject.getString("Data"), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ImageView imageView = findViewById(R.id.image);
                            imageView.setImageBitmap(bitmap);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                    // ImageLoader.getInstance().displayImage(jsonObject.getString("Data"), );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
