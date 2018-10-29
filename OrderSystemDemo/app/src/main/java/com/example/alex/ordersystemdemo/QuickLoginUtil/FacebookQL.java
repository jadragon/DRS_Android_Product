package com.example.alex.ordersystemdemo.QuickLoginUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.StudentApi;
import com.example.alex.ordersystemdemo.GlobalVariable;
import com.example.alex.ordersystemdemo.StudentAcitvity;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;
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

public class FacebookQL {

    private static final String TAG = FacebookQL.class.getSimpleName();
    private Context context;
    // FB
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    public FacebookQL(Context context) {
        this.context = context;
        // init facebook
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
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

    public void checkLogin() {
        // method_1.判斷用戶是否登入過
        if (Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            //  Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();
            //          Log.d(TAG, "Facebook userPhoto: " + userPhoto);
//            Log.d(TAG, "Facebook id: " + id);
//            Log.d(TAG, "Facebook name: " + name);
            sendApi(id, name, "");
        } else {
            loginFB();
        }
    }

    public void loginFB() {
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
        loginManager.logInWithReadPermissions((Activity) context, permissions);
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
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                //      String gender = object.getString("gender");
//                                String birthday = object.getString("birthday");
                                //      String picture = "https://graph.facebook.com/" + id + "/picture?width=" + 300 + "&height=" + 300;
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

                                sendApi(id, name, email);
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

    private void sendApi(final String id, final String name, final String email) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new StudentApi().student_login(id, name, email, "0");
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    GlobalVariable gv = (GlobalVariable) context.getApplicationContext();
                    gv.setType(0);
                    gv.setToken(AnalyzeUtil.getToken(jsonObject, 0));
                    context.startActivity(new Intent(context, StudentAcitvity.class));
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
