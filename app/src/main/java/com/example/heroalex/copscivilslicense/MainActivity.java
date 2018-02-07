package com.example.heroalex.copscivilslicense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    //button civil & cop
    private Button mButtonCivil = null;
    private Button mButtonCop = null;

    //for dialog
    private TextInputLayout mFieldEmailLayout;
    private AutoCompleteTextView mFieldEmail;
    private TextInputLayout mFieldPasswordLayout;
    private EditText mFieldPassword;
    private Dialog mDialog;

    //data Users in firebase

    /*
    private final int mmaxFirebaseChilds = 8;

    public enum menumDataFirebase{
        BLOOD("bloodType", 0),
        CLOSEONENAME("closeOneName", 1),
        CLOSEONENUMBER("closeOneNumber", 2),
        FIRSTNAME("firstName", 3),
        GENDER("gender", 4),
        GPSCOORDINATES("gpscoordinates", 5),
        LASTNAME("lastName", 6),
        PASSWORD("password", 7);

        private String stringValue;
        private int intValue;
        private menumDataFirebase(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }
        private menumDataFirebase(int value){
            intValue = value;


        }
        public void funct(){

        }
        @Override
        public String toString() {

            return stringValue;
        }
    }
    */
    /*
    private final static String[] arrayData = {
            "bloodType",
            "closeOneName",
            "closeOneNumber",
            "firstName",
            "gender",
            "gpscoordonates",
            "lastName",
            "password"
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mButtonCivil = findViewById(R.id.civilButton);
        mButtonCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();
            }
        });

        mButtonCop = findViewById(R.id.copButton);
        mButtonCop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void createDialog(){

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_log);

        mFieldEmailLayout = mDialog.findViewById(R.id.layout_input_email);
        mFieldEmail = mDialog.findViewById(R.id.input_email);
        mFieldPassword = mDialog.findViewById(R.id.input_password);
        Button loginButton = mDialog.findViewById(R.id.button_log_in);
        TextView txtRecuperareParola = mDialog.findViewById(R.id.text_forgot_passwd);
        TextView txt_Sign_Up_Register = mDialog.findViewById(R.id.text_spec_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connection with FireBase
                validateFields();
            }
        });

        txtRecuperareParola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Trebuie facut cu uitatul parolei", Toast.LENGTH_SHORT).show();
            }
        });

        txt_Sign_Up_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterCivil.class);
                startActivity(i);
            }
        });


        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if(mDialog.getWindow()!=null) {
            mDialog.getWindow().setAttributes(params);
        }
    mDialog.show();
    }

    private void validateFields(){

        String email = mFieldEmail.getText().toString();
        String password = mFieldPassword.getText().toString();

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


        //-------------------------------------------   FUNCTIONEAZA PENTRU A VERIFICA O VARIABILA DIN FIREBASE ---------------------------------------//////////////////

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mmm = mRootRef.child("civils").child(email.replaceAll("[.]", ",")).child("firstName");

            mRootRef.child("civils").child(email.replaceAll("[.]", ",")).child("firstName").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Toast.makeText(getApplicationContext(), dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();

//-------------------------------------------   intra si face update la o variabila data. ---------------------------------------//////////////////

                            mmm.setValue("ceva");

                            Toast.makeText(getApplicationContext(), dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
    }


}
