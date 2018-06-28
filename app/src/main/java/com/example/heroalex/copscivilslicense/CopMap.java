package com.example.heroalex.copscivilslicense;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hero Alex on 6/11/2018.
 */

public class CopMap extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = CopMap.class.getSimpleName();
    private GoogleMap mMap;

    public List<UserFirebase> mapArrayMapUsers = new ArrayList<>();

    private BitmapDescriptor iconMarker;

    @Override
    protected void onStart() {
        super.onStart();

        // subscriber
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cop_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent startIntent = new Intent(this, ServiceLogin.class);
        startIntent.setAction("start");
        startService(startIntent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        updateMap();


    }

    private void updateMap() {
        if (mapArrayMapUsers == null || mMap == null) {
            Log.e(TAG, "Users map is null");
            return;
        }
        LatLng latLngGPS = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMap.clear();

            for (UserFirebase user : mapArrayMapUsers) {
                if (user == null) {
                    Log.e(TAG, "User is null");
                    continue;
                }
                latLngGPS = new LatLng(user.getCoordinates()[0], user.getCoordinates()[1]);

                if (user.getStatus().equals("-2")) {
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_police_state_on_mission);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);
                }

                if (user.getStatus().equals("-1")) {
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_police_state_neutral);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);
                }

                if (user.getStatus().equals("0")) {
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_ok);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);
                }

                if (user.getStatus().equals("1")) {
                    double[] coordonatesGPSCivil = user.getCoordinates();
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_danger);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);

                    for (UserFirebase userCop : mapArrayMapUsers){
                        if(userCop.getStatus().equals("-1")){
                            double[] coordonatesGPSCop = userCop.getCoordinates();
                            setRouteBetweenTwoPoints(new LatLng(coordonatesGPSCivil[0], coordonatesGPSCivil[1]),
                                                     new LatLng(coordonatesGPSCop[0],coordonatesGPSCop[1]));

                        }
                    }
                }

                if (user.getStatus().equals("2")) {
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_on_resolve);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);
                }

            }
        }
    }

    private void setRouteBetweenTwoPoints(LatLng origin, LatLng dest){
        Object[] dataTransfer = new Object[4];
        dataTransfer[0] = mMap;
        dataTransfer[1] = getDirectionsUrl(origin, dest);
        dataTransfer[2] = new LatLng(origin.latitude, origin.longitude);
        dataTransfer[3] = new LatLng(dest.latitude, dest.longitude);

        GetDirectionsData getDirectionsData = new GetDirectionsData(this);
        getDirectionsData.execute(dataTransfer);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest){

        // value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;

        // value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // API key
        String api_key_route = "key=" + "AIzaSyBt6AlnROg_O0H_fSRwSeZXnoJAm9av9KE";

        // build the full param
        String param = str_org + "&" + str_dest +  "&" + api_key_route;

        // output format
        String output = "json";

        // url request
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
    }


    @Subscribe
    public void receiveArrayMap(EventBusUsers eventBusUsers) {
        this.mapArrayMapUsers = eventBusUsers.getmHashArrayDataFirebase();
        updateMap();
    }



}
