/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package library.Http;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * 透過URL及Params取得資料庫的資料(JSON)
 */
public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    public JSONObject getJSONFromUrl(String urlString, List<NameValuePair> params) {
        //建立 ssl context
        SSLContext sslContext = HttpUtils.prepareSelfSign();
        if (sslContext == null) {
            return null;
        }
        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) {
            return null;
        }
        // Making HTTP request
        try {
            // defaultHttpClient
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            // 使用ssl context
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setRequestMethod("POST");
            //"sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            String urlParameters = "";
            for (NameValuePair nameValuePair : params) {
                if (urlParameters.length() != 0)
                    urlParameters = urlParameters + "&" + nameValuePair.getName() + "=" + nameValuePair.getValue();
                else
                    urlParameters = nameValuePair.getName() + "=" + nameValuePair.getValue();
            }
            //发送Post请求
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(urlParameters);
            writer.close();
            wr.flush();
            wr.close();

            // 讀取結果
            is = urlConnection.getInputStream();

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
          //  Log.e("JSON", json);
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
