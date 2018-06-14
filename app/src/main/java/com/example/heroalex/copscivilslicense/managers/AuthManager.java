package com.example.heroalex.copscivilslicense.managers;


import android.content.Context;
import android.content.Intent;

import com.example.heroalex.copscivilslicense.ServiceLogin;
import com.example.heroalex.copscivilslicense.fragments.IndexFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthManager {

    private static AuthManager sInstance;
    private FirebaseAuth mAuth;

    public static synchronized AuthManager getInstance() {
        if (sInstance == null) {
            sInstance = new AuthManager();
        }
        return sInstance;
    }

    private AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void startUserRegister(String email, String password, OnCompleteListener<AuthResult> completeListener, OnFailureListener failureListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(completeListener)
                .addOnFailureListener(failureListener);
    }

    public void startUserLogin(String email, String password, OnCompleteListener<AuthResult> completeListener, OnFailureListener failureListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(completeListener)
                .addOnFailureListener(failureListener);
    }

    public void startUserResetPassword(String email, OnCompleteListener<Void> completeListener, OnFailureListener failureListener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(completeListener)
                .addOnFailureListener(failureListener);
    }

    public void updateUserName(String name) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build();
            user.updateProfile(profileUpdates);
        }
    }

    public void startLogOut() {
        mAuth.signOut();
    }


}
