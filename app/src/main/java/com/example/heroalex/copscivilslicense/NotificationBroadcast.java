package com.example.heroalex.copscivilslicense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 6/14/2018.
 */

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // AR TREBUI SA FACA UPDATE LA BAZA DE DATE, DAR NICI MACAR NU MA LASA SA INTRU IN CIVILS.....
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {


            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = mRootRef.child(user.getUid());

            usersRef.child("statusPoint").setValue("1");
        }
    }
}
