package com.example.firebase_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView mtxtUserName, mtxtUserEmail;
    Button mLogoutButton, mFBLoginButton, mGoogleLogIn;

    // Google requestCode
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    FireStore mFirStore = new FireStore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        FirebaseManager.FireBaseRegister();

        mtxtUserName = findViewById(R.id.textView);
        mtxtUserEmail = findViewById(R.id.textView2);
        mLogoutButton = findViewById(R.id.logout_bt);
        mFBLoginButton = findViewById(R.id.fb_sign_in);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager.getFirebaseAuth().getInstance().signOut();

                Log.e("TAG", String.valueOf(FirebaseManager.getFirebaseAuth().getInstance().getCurrentUser()));
                if (FirebaseManager.getFirebaseAuth().getInstance().getCurrentUser() == null) {
                    Log.e("TAG", "Already Logout");

                    mtxtUserName.setText("Logout");
                    mtxtUserEmail.setText("Logout");
                }

//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });

        // Initialize FacebookToken Login button
        LoginManager.getInstance().registerCallback(FirebaseManager.getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("TAG", "facebook:onSuccess:" + loginResult);
                FirebaseManager.getFacebookToken().handleFacebookAccessToken(loginResult.getAccessToken());

                // get token
                if (FirebaseManager.getFacebookToken().getToken() != null) {
                    FacebookSignIn(FirebaseManager.getFacebookToken().getToken());
                }
            }

            @Override
            public void onCancel() {
                Log.e("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("TAG", "facebook:onError", exception);
            }
        });


        mFBLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        // init google Login button
        mGoogleLogIn = findViewById(R.id.google_sign_in);
        mGoogleLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleRegister();
            }
        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Log.d("TAG", "onLogout catched");
                    FirebaseManager.getFirebase().SignOut();

                    updateUI();//This is my code
                }
            }
        };

        mFirStore.initDB();

        mFirStore.SearchData();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FirebaseManager.getFirebase().CheckLogin() == true) {
            Log.d("TAG", "Already get User Login");

            if (FirebaseManager.getFirebaseUser().isEmailVerified() == true) {
                updateUI();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the FacebookToken SDK
        FirebaseManager.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Set Token
                FirebaseManager.getGoogleToken().handleGoogleAccessToken(account.getIdToken());

                // get token
                if (FirebaseManager.getGoogleToken().getToken() != null) {
                    GoogleSignIn();
                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void FacebookSignIn(AuthCredential FBToken) {
        FirebaseManager.getFirebaseAuth().signInWithCredential(FBToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseManager.getFirebase().setFirebaseUser(FirebaseManager.getFirebaseAuth().getCurrentUser());

                            Log.e("TAG", "Email :" + String.valueOf(FirebaseManager.getFirebaseUser().isEmailVerified()));
                            if (FirebaseManager.getFirebaseUser().isEmailVerified() == false) {
                                Log.e("TAG", "Email unVerified");
                                FirebaseManager.getFirebaseUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "Email sent.");
                                                }
                                            }
                                        });
                            } else if (FirebaseManager.getFirebaseUser().isEmailVerified() == true) {
                                Log.e("TAG", "Email Verified = true");

                                FirebaseManager.getFirebase().CheckLogin();

                                updateUI();
                            }

                            Log.e("TAG", "currentUser : " + FirebaseManager.getFirebaseUser());

                        } else {
                            // If sign in fails
                            Log.e("TAG", "currentUser : null");

                        }
                    }
                });
    }

    // 顯示 User 資訊
    private void updateUI() {

        if (FirebaseManager.getSignFlag() == true) {

            mtxtUserName.setText(FirebaseManager.getFirebaseUser().getDisplayName());
            Log.d("Info", "UserName : $FirebaseManager.getFirebaseUser().getDisplayName()");

            mtxtUserEmail.setText(FirebaseManager.getFirebaseUser().getEmail());
            Log.d("Info", "UserEmail : $FirebaseManager.getFirebaseUser().getEmail()");

        } else {

            mtxtUserName.setText("Logout");

            mtxtUserEmail.setText("Logout");
            Log.d("TAG", "Logout");

        }

    }

    private void GoogleRegister() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void GoogleSignIn() {
        FirebaseManager.getFirebaseAuth().signInWithCredential(GoogleToken.getToken())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseManager.getFirebase().setFirebaseUser(FirebaseManager.getFirebaseAuth().getCurrentUser());

                            if (FirebaseManager.getFirebaseUser().isEmailVerified() == false) {
                                Log.e("TAG", "Email unVerified");
                                FirebaseManager.getFirebaseUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "Email sent.");
                                                }
                                            }
                                        });
                            } else if (FirebaseManager.getFirebaseUser().isEmailVerified() == true) {
                                Log.e("TAG", "Email Verified = true");

                                FirebaseManager.getFirebase().CheckLogin();

                                updateUI();
                            }
                        } else {
                            // If sign in fails
                            Log.d("TAG", "currentUser : null");
                        }
                    }
                });
    }
}