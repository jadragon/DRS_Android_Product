package com.company.alex.ordersystemdemo.QuickLoginUtil;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.Task;

public class GoogleQL {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private Context ctx;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;

    public GoogleQL(Context ctx) {
        this.ctx = ctx;
        /**
         * 設定Sign In 選項
         * */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        // [END configure_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)//為了取得性別..等
                .enableAutoManage((FragmentActivity) ctx /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(ctx, gso);
        // [END build_client]
        // [START customize_button]
        // Set the dimensions of the sign-in button.
        // [END customize_button]
    }

    public void onStart() {
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(ctx);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    public GoogleSignInAccount onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            return handleSignInResult(task);
        }
        return null;
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private GoogleSignInAccount handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            return account;
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            return null;
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((AppCompatActivity) ctx).startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

}