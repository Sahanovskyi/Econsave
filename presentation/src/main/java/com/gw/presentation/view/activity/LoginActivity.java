package com.gw.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gw.presentation.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements
		GoogleApiClient.OnConnectionFailedListener {


	private static final String TAG = "LoginActivity";
	private static final int RC_SIGN_IN = 9001;
	@BindView(R.id.sign_in_google_btn)
	SignInButton signInButton;
	private GoogleApiClient mGoogleApiClient;
	// [START declare_auth]
	private FirebaseAuth mAuth;
	private CallbackManager callbackManager;
	// [END declare_auth]

	public static Intent getCallingIntent(Context context) {
		return new Intent(context, LoginActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		mAuth = FirebaseAuth.getInstance();


		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);

		callbackManager = CallbackManager.Factory.create();


		final LoginButton loginButton = (LoginButton) findViewById(R.id.sign_in_fb_btn);
		loginButton.setReadPermissions(Arrays.asList(EMAIL, public_profile));
		// If you are using in a fragment, call loginButton.setFragment(this);

		// Callback registration
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
				Log.d(TAG, "onSuccess() called with: accessToken = [" + loginResult.getAccessToken().getToken() + "]");
			}

			@Override
			public void onCancel() {
				// App code
				Log.d(TAG, "onCancel() called");
			}

			@Override
			public void onError(FacebookException exception) {
				// App code
				Log.d(TAG, "onError() called with: exception = [" + exception + "]");
			}
		});


	}


	private static final String EMAIL = "email";
	private static final String public_profile = "public_profile";


	@Override
	public void onStart() {
		super.onStart();
		if (getIntent().getBooleanExtra("SIGNING_OUT", false)) {
			signOut();
		} else {
			// Check if user is signed in (non-null) and update UI accordingly.
			FirebaseUser currentUser = mAuth.getCurrentUser();
			updateUI(currentUser);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = result.getSignInAccount();

				firebaseAuthWithGoogle(account);

			} else {
				// Google Sign In failed, update UI appropriately
				updateUI(null);
			}
		}
	}
	// [END auth_with_google]

	// [START auth_with_google]
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
		// [START_EXCLUDE silent]
		showProgressDialog();
		// [END_EXCLUDE]

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							updateUI(user);
						} else {
							Auth.GoogleSignInApi.signOut(mGoogleApiClient);
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(LoginActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
							updateUI(null);
						}

					}
				});
	}
	// [END signin]

//    private void signOut() {
//        // Firebase sign out
//        mAuth.signOut();
//
//        // Google sign out
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                });
//    }
//
//    private void revokeAccess() {
//        // Firebase sign out
//        mAuth.signOut();
//
//        // Google revoke access
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                });
//    }

	@OnClick(R.id.sign_in_google_btn)
	public void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void updateUI(FirebaseUser user) {

		hideProgressDialog();
		if (user != null) {
			startActivity(MainActivity.getCallingIntent(this));
			finish();
		} else {

		}
	}

	//    @Override
//    public void onDestroy() {
//        if (mGoogleApiClient.isConnected())
//            mGoogleApiClient.disconnect();
//        super.onDestroy();
//    }
	private void signOut() {
		// Firebase sign out
		mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
			@Override
			public void onConnected(@Nullable Bundle bundle) {

				FirebaseAuth.getInstance().signOut();
				if (mGoogleApiClient.isConnected()) {
					Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
						@Override
						public void onResult(@NonNull Status status) {
							if (status.isSuccess()) {
								Log.d(TAG, "User Logged out");
//                                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
							}
						}
					});
				}
			}

			@Override
			public void onConnectionSuspended(int i) {
				Log.d(TAG, "Google API Client Connection Suspended");
			}
		});
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}
}
