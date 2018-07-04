package com.example.heroalex.copscivilslicense;

import java.util.ArrayList;
import java.util.List;


class EventBusUsers {
    private List<UserFirebase> mHashArrayDataFirebase = new ArrayList<>();

    EventBusUsers(List<UserFirebase> mHashArrayDataFirebase){
        this.mHashArrayDataFirebase = mHashArrayDataFirebase;
    }

    List<UserFirebase> getmHashArrayDataFirebase() {
        return mHashArrayDataFirebase;
    }
}

