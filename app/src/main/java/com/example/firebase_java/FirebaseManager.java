package com.example.firebase_java;

import android.util.Log;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseManager {

    // This.Class
    private static FirebaseManager mFirebaseManager = null;
    public static FirebaseManager getFirebase() { // use on UI thread only!
        return mFirebaseManager;
    }

    // FirebaseAccount
    private static FirebaseAuth mAuth = null;
    public static FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    // bundling with FB or Google
    private static FirebaseUser mFirebaseUser = null;
    public static FirebaseUser getFirebaseUser() { // use on UI thread only!
        return mFirebaseUser;
    }
    public void setFirebaseUser(FirebaseUser mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    // Facebook Token Class
    private static FacebookToken mFacebookToken = null;
    public static FacebookToken getFacebookLogin() {
        return mFacebookToken;
    }

    // Facebook Button CallbackManger
    private static CallbackManager mCallbackManager = null;
    public static CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    // GoogleToken Class
    private static GoogleToken mGoogleToken = null;
    public static GoogleToken getGoogleLogin() {
        return mGoogleToken;
    }

    // Firebase SignFlag
    private static boolean mSignFlag = false;
    public static boolean getSignFlag() {
        return mSignFlag;
    }

    // Init
    public static void FireBaseRegister() {

        // Initialize FacebookToken Login button
        mCallbackManager = CallbackManager.Factory.create();

        mFirebaseManager = new FirebaseManager();

        mFacebookToken = new FacebookToken();

        mGoogleToken = new GoogleToken();
    }

    public boolean CheckLogin() {
        // Initialize FirebaseManager Auth
        mAuth = FirebaseAuth.getInstance();

        Log.e("TAG", "Print mAuth : " + mAuth);

        if (mAuth != null) {

            if (mAuth.getCurrentUser() != null) {
                mFirebaseUser = (mAuth.getCurrentUser());

                Log.e("TAG", "Print User : " + mFirebaseUser);

                mSignFlag = true;
            }
        }

        return mSignFlag;
    }

    public void SignOut() {
        mSignFlag = false;
    }

    // or use GetSignFlag();
    public boolean IsSign() {
        return mSignFlag;
    }
}
