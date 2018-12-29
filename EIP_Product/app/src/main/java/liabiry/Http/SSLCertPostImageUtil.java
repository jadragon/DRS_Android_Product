package liabiry.Http;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class SSLCertPostImageUtil {
    /**
     * 透過URL及Params取得資料庫的資料(JSON)
     */
    static JSONObject jObj = null;
    static InputStream is = null;
    static String json = "";

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params, byte[] image) throws Exception {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new SSLCertHttpClient(HttpUtils.getContext());
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            /*
            if (bm != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                }
                */
            ByteArrayBody bab = new ByteArrayBody(image, 123 + ".png");
            entity.addPart("fimg", bab);

            try {
                for (NameValuePair nameValuePair : params) {
                    entity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();


        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } finally {

            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.e("JSON", "" + jObj);
            if (!jObj.getBoolean("Success")) {
                throw new Exception("Success is false!!!!");
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}
