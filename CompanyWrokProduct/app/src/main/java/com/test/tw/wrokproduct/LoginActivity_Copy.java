package com.test.tw.wrokproduct;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
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

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeMember;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;

public class LoginActivity_Copy extends AppCompatActivity implements View.OnClickListener {
    private  Toolbar toolbar;
    private   EditText login_edit_account, login_edit_password;
    private  Button login_button;
    private   GlobalVariable gv;
    private  ImageView login_img_account, login_img_mobile, login_img_email, login_img_fb, login_img_google;
    private   int type;
    private  String id;
    private  JSONObject jsonObject;
    private  TextView login_txt_account, login_btn_forget, login_btn_register;
    private   ToastMessageDialog toastMessage;
    private  String account;
    private View login_cover_bg, login_info_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_cover_bg = findViewById(R.id.login_cover_bg);
        login_info_layout = findViewById(R.id.login_info_layout);
        toastMessage = new ToastMessageDialog(this);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar();
        initTextView();
        initEditText();
        initButton();
        initTextButton();
        initImage();
    }

    private void initTextView() {
        login_txt_account = findViewById(R.id.login_txt_account);
    }

    private void initTextButton() {
        login_btn_register = findViewById(R.id.login_btn_register);
        login_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity_Copy.this, RegisterActivity.class));
            }
        });

        login_btn_forget = findViewById(R.id.login_btn_forget);
        login_btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity_Copy.this, ForgetPassActivity.class);
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
                if (!TextUtils.isEmpty(login_edit_account.getText().toString()) || !TextUtils.isEmpty(login_edit_password.getText().toString())) {
                    switch (type) {
                        case 0:
                            sendApi();
                            break;
                        case 1:
                            if (login_edit_account.getText().toString().matches("09[0-9]{8}")) {
                                sendApi();
                            } else {
                                login_edit_account.setError("手機格式有誤");
                                //  toastMessage.setMessageText("手機格式有誤");
                                //  toastMessage.confirm();
                            }
                            break;
                        case 2:
                            if (login_edit_account.getText().toString().matches("[\\w-.]+@[\\w-]+(.[\\w_-]+)+")) {
                                sendApi();
                            } else {
                                login_edit_account.setError("信箱格式有誤");
                                //  toastMessage.setMessageText("信箱格式有誤");
                                //  toastMessage.confirm();
                            }
                            break;
                    }

                } else {
                    login_edit_account.setError("帳號及密碼不能為空");
                    login_edit_password.setError("帳號及密碼不能為空");
                    // toastMessage.setMessageText("帳號及密碼不能為空");
                    // toastMessage.confirm();
                }
                break;
            case R.id.login_img_account:
                type = 0;
                login_info_layout.setVisibility(View.VISIBLE);
                login_txt_account.setText("帳號");
                login_edit_account.setHint("請輸入您的帳號");
                login_edit_account.setInputType(InputType.TYPE_CLASS_TEXT);
                login_edit_account.setText(null);
                login_edit_password.setText(null);
                login_edit_account.setEnabled(true);
                login_edit_password.setEnabled(true);
                login_button.setEnabled(true);
                break;
            case R.id.login_img_mobile:
                type = 1;
                login_info_layout.setVisibility(View.VISIBLE);
                login_txt_account.setText("手機號碼");
                login_edit_account.setHint("請輸入您的手機號碼");
                login_edit_account.setInputType(InputType.TYPE_CLASS_PHONE);
                login_edit_account.setText(null);
                login_edit_password.setText(null);
                login_edit_account.setEnabled(true);
                login_edit_password.setEnabled(true);
                login_button.setEnabled(true);
                break;
            case R.id.login_img_email:
                type = 2;
                login_info_layout.setVisibility(View.VISIBLE);
                login_txt_account.setText("電子信箱");
                login_edit_account.setHint("請輸入您的電子信箱");
                login_edit_account.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                login_edit_account.setText(null);
                login_edit_password.setText(null);
                login_edit_account.setEnabled(true);
                login_edit_password.setEnabled(true);
                login_button.setEnabled(true);
                break;
            case R.id.login_img_fb:
                type = 3;
                login_info_layout.setVisibility(View.INVISIBLE);
                login_cover_bg.setVisibility(View.VISIBLE);
                quickLoginFB();

                break;
            case R.id.login_img_google:
                type = 4;
                login_info_layout.setVisibility(View.INVISIBLE);
                login_cover_bg.setVisibility(View.VISIBLE);
                quickLoginGoogle();
                break;
        }

    }

    private void sendApi() {

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new MemberJsonData().login(type, "886", login_edit_account.getText().toString(), login_edit_password.getText().toString());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {

                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    account = login_edit_account.getText().toString();
                    try {
                        initMemberDB(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    toastMessage.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                    toastMessage.confirm();
                }

            }
        });

    }

    private void initMemberDB(final JSONObject json) throws JSONException {
        gv.setToken(jsonObject.getString("Token"));
        gv.setMvip(jsonObject.getString("Mvip"));
        final Map<String, String> datas = AnalyzeMember.getLogin(json);
        if (datas != null) {

            final SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
            db.resetLoginTables();
            db.addMember(datas.get("Token"), account, datas.get("Name"), datas.get("Picture"), datas.get("Mvip"), datas.get("Svip"));
            db.updateBackground(R.drawable.member_bg1 + "");
            ImageLoader.getInstance().loadImage(datas.get("Picture"), new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.quick_login_account);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bitmapByte = baos.toByteArray();
                    db.updatePhotoImage(bitmapByte);
                    db.close();
                    finish();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    } catch (Exception e) {
                        loadedImage = BitmapFactory.decodeResource(getResources(), R.mipmap.quick_login_account);
                        loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    }

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
                                        jsonObject = new MemberJsonData().login(type, "886", id, "");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    boolean success = jsonObject.getBoolean("Success");
                                                    if (success) {
                                                        initMemberDB(jsonObject);
                                                    } else {
                                                        toastMessage.setMessageText(jsonObject.getString("Message"));
                                                        toastMessage.confirm();
                                                    }
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
                login_cover_bg.setVisibility(View.INVISIBLE);
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
            /*
            Log.e(TAG, "--------------------------------");
            Log.e(TAG, "getId: " + account.getId());
            */
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
            login_cover_bg.setVisibility(View.INVISIBLE);
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
                        jsonObject = new MemberJsonData().login(type, "886", id, "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean success = jsonObject.getBoolean("Success");
                                    if (success) {
                                        initMemberDB(jsonObject);
                                    } else {
                                        toastMessage.setMessageText(jsonObject.getString("Message"));
                                        toastMessage.confirm();
                                    }
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
