package com.example.heroalex.copscivilslicense;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 1/8/2018.
 */

public class FireBaseData extends Application{

    @Override
    public void onCreate() {
        super.onCreate();


        Firebase.setAndroidContext(this);
    }
}
