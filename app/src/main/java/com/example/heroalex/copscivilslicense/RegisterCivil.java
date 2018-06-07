package com.example.heroalex.copscivilslicense;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Hero Alex on 11/23/2017.
 */

public class RegisterCivil extends AppCompatActivity{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private static final String TAG = "MyActivity";

    private Firebase mRootRef;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String mGPSCoordinates = "null";


    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_log);


//-------------------------------------------   UPDATE AUTOMAT LA GPS COORDONATES ---------------------------------------//////////////////

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mGPSCoordinates = location.getLatitude() + " " + location.getLongitude();
                Toast.makeText(RegisterCivil.this, mGPSCoordinates, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(RegisterCivil.this, "GPS provider enabled:" + provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(RegisterCivil.this, "GPS provider disabled:" + provider, Toast.LENGTH_SHORT).show();
            }
        };

        spinnerSexAdapter();
        spineerBloodType();

        Button mButtonRegister = findViewById(R.id.button_registration);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //take values of labels
                final String FirstName = ((EditText) findViewById(R.id.input_first_name)).getText().toString();
                final String LastName = ((EditText) findViewById(R.id.input_last_name)).getText().toString();
                final String Password = ((EditText) findViewById(R.id.input_password_register)).getText().toString();
                final String Gender = ((Spinner) findViewById(R.id.spinner_sex_registration)).getSelectedItem().toString();
                final String EmailSign = ((EditText) findViewById(R.id.sign_up_email)).getText().toString().replaceAll("[.]", ",");
                final String BloodType = ((Spinner) findViewById(R.id.spinner_blood_type_registration)).getSelectedItem().toString();
                final String CloseOneName = ((EditText) findViewById(R.id.input_close_one_name)).getText().toString();
                final String CloseOneNumber = ((EditText) findViewById(R.id.input_close_one_number)).getText().toString();

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                DatabaseReference usersRef = mRootRef.child("civils").child(EmailSign);

                usersRef.setValue(new CivilData.CivilDataBuilder()
                        .firstName(FirstName)
                        .lastName(LastName)
                        .password(Password)
                        .gender(Gender)
                        .bloodType(BloodType)
                        .closeOneName(CloseOneName)
                        .closeOneNumber(CloseOneNumber)
                        .gps(mGPSCoordinates)
                        .build());
            }
        });

    }

    private void spinnerSexAdapter() {
        Spinner spinnerSex = findViewById(R.id.spinner_sex_registration);

        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_sex_array, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSex.setAdapter(adapterSex);
    }

    private void spineerBloodType() {
        Spinner spinnerBlood = findViewById(R.id.spinner_blood_type_registration);

        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_blood_stringArray, android.R.layout.simple_spinner_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerBlood.setAdapter(adapterBlood);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getGpsCoordinates();
    }

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // cere permisiuni
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        // avem permisiuni
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permisiuni acordate cerem din nou coordonatele
                    getGpsCoordinates();
                } else {
                    Toast.makeText(RegisterCivil.this, "No permission for this request", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void getGpsCoordinates() {
        if (checkPermission()) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("Location", "GPS enabled");
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

            } else {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0 , 0, mLocationListener);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mLocationListener);
    }
}
