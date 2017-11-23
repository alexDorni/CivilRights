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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    //button civil & cop
    private Button mButtonCivil = null;
    private Button mButtonCop = null;

    //for dialog
    private TextInputLayout mFieldEmailLayout;
    private AutoCompleteTextView mFieldEmail;
    private TextInputLayout mFieldPasswordLayout;
    private EditText mFieldPassword;
    private Dialog dialog;

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

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_log);

        mFieldEmailLayout = dialog.findViewById(R.id.layout_input_email);
        mFieldEmail = dialog.findViewById(R.id.input_email);
        mFieldPasswordLayout = dialog.findViewById(R.id.layout_input_passwd);
        mFieldPassword = dialog.findViewById(R.id.input_password);
        Button loginButton = dialog.findViewById(R.id.button_log_in);
        TextView txtRecuperareParola = dialog.findViewById(R.id.text_forgot_passwd);
        TextView txtRegister = dialog.findViewById(R.id.text_spec_register);

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
                Context ct = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(ct, "Trebuie facut cu uitatul parolei", duration).show();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterCivil.class);
                startActivity(i);
            }
        });


        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setAttributes(params);
        }
    dialog.show();
    }

    // TODO asta
    private void validateFields(){
        String email = mFieldEmail.getText().toString().trim();
        String password = mFieldPassword.getText().toString().trim();

        mFieldPasswordLayout.setErrorEnabled(false);
        mFieldEmailLayout.setErrorEnabled(false);

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

        //Verificare cu firebase
    }


}
