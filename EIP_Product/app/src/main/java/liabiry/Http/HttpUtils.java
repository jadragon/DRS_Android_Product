package liabiry.Http;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpUtils {

    private static SSLContext sslContext = null;
    private static Context context=null;


    public static void setContext(Context context) {
        HttpUtils.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static SSLContext prepareSelfSign() {
        if (context!=null&&sslContext == null) {
            KeyStore trustStore = null;
            try {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            if (trustStore == null) {
                return null;
            }
            InputStream crtInput = null;
            try {
                // 載入憑證檔
                crtInput = context.getAssets().open("cert.crt");
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = new BufferedInputStream(crtInput);
                Certificate ca = cf.generateCertificate(caInput);
                trustStore.load(null, null);
                trustStore.setCertificateEntry("ca", ca);

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(trustStore);

                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } finally {
                if (crtInput != null) {
                    try {
                        crtInput.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return sslContext;
    }
}
