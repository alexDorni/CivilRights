package com.example.heroalex.copscivilslicense;

import android.*;
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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 3/5/2018.
 */

public class ServiceLogin extends Service {

    private LocationManager mLocationManager;
   // private static String mGPSCoordinates = "null";
    public static String mEmailName;
    private static LocationListener mLocationListener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here//-------------------------------------------   UPDATE AUTOMAT LA GPS COORDONATES ---------------------------------------//////////////////

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                Log.d("background Location", mGPSCoordinates + " ceva");
                mEmailName = MainActivity.mEmailName;
                final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");
                mRootRef.child(mEmailName).child("gpscoordonates").setValue(mGPSCoordinates);
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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
