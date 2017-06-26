package com.gw.presentation.view.activity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.AuthComponent;

public class StartActivity extends BaseActivity {

    private static final String TAG = "StartActivity";

    private AuthComponent authComponent;

    private FirebaseAuth mAuth;
  //  FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        mAuth = FirebaseAuth.getInstance();



//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(MainActivity.getCallingIntent(this));
        }
        else {
            startActivity(LoginActivity.getCallingIntent(this));
        }
        finish();
 //       mAuth.addAuthStateListener(mAuthListener);
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(!executer.isCancelled())
//            executer.cancel(true);
//    }
//
//    private class LoginExecuter extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//            if (opr.isDone()) {
//                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//                // and the GoogleSignInResult will be available instantly.
//                Log.d(TAG, "Got cached sign-in");
//                GoogleSignInResult result = opr.get();
//                handleSignInResult(result);
//            } else {
//                // If the user has not previously signed in on this device or the sign-in has expired,
//                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//                // single sign-on will occur in this branch.
//                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                    @Override
//                    public void onResult(GoogleSignInResult googleSignInResult) {
//                        handleSignInResult(googleSignInResult);
//                    }
//                });
//            }
//            return null;
//        }
//
//
//
//    }


//
//
//    // [START handleSignInResult]
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        Intent intent;
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//     //       GoogleSignInAccount acct = result.getSignInAccount();
//            intent = MainActivity.getCallingIntent(this);
//
//        } else {
//            // Signed out, show unauthenticated UI.
//            intent = LoginActivity.getCallingIntent(this);
//        }
//        startActivity(intent);
//        finish();
//    }
//    // [END handleSignInResult]
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//        startActivity(LoginActivity.getCallingIntent(this));
//        finish();
//    }
//

//    // [START onActivityResult]
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
    // [END onActivityResult]

//    private void initializeInjector() {
//        this.authComponent = DaggerActivityComponent.builder()
//                .applicationComponent(getApplicationComponent())
//                .activityModule(getActivityModule())
//                .authModule(getAuthModue())
//                .build();
//    }
}
