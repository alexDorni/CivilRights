package com.example.heroalex.copscivilslicense;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hero Alex on 3/5/2018.
 */

public class ServiceLogin extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String mGPSCoordinates = "";
    public static String mEmailName;

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
                mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                //Toast.makeText(RegisterCivil.this, mGPSCoordinates, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //Toast.makeText(RegisterCivil.this, "GPS provider enabled:" + provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Toast.makeText(RegisterCivil.this, "GPS provider disabled:" + provider, Toast.LENGTH_SHORT).show();
            }
        };
        this.mEmailName = MainActivity.mEmailName;
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");
        mRootRef.child(mEmailName).child("gpscoordonates").setValue(mGPSCoordinates);
        //incercat aci

        Toast.makeText(getBaseContext(), mGPSCoordinates, Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
