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
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_danger);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);

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
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(47.1257305, 27.5662089), new LatLng(47.1558029, 27.5836955))
                    .width(5)
                    .color(Color.RED));
            //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngGPS));
            // distanta dintre 2 puncte
            //  Location.distanceBetween();
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest){

        // value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;

        // value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // value of sensor
        String sensor = "sensor=false";

        // modul pentru a gasi directia
        String mode = "mode=driving";

        // API key
        String api_key_route = "key=" + "AIzaSyAdGqNtCnWarJid1B3gZf9GvusOiBF-zIY";

        // build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + api_key_route;

        // output format
        String output = "json";

        //str =  AIzaSyAdGqNtCnWarJid1B3gZf9GvusOiBF-zIY
        // url request
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
    }

    private GeoApiContext getGeoApiContext(){
        GeoApiContext geoApiContext = new GeoApiContext();

        return geoApiContext.setQueryRateLimit(3)
                .setApiKey("AIzaSyAdGqNtCnWarJid1B3gZf9GvusOiBF-zIY")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }
    private void DirectionFromPoints(String origin, String destination){
        DateTime now = new DateTime();
        try {
            DirectionsResult result = DirectionsApi.newRequest(getGeoApiContext())
                    .mode(TravelMode.DRIVING).origin(origin)
                    .destination(destination).departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    @Subscribe
    public void receiveArrayMap(EventBusUsers eventBusUsers) {
        this.mapArrayMapUsers = eventBusUsers.getmHashArrayDataFirebase();
        updateMap();
    }



}
