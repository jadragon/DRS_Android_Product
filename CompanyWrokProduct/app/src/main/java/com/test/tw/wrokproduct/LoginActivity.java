package com.test.tw.wrokproduct;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeMember;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;
import library.component.ToastMessageDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText login_edit_account, login_edit_password;
    Button login_button;
    GlobalVariable gv;
    ImageView login_img_account, login_img_mobile, login_img_email, login_img_fb, login_img_google;
    int type;
    String id;
    JSONObject jsonObject;
    TextView login_btn_forget, login_btn_register;
    ToastMessageDialog toastMessage;
    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toastMessage = new ToastMessageDialog(this);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar();
        initEditText();
        initButton();
        initTextButton();
        initImage();
    }

    private void initTextButton() {
        login_btn_register = findViewById(R.id.login_btn_register);
        login_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login_btn_forget = findViewById(R.id.login_btn_forget);
        login_btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initButton() {
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    private void initImage() {
        login_img_account = findViewById(R.id.login_img_account);
        login_img_account.setOnClickListener(this);
        login_img_mobile = findViewById(R.id.login_img_mobile);
        login_img_mobile.setOnClickListener(this);
        login_img_email = findViewById(R.id.login_img_email);
        login_img_email.setOnClickListener(this);
        login_img_fb = findViewById(R.id.login_img_fb);
        login_img_fb.setOnClickListener(this);
        login_img_google = findViewById(R.id.login_img_google);
        login_img_google.setOnClickListener(this);
    }

    private void initEditText() {
        login_edit_account = findViewById(R.id.login_edit_account);
        login_edit_password = findViewById(R.id.login_edit_password);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("登入");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!login_edit_account.getText().toString().equals("") && !login_edit_password.getText().toString().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jsonObject = new MemberJsonData().login(type, "+886", login_edit_account.getText().toString(), login_edit_password.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean success = jsonObject.getBoolean("Success");
                                        if (success) {
                                            gv.setToken(jsonObject.getString("Token"));
                                            account = login_edit_account.getText().toString();
                                            initMemberDB(jsonObject);

                                        } else {
                                            toastMessage.setMessageText(jsonObject.getString("Message"));
                                            toastMessage.confirm();
                                        }
                                        Log.e("success", success + "" + jsonObject.getString("Message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    toastMessage.setMessageText("請先輸入帳號密碼");
                    toastMessage.confirm();
                }
                break;
            case R.id.login_img_account:
                login_edit_account.setHint("請輸入您的帳號");
                login_edit_account.setInputType(InputType.TYPE_CLASS_TEXT);
                type = 0;
                break;
            case R.id.login_img_mobile:
                login_edit_account.setHint("請輸入您的電話");
                login_edit_account.setInputType(InputType.TYPE_CLASS_PHONE);
                type = 1;
                break;
            case R.id.login_img_email:
                login_edit_account.setHint("請輸入您的信箱");
                login_edit_account.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                type = 2;
                break;
            case R.id.login_img_fb:
                type = 3;
                quickLoginFB();
                break;
            case R.id.login_img_google:
                type = 4;
                quickLoginGoogle();
                break;
        }

    }

    private void initMemberDB(final JSONObject json) {

        final Map<String, String> datas = AnalyzeMember.getLogin(json);

        if (datas != null) {

                    final SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
                    db.resetLoginTables();
                    db.addMember(datas.get("Token"), account, datas.get("Name"), datas.get("Picture"));
                    db.updateBackground(R.drawable.member_bg1 + "");
                    ImageLoader.getInstance().loadImage(datas.get("Picture"), new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] bitmapByte = baos.toByteArray();
                            db.updatePhotoImage(bitmapByte);
                            db.close();
                            finish();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });





        }

    }

    // FB
    private static final String TAG = FacebookActivity.class.getSimpleName();
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    public void quickLoginFB() {
        // init facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginFB();
    }

    private void loginFB() {
        // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
        /**
         * 1. NATIVE_WITH_FALLBACK
         * 2. NATIVE_ONLY
         * 3. KATANA_ONLY
         * 4. WEB_ONLY
         * 5. WEB_VIEW_ONLY
         * 6. DEVICE_AUTH
         */
        /**
         * 權限在此設定
         */
        loginManager.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_gender");

        // 設定要讀取的權限
        loginManager.logInWithReadPermissions(this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // 透過GraphRequest來取得用戶的Facebook資訊
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (response.getConnection().getResponseCode() == 200) {
                                /**
                                 * 值在這裡取得
                                 */
                                id = object.getString("id");
                                String email = object.getString("email");
                                account = email.substring(0, email.indexOf("@"));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsonObject = new MemberJsonData().login(type, "+886", id, "");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    boolean success = jsonObject.getBoolean("Success");
                                                    if (success) {
                                                        gv.setToken(jsonObject.getString("Token"));
                                                        initMemberDB(jsonObject);
                                                    } else {
                                                        toastMessage.setMessageText(jsonObject.getString("Message"));
                                                        toastMessage.confirm();
                                                    }
                                                    Log.e("success", success + "" + jsonObject.getString("Message"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                /**
                 * 想取得的資訊在這裡設定
                 */
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // 用戶取消
                Log.d(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // 登入失敗
                Log.d(TAG, "Facebook onError:" + error.toString());
            }
        });
    }


    //Google+
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    public void quickLoginGoogle() {
        /**
         * 設定Sign In 選項
         * */
        if (gso == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))//
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))//取得性別..等
                    .requestEmail()
                    .build();
        }

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        signIn();
    }

    private Map<String, String> handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Map<String, String> map = new HashMap<>();
            Log.e(TAG, "--------------------------------");
            Log.e(TAG, "getId: " + account.getId());
            map.put("id", account.getId());
            map.put("email", account.getEmail());

            // G+
            return map;
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
        return null;
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Map<String, String> map = handleSignInResult(task);
            if (map != null) {
                id = map.get("id");
                String email = map.get("email");
                account = email.substring(0, email.indexOf("@"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonObject = new MemberJsonData().login(type, "+886", id, "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean success = jsonObject.getBoolean("Success");
                                    if (success) {
                                        gv.setToken(jsonObject.getString("Token"));
                                        initMemberDB(jsonObject);
                                    } else {
                                        toastMessage.setMessageText(jsonObject.getString("Message"));
                                        toastMessage.confirm();
                                    }
                                    Log.e("success", success + "" + jsonObject.getString("Message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        }

    }


}
