package com.example.firebase_java;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleToken {

    private static AuthCredential mToken;

    public static AuthCredential getToken() {
        return mToken;
    }

    public AuthCredential handleFacebookAccessToken(String idToken) {
        Log.d("TAG", "handleFacebookAccessToken:" + idToken);

        mToken = GoogleAuthProvider.getCredential(idToken, null);

        return mToken;
    }
}
