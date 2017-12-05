package com.example.heroalex.copscivilslicense;

import android.app.Activity;
import android.os.Bundle;
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

public class RegisterCivil extends Activity{

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_log);

        spinnerSexAdapter();
        spineerBloodType();

        final String FirstName = ((EditText) findViewById(R.id.input_first_name)).getText().toString();
        final String LastName = ((EditText) findViewById(R.id.input_last_name)).getText().toString();
        final String Password = ((EditText) findViewById(R.id.input_password_register)).getText().toString();
        final String Gender = ((Spinner) findViewById(R.id.spinner_sex_registration)).getSelectedItem().toString();
        final String BloodType = ((Spinner) findViewById(R.id.spinner_blood_type_registration)).getSelectedItem().toString();
        final String CloseOneName = ((EditText) findViewById(R.id.input_close_one_name)).getText().toString();
        final String CloseOneNumber = ((EditText) findViewById(R.id.input_close_one_number)).getText().toString();



        Button mButtonRegister = (Button) findViewById(R.id.button_registration);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                // ----------------------------------------------------------------------------------------------------------------------------------//
                // de ce este NULL FIRST NAME UL...



                Toast.makeText(getApplicationContext(), FirstName, Toast.LENGTH_SHORT).show();


                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference usersRef = database.getReference("civil");

               //usersRef.setValue("HELOOOO");

                //WRITE IN FIREBASE WORKING BUT NULL
               /* usersRef.setValue(new CivilData.CivilDataBuilder(FirstName, LastName)
                        .password(Password)
                        .gender(Gender)
                        .bloodType(BloodType)
                        .closeOneName(CloseOneName)
                        .closeOneNumber(CloseOneNumber)
                        .build());*/
            }
        });

    }


    private void spinnerSexAdapter(){
        Spinner spinnerSex = (Spinner) findViewById(R.id.spinner_sex_registration);

        ArrayAdapter<CharSequence> adapterSex =  ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_sex_array, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSex.setAdapter(adapterSex);
    }
    private void spineerBloodType(){
        Spinner spinnerBlood = (Spinner) findViewById(R.id.spinner_blood_type_registration);

        ArrayAdapter<CharSequence> adapterBlood =  ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_blood_stringArray, android.R.layout.simple_spinner_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerBlood.setAdapter(adapterBlood);
    }

    private void RegisterUser(){

        //send to firebase

    }
}
