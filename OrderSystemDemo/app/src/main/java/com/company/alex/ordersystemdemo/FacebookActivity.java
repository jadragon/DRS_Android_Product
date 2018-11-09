package com.company.alex.ordersystemdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FacebookActivity extends AppCompatActivity {

    private static final String TAG = FacebookActivity.class.getSimpleName();
    private ImageView mImgPhoto;
    private TextView mTextDescription;

    // FB
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        // init facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        findViewById(R.id.mImgFBBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook Login
                loginFB();
            }
        });
        findViewById(R.id.mImgLogoutBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook Logout
                loginManager.logOut();
              /*
                Glide.with(FacebookActivity.this)
                        .load(R.drawable.actmember)
                        //.crossFade()
                        .into(mImgPhoto);

*/
                mTextDescription.setText("");
            }
        });
        mImgPhoto = (ImageView) findViewById(R.id.mImgPhoto);
        mTextDescription = (TextView) findViewById(R.id.mTextDescription);

        // method_1.判斷用戶是否登入過
        if (Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();
//            Log.d(TAG, "Facebook userPhoto: " + userPhoto);
//            Log.d(TAG, "Facebook id: " + id);
//            Log.d(TAG, "Facebook name: " + name);
        }

        // method_2.判斷用戶是否登入過
        /*if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG, "Facebook getApplicationId: " + AccessToken.getCurrentAccessToken().getApplicationId());
            Log.d(TAG, "Facebook getUserId: " + AccessToken.getCurrentAccessToken().getUserId());
            Log.d(TAG, "Facebook getExpires: " + AccessToken.getCurrentAccessToken().getExpires());
            Log.d(TAG, "Facebook getLastRefresh: " + AccessToken.getCurrentAccessToken().getLastRefresh());
            Log.d(TAG, "Facebook getToken: " + AccessToken.getCurrentAccessToken().getToken());
            Log.d(TAG, "Facebook getSource: " + AccessToken.getCurrentAccessToken().getSource());
        }*/
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
                // 登入成功
                /* 可以取得相關資訊，這裡就請各位自行打印出來
                Log.d(TAG, "Facebook getApplicationId: " + loginResult.getAccessToken().getApplicationId());
                Log.d(TAG, "Facebook getUserId: " + loginResult.getAccessToken().getUserId());
                Log.d(TAG, "Facebook getExpires: " + loginResult.getAccessToken().getExpires());
                Log.d(TAG, "Facebook getLastRefresh: " + loginResult.getAccessToken().getLastRefresh());
                Log.d(TAG, "Facebook getToken: " + loginResult.getAccessToken().getToken());
                Log.d(TAG, "Facebook getSource: " + loginResult.getAccessToken().getSource());
                Log.d(TAG, "Facebook getRecentlyGrantedPermissions: " + loginResult.getRecentlyGrantedPermissions());
                Log.d(TAG, "Facebook getRecentlyDeniedPermissions: " + loginResult.getRecentlyDeniedPermissions());
                */
                // 透過GraphRequest來取得用戶的Facebook資訊
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (response.getConnection().getResponseCode() == 200) {
                                /**
                                                     * 值在這裡取得
                                                         */
                                long id = object.getLong("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                //      String gender = object.getString("gender");
//                                String birthday = object.getString("birthday");
                                String picture = "https://graph.facebook.com/" + id + "/picture?width=" + 300 +"&height=" + 300;
                            //    Log.e(TAG, "Facebook id:" + object);
//                                Log.e(TAG, "Facebook id:" + id);
//                                Log.e(TAG, "Facebook name:" + name);
//                                Log.e(TAG, "Facebook email:" + email);
                                //    Log.d(TAG, "Facebook gender:" + gender);
                         //       Log.d(TAG, "Facebook birthday:" + birthday);
                                /*
                                // 此時如果登入成功，就可以順便取得用戶大頭照
                                Profile profile = Profile.getCurrentProfile();
                                // 設定大頭照大小
                                Uri userPhoto = profile.getProfilePictureUri(300, 300);
                                */
                                    /*
                                Glide.with(FacebookActivity.this)
                                        .load(userPhoto.toString())
                                      //  .crossFade()
                                        .into(mImgPhoto);
                                    */
                        //        ImageLoader.getInstance().displayImage(picture, mImgPhoto);
                                mTextDescription.setText(String.format(Locale.TAIWAN, "Name:%s\nE-mail:%s", name, email));
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
                Log.d(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // 登入失敗
                Log.d(TAG, "Facebook onError:" + error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
