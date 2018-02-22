package com.example.heroalex.copscivilslicense;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Hero Alex on 2/22/2018.
 */

public class LoginStart extends Application {
    public void  onCreate(){
        super.onCreate();

        startService(new Intent(this, YourService.class));
    }
}
