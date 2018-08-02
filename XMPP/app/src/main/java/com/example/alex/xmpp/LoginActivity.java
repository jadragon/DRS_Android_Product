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

import org.jivesoftware.smack.XMPPException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Thread(new Runnable() {
            @Override
            public void run() {
                IMService.getConnection();
            }
        }).start();
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
                            //添加额外配置信息
                            IMService.getConnection().login(mAccountView.getText().toString(), mPasswordView.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    //保存當前帳戶
                                    String account = mAccountView.getText().toString() + "@" + IMService.SERVER_NAME;
                                    IMService.CURRENT_ACCOUNT = account;
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

