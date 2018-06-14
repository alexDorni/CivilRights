package com.example.heroalex.copscivilslicense;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.heroalex.copscivilslicense.fragments.IndexFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 3/5/2018.
 */

public class ServiceLogin extends Service{



    // TODO google maps ruta coordonate distante

    private LocationManager mLocationManager;
    private static final String ACTION_MAIN = "action";
    private static final String mNotificationAction = "Protection ON";
    private static final String ALARM_ACTION = "ceva";
    static final int NOTIFICATION_ID = 543;

    public static boolean isServiceRunning = false;
    public static String mUserUid;
    private static LocationListener mLocationListener;

    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // do your jobs here//-------------------------------------------   UPDATE AUTOMAT LA GPS COORDONATES ---------------------------------------//////////////////
        if (intent != null && intent.getAction().equals("start")) {


            //  APELUL SERVICIULUI MASII..
            startServiceWithNotification();

            mUserUid = IndexFragment.mUserUid;
            Toast.makeText(getApplicationContext(), mUserUid, Toast.LENGTH_SHORT).show();

            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                    Log.d("background Location", mGPSCoordinates + " ceva");

                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");

                    if (!TextUtils.isEmpty(mUserUid))
                        mRootRef.child(mUserUid).child("gpscoordonates").setValue(mGPSCoordinates);
                    else
                        Toast.makeText(getApplicationContext(), mUserUid, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                    Log.d("test", "GPS provider enabled");
                }

                @Override
                public void onProviderDisabled(String provider) {

                    Log.d("test","GPS provider disabled" );
                }


            };


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
            }else {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        0, mLocationListener);
            }
        }
        else
            stopMyService();
        return START_STICKY;

        /*
            Trebuie facut notificari pentru sistem sa nu ti-l omoare
            link Rares
            https://stackoverflow.com/questions/42126979/cannot-keep-android-service-alive-after-app-is-closed
         */

    }


///////////////////////////////////////////     PROBLEMA MAJORA /////////////////////


    public void startServiceWithNotification() {

        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        Intent broadcastIntent = new Intent(getApplicationContext(), NotificationBroadcast.class);

        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0,
                broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationIntent.setAction(ACTION_MAIN);  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        Notification notification = new Notification.Builder(this)
                .setContentTitle("Notificare")
                .setContentText( getResources().getString(R.string.content_text))
                .setSmallIcon(R.drawable.ic_stat_protection)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_state_danger, "ALERTA POLITIE", actionIntent)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
        notificationManager.notify(1, notification);
    }

    private void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }

    // CACATURI
/*
    public void showActionButtonNotification(){

        Intent alertIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, alertIntent, 0);

        Intent broadcastIntent = new Intent(getApplicationContext(), NotificationBroadcast.class);

        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0,
                                                                broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Assist notification")
                .setContentText(getResources().getString(R.string.content_text))
                .setSmallIcon(R.drawable.ic_stat_protection)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_state_danger, "ALERTA POLITIE", actionIntent)
                .setOngoing(true)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);

        notificationManager.notify(1,  notification);
    }
*/

    // SI MAI MULTE CACATURI
    /*
    public static Intent getNotificationIntent(){
        Intent intent = new Intent(getAppContext(), ServiceLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static Context getAppContext() {
        return ServiceLogin.context;
    }

    private void processIntentAction(Intent intent){
        if (intent.getAction() != null){
            switch (intent.getAction()){
                case ALARM_ACTION:
                    Toast.makeText(this, "CEVAAA", Toast.LENGTH_SHORT).show();
            }
        }
    }

*/
    // incercare butoane de volum
   /*
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            IndexFragment.mEditButtVol.setText(keyCode);
            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            IndexFragment.mEditButtVol.setText(keyCode);
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    */
}
