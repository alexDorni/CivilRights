package com.example.heroalex.copscivilslicense;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Hero Alex on 3/21/2018.
 */

public class LocationServiceManager extends BroadcastReceiver {
    public static final String TAG = "LocationLoggerService";
    @Override
    public void onReceive(Context context, Intent intent) {
        // just make sure we are getting the right intent (better safe than sorry)
        if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
           ComponentName comp = null;
            // ComponentName comp = new ComponentName(context.getPackageName(), LocationLoggerService.class.getName());
            ComponentName service = context.startService(new Intent().setComponent(comp));
            if (null == service){
                // something really wrong here
                Log.d(TAG, "Could not start service " + comp.toString());
            }
        } else {
            Log.d(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}
