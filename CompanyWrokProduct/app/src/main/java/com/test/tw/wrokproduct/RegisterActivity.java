package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;
import library.JsonDataThread;
import library.LoadingView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView register_txt_account;
    EditText register_edit_account, register_edit_password;
    Button register_button, register_btn_gvcode;
    ImageView register_img_mobile, register_img_email, register_img_fb, register_img_google;
    int type = 1;
    String vcode;
    ToastMessageDialog toastMessage;
    private View register_cover_bg;
    View register_info_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LoadingView.setContext(RegisterActivity.this);
        LoadingView.setMessage("認證碼寄送中...");
        register_cover_bg = findViewById(R.id.register_cover_bg);
        register_info_layout = findViewById(R.id.register_info_layout);
        toastMessage = new ToastMessageDialog(this);
        initTextView();
        initButton();
        initImage();
        initEditText();
        initToolbar();
    }

    private void initTextView() {
        register_txt_account = findViewById(R.id.register_txt_account);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("註冊");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initButton() {
        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        register_btn_gvcode = findViewById(R.id.register_btn_gvcode);
        register_btn_gvcode.setOnClickListener(this);

    }

    private void initEditText() {
        register_edit_account = findViewById(R.id.register_edit_account);
        register_edit_password = findViewById(R.id.register_edit_password);
    }

    private void initImage() {
        register_img_mobile = findViewById(R.id.register_img_mobile);
        register_img_mobile.setOnClickListener(this);
        register_img_email = findViewById(R.id.register_img_email);
        register_img_email.setOnClickListener(this);
        register_img_fb = findViewById(R.id.register_img_fb);
        register_img_fb.setOnClickListener(this);
        register_img_google = findViewById(R.id.register_img_google);
        register_img_google.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.register_button:
                if (vcode != null) {
                    if (vcode.toString().equals(register_edit_password.getText().toString())) {
                        Intent intent = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("vcode", vcode);
                        intent.putExtra("account", register_edit_account.getText().toString());
                        startActivity(intent);
                    } else {
                        toastMessage.setMessageText("請確認您輸入的驗證碼是否正確");
                        toastMessage.confirm();
                    }
                } else {
                    toastMessage.setMessageText("請先取得驗證碼後再進行下一步");
                    toastMessage.confirm();
                }
                break;
            case R.id.register_btn_gvcode:
                switch (type) {
                    case 1:
                        if (register_edit_account.getText().toString().matches("09[0-9]{8}")) {
                            sendApi();
                        } else {
                            toastMessage.setMessageText("手機號碼格式有誤");
                            toastMessage.confirm();
                        }

                        break;
                    case 2:
                        if (register_edit_account.getText().toString().matches("[\\w-.]+@[\\w-]+(.[\\w_-]+)+")) {
                            sendApi();
                        } else {
                            toastMessage.setMessageText("信箱格式有誤");
                            toastMessage.confirm();
                        }
                        break;
                }

                break;
            case R.id.register_img_mobile:
                type = 1;
                register_info_layout.setVisibility(View.VISIBLE);
                register_txt_account.setText("手機號碼");
                register_edit_account.setHint("請輸入手機號碼");
                register_edit_account.setInputType(InputType.TYPE_CLASS_PHONE);
                register_edit_account.setFocusableInTouchMode(true);
                register_edit_account.setFocusable(true);
                register_edit_account.requestFocus();
                register_edit_account.setText(null);
                register_edit_password.setText(null);
                register_edit_account.setEnabled(true);
                register_edit_password.setEnabled(true);
                register_button.setEnabled(true);
                register_btn_gvcode.setEnabled(true);
                vcode = null;

                break;
            case R.id.register_img_email:
                type = 2;
                register_info_layout.setVisibility(View.VISIBLE);
                register_txt_account.setText("電子信箱");
                register_edit_account.setHint("請輸入信箱帳號");
                register_edit_account.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                register_edit_account.setFocusableInTouchMode(true);
                register_edit_account.setFocusable(true);
                register_edit_account.requestFocus();
                register_edit_account.setText(null);
                register_edit_password.setText(null);
                register_edit_account.setEnabled(true);
                register_edit_password.setEnabled(true);
                register_button.setEnabled(true);
                register_btn_gvcode.setEnabled(true);
                vcode = null;
                break;
            case R.id.register_img_fb:
                type = 3;
                register_info_layout.setVisibility(View.INVISIBLE);
                register_cover_bg.setVisibility(View.VISIBLE);
                quickLoginFB();
                break;
            case R.id.register_img_google:
                type = 4;
                register_info_layout.setVisibility(View.INVISIBLE);
                register_cover_bg.setVisibility(View.VISIBLE);
                quickLoginGoogle();

                break;
        }
    }

    private void sendApi() {
        //收鍵盤
        ((InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        LoadingView.show(getCurrentFocus());
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new MemberJsonData().gvcode(type, "886", register_edit_account.getText().toString());
            }

            @Override
            public void runUiThread(JSONObject json) {
                try {
                    LoadingView.hide();
                    boolean success = json.getBoolean("Success");
                    if (success) {
                        vcode = json.getString("Data");
                        register_edit_account.setFocusable(false);
                    }
                    toastMessage.setMessageText(json.getString("Message"));
                    toastMessage.confirm();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        //loginManager.logOut();
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
                        register_cover_bg.setVisibility(View.INVISIBLE);
                        try {
                            if (response.getConnection().getResponseCode() == 200) {
                                /**
                                 * 值在這裡取得
                                 */
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String gender = object.getString("gender");
                                String birthday = object.getString("birthday");
                                String photo = "https://graph.facebook.com/" + id + "/picture?width=" + 400 + "&height=" + 400;
                                /*
                                Log.e(TAG, "Facebook id:" + object);
                                Log.e(TAG, "Facebook id:" + id);
                                Log.e(TAG, "Facebook name:" + name);
                                Log.e(TAG, "Facebook email:" + email);
                                Log.e(TAG, "Facebook gender:" + gender);
                                Log.e(TAG, "Facebook birthday:" + birthday);
                                */
                                Intent intent = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("id", id);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                switch (gender) {
                                    case "male":
                                        intent.putExtra("gender", 1);
                                        break;
                                    case "female":
                                        intent.putExtra("gender", 2);
                                        break;
                                    default:
                                        intent.putExtra("gender", 0);
                                        break;
                                }

                                intent.putExtra("photo", photo);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // https://developers.facebook.com/docs/android/graph?locale=zh_TW
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
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
                register_cover_bg.setVisibility(View.INVISIBLE);
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
    private GoogleApiClient mGoogleApiClient;
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
        // [END configure_signin]
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)//為了取得性別..等
                    .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        }
                    } /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .build();
        }
        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        signIn();
    }

    private Intent handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            /*
            Log.e(TAG, "--------------------------------");
            Log.e(TAG, "getId: " + account.getId());
            Log.e(TAG, "getDisplayName: " + account.getDisplayName());
            Log.e(TAG, "getEmail: " + account.getEmail());
            Log.e(TAG, "getIdToken: " + account.getIdToken());
            // Log.e(TAG, "getAccount: " + account.getAccount());
            Log.e(TAG, "getPhotoUrl: " + account.getPhotoUrl());
            */
            // G+
            Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            /*
            Log.e(TAG, "--------------------------------");
            //    Log.e(TAG, "Display Name: " + person.getDisplayName());
            Log.e(TAG, "Gender: " + person.getGender());
            */
            // Log.e(TAG, "AboutMe: " + person.getAboutMe());
            //  Log.e(TAG, "Birthday: " + person.getBirthday());
            //   Log.e(TAG, "Current Location: " + person.getCurrentLocation());
            //  Log.e(TAG, "Language: " + person.getLanguage());
            Intent intent = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
            switch (person.getGender()) {
                case Person.Gender.MALE:
                    intent.putExtra("gender", 1);
                    break;
                case Person.Gender.FEMALE:
                    intent.putExtra("gender", 2);
                    break;
                case Person.Gender.OTHER:
                    intent.putExtra("gender", 0);
                    break;
            }
            intent.putExtra("type", type);
            intent.putExtra("id", account.getId());
            intent.putExtra("name", account.getDisplayName());
            intent.putExtra("email", account.getEmail());
            intent.putExtra("photo", account.getPhotoUrl().toString());
            return intent;
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
            register_cover_bg.setVisibility(View.INVISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Intent intent = handleSignInResult(task);
            if (intent != null)
                startActivity(intent);
        }
    }


}
