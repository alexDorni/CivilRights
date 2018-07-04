package com.example.heroalex.copscivilslicense;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.ArrayMap;
import android.util.Log;

import com.example.heroalex.copscivilslicense.fragments.BaseFragment;
import com.example.heroalex.copscivilslicense.fragments.CopAcceptDialogFragment;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Collections;
import java.util.Comparator;
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
    public static boolean mSuccCopMinim = false;

    public static boolean dialogStatus = false;

    private double[] coordonatesGPSCop;
    public static String globalUidCop, globalUidCivil;


    private List<UserFirebase> usersFirebaseArrayCoordonates = new ArrayList<>();
    private static final String JKEY_FIRST_NAME = "firstName";
    private static final String JKEY_COORDINATES = "gpscoordonates";
    private static final String JKEY_STATUS = "statusPoint";
    public boolean unickDialog = false;
    public static boolean unickDialogStatic = false;

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

            for (final UserFirebase user : mapArrayMapUsers) {
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

                if (user.getStatus().equals("2")) {
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_on_resolve);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);


                    FirebaseUser userFirebaseCop = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    mRootRef.child(userFirebaseCop.getUid()).child(JKEY_COORDINATES).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            mRootRef.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            boolean okCop = false;
                                            usersFirebaseArrayCoordonates.clear();
                                            if (dataSnapshot != null) {
                                                // verificam daca uid ul civilului e egal cu firstName ul unui politist
                                                for (DataSnapshot usersFireBaseCivilUid : dataSnapshot.getChildren()) {

                                                    for (DataSnapshot usersFireBase : dataSnapshot.getChildren())
                                                        for (DataSnapshot usertFirebaseData : usersFireBase.getChildren()) {
                                                            if (usertFirebaseData.getKey().equals(JKEY_FIRST_NAME)) {
                                                                if (usersFireBaseCivilUid.getKey().equals(usertFirebaseData.getValue())) {
                                                                    okCop = true;
                                                                }
                                                            }
                                                            if (okCop == true) {
                                                                if (usertFirebaseData.getKey().equals(JKEY_COORDINATES)) {
                                                                    UserFirebase userFirebase = new UserFirebase();

                                                                    // cop coordonates
                                                                    userFirebase.setCoordinates(usertFirebaseData.getValue().toString());
                                                                    usersFirebaseArrayCoordonates.add(userFirebase);

                                                                    // civil coordonates
                                                                    for (DataSnapshot civilGPS : usersFireBaseCivilUid.getChildren()) {
                                                                        if (civilGPS.getKey().equals(JKEY_COORDINATES)) {

                                                                            UserFirebase userFirebase1 = new UserFirebase();
                                                                            userFirebase1.setCoordinates(civilGPS.getValue().toString());
                                                                            usersFirebaseArrayCoordonates.add(userFirebase1);
                                                                            okCop = false;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                }
                                                for (int i = 0; i < usersFirebaseArrayCoordonates.size(); ++i) {
                                                    Log.d("coords1", String.valueOf(usersFirebaseArrayCoordonates.get(i).getCoordinates()[0])
                                                            + String.valueOf(usersFirebaseArrayCoordonates.get(i + 1).getCoordinates()[0]));
                                                    break;
                                                }
                                                if (!usersFirebaseArrayCoordonates.isEmpty()) {
                                                    // apel EventBus
                                                    Log.d("TagList", "User list empty");
                                                    EventBus.getDefault().post(new EventBusGetCoords(usersFirebaseArrayCoordonates));
                                                } else {
                                                    Log.d("TagList", "User list empty");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    // set route
                    for (int i = 0; i < usersFirebaseArrayCoordonates.size(); ++i) {
                        Log.d("coords", String.valueOf(usersFirebaseArrayCoordonates.get(i).getCoordinates()[0])
                                + " : " + String.valueOf(usersFirebaseArrayCoordonates.get(i).getCoordinates()[1])
                                + "\n" + String.valueOf(usersFirebaseArrayCoordonates.get(i + 1).getCoordinates()[0])
                                + " : " + String.valueOf(usersFirebaseArrayCoordonates.get(i + 1).getCoordinates()[1]));

                        setRouteBetweenTwoPoints(new LatLng(usersFirebaseArrayCoordonates.get(i).getCoordinates()[0],
                                        usersFirebaseArrayCoordonates.get(i).getCoordinates()[1]),
                                new LatLng(usersFirebaseArrayCoordonates.get(i + 1).getCoordinates()[0],
                                        usersFirebaseArrayCoordonates.get(i + 1).getCoordinates()[1])
                        );
                        break;
                    }
                }


                if (user.getStatus().equals("1")) {
                    globalUidCivil = user.getUid();
                    iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_civil_status_danger);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLngGPS)
                            .title(user.getName())
                            .icon(iconMarker);
                    mMap.addMarker(markerOptions);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }

                    CopAcceptDialogFragment dialogFragment = new CopAcceptDialogFragment();
                    for (UserFirebase userCop : mapArrayMapUsers) {
                        if (userCop.getStatus().equals("-1"))  {

                            if (dialogFragment.)
                                dialogFragment.show(getFragmentManager(), "TAG");

                        }
                    }
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

    @Subscribe
    public void receiveArrayMapOfTwo(EventBusGetCoords eventBusGetCoords){
        this.usersFirebaseArrayCoordonates = eventBusGetCoords.getmHashArrayDataFirebase();
    }

    @Subscribe
    public void receiveUnickDialog(EventBusGetUnick eventBusGetUnick){
        this.unickDialog = eventBusGetUnick.getUnickMark();
    }


}
