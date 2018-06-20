package com.example.heroalex.copscivilslicense;

import android.util.ArrayMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Hero Alex on 6/20/2018.
 */

public class EventBusUsers {
    public static ArrayMap<String, ArrayMap<String, String>> mHashArrayDataFirebase;


    public EventBusUsers(ArrayMap<String, ArrayMap<String, String>> mHashArrayDataFirebase){
        this.mHashArrayDataFirebase = mHashArrayDataFirebase;
    }

    public EventBusUsers() { }

    public static ArrayMap<String, ArrayMap<String, String>> getmHashArrayDataFirebase() {
        return mHashArrayDataFirebase;
    }
}

