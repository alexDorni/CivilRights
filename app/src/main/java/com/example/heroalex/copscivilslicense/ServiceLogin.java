package com.example.heroalex.copscivilslicense;

import android.*;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 3/5/2018.
 */

public class ServiceLogin extends Service {



    // TODO google maps ruta coordonate distante

    private LocationManager mLocationManager;
    private static final String ACTION_MAIN = "action";
    private static final String mNotificationAction = "Protection ON";
    static final int NOTIFICATION_ID = 543;

    public static boolean isServiceRunning = false;
    public static String mEmailName;
    private static LocationListener mLocationListener;


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
            startServiceWithNotification();
            mEmailName = MainActivity.mEmailName;
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                    Log.d("background Location", mGPSCoordinates + " ceva");

                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");

                    if (!TextUtils.isEmpty(mEmailName))
                        mRootRef.child(mEmailName).child("gpscoordonates").setValue(mGPSCoordinates);
                    else
                        Toast.makeText(getApplicationContext(), mEmailName, Toast.LENGTH_SHORT).show();
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
//
            //incercat aci
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

    void startServiceWithNotification() {

        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(ACTION_MAIN);  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        Notification notification = new Notification.Builder(this)
                .setContentTitle("Notificare")
                .setContentText( getResources().getString(R.string.content_text))
                .setSmallIcon(R.drawable.ic_stat_protection)
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }

    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }
}
