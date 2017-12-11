package com.example.heroalex.copscivilslicense;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class RegisterCivil extends Activity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String mGPSCoordinates;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_log);

        spinnerSexAdapter();
        spineerBloodType();


        Button mButtonRegister = (Button) findViewById(R.id.button_registration);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //take values of labels
                final String FirstName = ((EditText) findViewById(R.id.input_first_name)).getText().toString();
                final String LastName = ((EditText) findViewById(R.id.input_last_name)).getText().toString();
                final String Password = ((EditText) findViewById(R.id.input_password_register)).getText().toString();
                final String Gender = ((Spinner) findViewById(R.id.spinner_sex_registration)).getSelectedItem().toString();
                final String BloodType = ((Spinner) findViewById(R.id.spinner_blood_type_registration)).getSelectedItem().toString();
                final String CloseOneName = ((EditText) findViewById(R.id.input_close_one_name)).getText().toString();
                final String CloseOneNumber = ((EditText) findViewById(R.id.input_close_one_number)).getText().toString();

                gpsCoordinatesMethod();

                //  mGPSCoordinates = "ceva";
                Toast.makeText(getApplicationContext(), mGPSCoordinates, Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference usersRef = database.getReference("civil");

                usersRef.setValue(new CivilData.CivilDataBuilder(FirstName, LastName)
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
        Spinner spinnerSex = (Spinner) findViewById(R.id.spinner_sex_registration);

        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_sex_array, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSex.setAdapter(adapterSex);
    }

    private void spineerBloodType() {
        Spinner spinnerBlood = (Spinner) findViewById(R.id.spinner_blood_type_registration);

        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_blood_stringArray, android.R.layout.simple_spinner_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerBlood.setAdapter(adapterBlood);
    }


    private void gpsCoordinatesMethod() {
        if(Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ))
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET}, 10);
                    return;
                }
                else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                }
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                /////////////////////////////////////////////////////////////////////// TREBUIE REFACUT CUMVA ///////////////////////////////////////////////////////////////////
                Toast.makeText(
                        getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                String longitude = "" + loc.getLongitude();

                String latitude = "" + loc.getLatitude();


                mGPSCoordinates = latitude + " " + longitude;

                Toast.makeText(getApplicationContext(), (String) mGPSCoordinates, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
    }



}
