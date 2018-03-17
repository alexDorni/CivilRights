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
    private static String mGPSCoordinates = "";
    public static String mEmailName;

    private static LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
            Log.d("background Location", mGPSCoordinates + " ceva");
            mEmailName = MainActivity.mEmailName;
            final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");
            mRootRef.child(mEmailName).child("gpscoordonates").setValue(mGPSCoordinates);

            //Toast.makeText(RegisterCivil.this, mGPSCoordinates, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            //Toast.makeText(, "GPS provider enabled:" + provider, Toast.LENGTH_SHORT).show();
            Log.d("test", "GPS provider enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Toast.makeText(RegisterCivil.this, "GPS provider disabled:" + provider, Toast.LENGTH_SHORT).show();
            Log.d("test","GPS provider disabled" );
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here//-------------------------------------------   UPDATE AUTOMAT LA GPS COORDONATES ---------------------------------------//////////////////
        Log.d("Enter1", mGPSCoordinates);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        this.mEmailName = MainActivity.mEmailName;
//        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");
//        mRootRef.child(mEmailName).child("gpscoordonates").setValue(mGPSCoordinates);
        //incercat aci
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
        }else {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
        }
        Toast.makeText(getBaseContext(), mGPSCoordinates, Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
