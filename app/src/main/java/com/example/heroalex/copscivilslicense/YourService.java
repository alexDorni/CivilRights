package com.example.heroalex.copscivilslicense;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Hero Alex on 2/22/2018.
 */

public class YourService  extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //jobs
        return  super.onStartCommand(intent, flags, startId);
    }
}
