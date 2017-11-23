package com.example.heroalex.copscivilslicense;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Hero Alex on 11/23/2017.
 */

public class RegisterCivil extends Activity{

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_log);
        Spinner spinnerSex = (Spinner) findViewById(R.id.spinner_sex_registration);

        ArrayAdapter<CharSequence> adapterSex =  ArrayAdapter.createFromResource(RegisterCivil.this, R.array.spinner_sex_array, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSex.setAdapter(adapterSex);
    }
}
