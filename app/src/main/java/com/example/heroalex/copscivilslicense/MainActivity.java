package com.example.heroalex.copscivilslicense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //button civil & cop
    private Button mButtonCivil = null;
    private Button mButtonCop = null;

    //for dialog
    public TextInputLayout mFieldEmailLayout;
    public AutoCompleteTextView mFieldEmail;
    public TextInputLayout mFieldPasswordLayout;
    public EditText mFieldPassword;
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
                Toast t = Toast.makeText(ct, "Hoho a intrat", duration);
                t.show();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ct = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast t = Toast.makeText(ct, "Hoho a intrat pe 2", duration);
                t.show();
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

    }


}
