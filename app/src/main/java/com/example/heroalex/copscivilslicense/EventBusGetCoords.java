package com.example.heroalex.copscivilslicense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hero Alex on 7/3/2018.
 */

public class EventBusGetCoords {

    private List<UserFirebase> mHashArrayDataFirebase = new ArrayList<>();


    EventBusGetCoords(List<UserFirebase> mHashArrayDataFirebase){
        this.mHashArrayDataFirebase = mHashArrayDataFirebase;
    }

    List<UserFirebase> getmHashArrayDataFirebase() {
        return mHashArrayDataFirebase;
    }
}
