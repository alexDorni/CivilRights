package com.example.heroalex.copscivilslicense;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.example.heroalex.copscivilslicense.fragments.IndexFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hero Alex on 3/5/2018.
 */

public class ServiceLogin extends Service{

    private static final String JKEY_FIRST_NAME = "firstName";
    private static final String JKEY_COORDINATES = "gpscoordonates";
    private static final String JKEY_STATUS = "statusPoint";

    // TODO google maps ruta coordonate distante

    private LocationManager mLocationManager;
    private static final String ACTION_MAIN = "action";
    public static final int NOTIFICATION_ID = 543;

    public static boolean isServiceRunning = false;
    private static LocationListener mLocationListener;
    public static ArrayMap<String, ArrayMap<String, String>> mHashArrayDataFirebase;

    private List<UserFirebase> users = new ArrayList<>();

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

        //-------------------------------------------   UPDATE AUTOMAT LA GPS COORDONATES ---------------------------------------//////////////////

        if (intent != null && intent.getAction().equals("start")) {


            //  notification service
            if (MainActivity.mCopBool == false) {
                startServiceWithNotification();
            }
            else {
                if (MainActivity.mCopBool == true) {

                }
            }

                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                mLocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                        Log.d("background Location", mGPSCoordinates + " ceva");


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                        if (!TextUtils.isEmpty(IndexFragment.mUserUid))
                            mRootRef.child(IndexFragment.mUserUid).child("gpscoordonates").setValue(mGPSCoordinates);
                        else {
                            Toast.makeText(getApplicationContext(), IndexFragment.mUserUid, Toast.LENGTH_SHORT).show();
                            Log.d("failUpdateChild", IndexFragment.mUserUid);
                        }

                        FirebaseGetData();
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
                        Log.d("test", "GPS provider disabled");
                    }
                };

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
                } else {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                            0, mLocationListener);
                }

        }
        else
            stopMyService();
        return START_STICKY;

    }

    public void FirebaseGetData(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.addListenerForSingleValueEvent(
                new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            users.clear();
                            for (DataSnapshot dspUid : dataSnapshot.getChildren()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    UserFirebase user = new UserFirebase();
                                    user.setUid(dspUid.getKey());
                                    for (DataSnapshot dspVal : dspUid.getChildren()) {
                                        if (dspVal.getValue() == null) {
                                            Log.d("TAG", "DspVal null");
                                            continue;
                                        }
                                        if (dspVal.getKey().equals(JKEY_COORDINATES)) {
                                            user.setCoordinates(dspVal.getValue().toString());
                                        }
                                        if (dspVal.getKey().equals(JKEY_FIRST_NAME)) {
                                            user.setName(dspVal.getValue().toString());
                                        }
                                        if (dspVal.getKey().equals(JKEY_STATUS)) {
                                            user.setStatus(dspVal.getValue().toString());
                                        }
                                    }
                                    users.add(user);
                                }
                            }
                            if (!users.isEmpty()) {
                                EventBus.getDefault().post(new EventBusUsers(users));
                            } else {
                                Log.d("Tag", "User list empty");
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }



    public void startServiceWithNotification() {

        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(ACTION_MAIN);  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.putExtra("id", NOTIFICATION_ID);

        PendingIntent actionIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);




        Notification notification = new Notification.Builder(this)
                .setContentTitle("Notificare Protectie")
                .setContentText( getResources().getString(R.string.content_text))
                .setSmallIcon(R.drawable.ic_notification_stat_protection)
                .setContentIntent(actionIntent)
                .setOngoing(true)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFICATION_ID, notification);
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
}
