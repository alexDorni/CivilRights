package com.example.heroalex.copscivilslicense;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.heroalex.copscivilslicense.fragments.IndexFragment;
import com.example.heroalex.copscivilslicense.fragments.LoginFragment;
import com.firebase.client.core.Context;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Hero Alex on 6/11/2018.
 */

public class CopMap extends FragmentActivity implements OnMapReadyCallback{


    private GoogleMap mMap;



    public static ArrayMap<String, ArrayMap<String, String>> mapArrayMapUsers;


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
    }

    @Subscribe
    public void onMessageEvent(CopMap copMap){

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        LatLng latLngGPS = null;


        Log.d("coordonateFor", "COOOOORDONATE: " + mapArrayMapUsers);


        // eroare NullPointerExeption
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        for ( int i = 0; i < ServiceLogin.mHashArrayDataFirebase.size(); ++i){
                String[] coordonatesGPU = ServiceLogin.mHashArrayDataFirebase.valueAt(i).valueAt(1).toString().split(" ");
                Log.d("coordonateFor", "COOOOORDONATE: " + coordonatesGPU[0] + " : " + coordonatesGPU[1]);

                latLngGPS = new LatLng(Integer.parseInt(coordonatesGPU[0].toString()), Integer.parseInt(coordonatesGPU[1].toString()));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLngGPS)
                        .title(ServiceLogin.mHashArrayDataFirebase.valueAt(i).valueAt(0).toString());
                mMap.addMarker(markerOptions);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngGPS));
        }

        /*
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions markerOptions = new MarkerOptions().position(sydney)
                .title("MarkerSydney");
        mMap.addMarker(markerOptions);

        LatLng latLng = new LatLng(-50, 123);
        MarkerOptions markerOptions1 = new MarkerOptions().position(latLng)
                .title("CEVA");
        mMap.addMarker(markerOptions1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        /*
        LatLng sydney = new LatLng(-34, 151);

        // imbulinare cu drawable-uri custom
        BitmapDescriptor iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_stat_protection);

        MarkerOptions markerOptions = new MarkerOptions().position(sydney)
                .title("MarkerSydney")
                .icon(iconMarker);
        mMap.addMarker(markerOptions);

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/


    }

    @Subscribe
    public void receiveArrayMap(EventBusUsers eventBusUsers){
        this.mapArrayMapUsers = eventBusUsers.getmHashArrayDataFirebase();
    }
}
