package tw.com.lccnet.app.designateddriving.Http;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ImageJSONParser {
    /**
     * 透過URL及Params取得資料庫的資料(JSON)
     */
    static JSONObject jObj = null;
    static InputStream is = null;
    static String json = "";

    public JSONObject getJSONFromUrl(String url, MultipartEntity entity) {
      /*
            if (bm != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                }
                MultipartEntity multipart = new MultipartEntity();
       File file = new File("/filepath");  // File with some location (filepath)
        Charset chars = Charset.forName("UTF-8"); // Setting up the encoding
        FileBody fileB = new FileBody(file); // Create a new FileBody with the above mentioned file
        multipart.addPart("data", fileB); // Add the part to my MultipartEntity. "data" is parameter name for the file
        StringBody stringB;  // Now lets add some extra information in a StringBody
        try {
            stringB = new StringBody("I am the caption of the file", chars);  // Adding the content to the StringBody and setting up the encoding
            multipart.addPart("caption", stringB); // Add the part to my MultipartEntity
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(url); // Setting up a HTTP Post method with the target url
        post.setEntity(multipart); // Setting the multipart Entity to the post method
        HttpResponse resp = client.execute(post);  // Using some HttpClient (I'm using DefaultHttpClient) to execute the post method and receive the response
          */
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
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
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}
