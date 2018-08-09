package library.Http;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class CertJSONParser {



    public String getJSONFromUrl(String urlString, List<NameValuePair> params) {
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
        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            // 使用ssl context
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setRequestMethod("POST");
            //"sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            String urlParameters = "";
            for (NameValuePair nameValuePair : params) {
                if (urlParameters.length()!=0)
                    urlParameters = urlParameters + "&" + nameValuePair.getName() + "=" + nameValuePair.getValue();
                else
                    urlParameters =  nameValuePair.getName() + "=" + nameValuePair.getValue();
            }
            //发送Post请求
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(urlParameters);
            writer.close();
            wr.flush();
            wr.close();
            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            // 讀取結果
            InputStream is = urlConnection.getInputStream();
            if (is != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte data[] = new byte[256];
                int length = 0, getPer = 0;
                while ((getPer = is.read(data)) != -1) {
                    length += getPer;
                    byteArrayOutputStream.write(data, 0, getPer);
                }
                is.close();
                byteArrayOutputStream.close();
                String utf8 = new String(byteArrayOutputStream.toByteArray(), "UTF-8").trim();
                Log.e("JSON", utf8);
                return utf8;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
