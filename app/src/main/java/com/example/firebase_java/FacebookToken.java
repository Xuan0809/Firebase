package com.example.firebase_java;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;

public class FacebookToken {

    private static AuthCredential mToken;

    public static AuthCredential getToken() {
        return mToken;
    }

    public AuthCredential handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        mToken = FacebookAuthProvider.getCredential(token.getToken());

        return mToken;

    }
}
