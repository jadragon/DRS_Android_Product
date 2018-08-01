package com.example.alex.xmpp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.xmpp.service.IMService;
import com.example.alex.xmpp.service.RegistActivity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    public final static int SERVER_PORT = 5222;//服务端口 可以在openfire上设置
    public final static String SERVER_HOST = "220.134.248.18";//你openfire服务器所在的ip
    public final static String SERVER_NAME = "220.134.248.18";
    public XMPPConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            connection = IMService.getConnection();
                            connection.connect();
                            //添加额外配置信息
                            connection.login(mAccountView.getText().toString(), mPasswordView.getText().toString());
                            IMService.connection = connection;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                                    //啟動service
                                    Intent service = new Intent(LoginActivity.this, IMService.class);
                                    startService(service);
                                    finish();
                                }
                            });
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

}

