package com.example.heroalex.copscivilslicense;

import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heroalex.copscivilslicense.fragments.IndexFragment;

public class MainActivity extends AppCompatActivity{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private static Context context;

    // button civil & cop
    private Button mButtonCivil = null;
    private Button mButtonCop = null;


    static final int NOTIFICATION_ID = 543;


    // for dialog
    private Dialog mDialog;
    public static String mEmailName;

    // ok for login
    private static int succesOK = -1;

    public static Notification notification;
    public static NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonCivil = findViewById(R.id.civilButton);
        mButtonCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BaseActivity.class));
            }
        });

        mButtonCop = findViewById(R.id.copButton);
        mButtonCop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //Intent i = new Intent(BaseActivity.class);

            }
        });
        //checkPermission();
    }


/*
    private void validateFields(){

        String variableEmail = mFieldEmail.getText().toString();
        final String email = variableEmail.replaceAll("[.]", ",");
        final String password = mFieldPassword.getText().toString();

        if (TextUtils.isEmpty(email)){

            Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show();
            mFieldEmail.setError("Please enter the email");
            return;
        }
        if (TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please insert the password", Toast.LENGTH_SHORT).show();
            mFieldPassword.setError("Please insert the password");
            return;
        }

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("civils");

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot civils : dataSnapshot.getChildren()) {
                    String key = civils.getKey(); // the email
                    if (email.equals(key)) {
                        Log.d("runt", "Key:" + key);
                        for (DataSnapshot civilsInfo : civils.getChildren()) {

                            if ("password".equals(civilsInfo.getKey())) {

                                // the password field
                                String passwordInfo = civilsInfo.getValue().toString();
                                Log.d("runt", "val:" + passwordInfo);

                                if (password.equals(passwordInfo)) {

                                    //succes
                                    succesOK = 1;
                                    Toast.makeText(getApplicationContext(), "Succes Login", Toast.LENGTH_SHORT).show();
                                    Log.d("runtSucc", "intrat");
                                    Log.d("SuccOkLogin: ", "" + succesOK);
                                    break;
                                } else {
                                    //error password
                                    succesOK = 0;
                                    Toast.makeText(getApplicationContext(), "Password or email incorrect", Toast.LENGTH_SHORT).show();
                                    Log.d("runtErrorPass", "val: " + passwordInfo);
                                }
                            }

                        }
                        //succes login
                        if (succesOK == 1) {
                            //for background coordonates
                            mEmailName = email;
                            Intent startIntent = new Intent(getApplicationContext(), ServiceLogin.class);
                            startIntent.setAction("start");
                            startService(startIntent);

                            return;
                        }
                    }
                }
                Log.d("succesOKstatus", "succesOK: " + succesOK);
                if (succesOK == -1) {
                    //error email
                    Log.d("runtErrorMail", "Key:" + succesOK);
                    Toast.makeText(getApplicationContext(), "Password or email incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
*/
    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // cere permisiuni
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
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
                   Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No permission for this request", Toast.LENGTH_SHORT).show();
                }
        }
    }


}
